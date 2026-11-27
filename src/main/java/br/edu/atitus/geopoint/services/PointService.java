package br.edu.atitus.geopoint.services;

import br.edu.atitus.geopoint.dtos.PointRequest;
import br.edu.atitus.geopoint.entities.PointEntity;
import br.edu.atitus.geopoint.exceptions.BadRequestException;
import br.edu.atitus.geopoint.exceptions.NotFoundException;
import br.edu.atitus.geopoint.repositories.PointRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;





//REGRAS




@Service
public class PointService {

    private static final String MSG_NAO_ENCONTRADO = "Ponto não localizado.";

    private final PointRepository repository;

    public PointService(PointRepository repository) {
        this.repository = repository;
    }

    public PointEntity insert(PointRequest request) {
        PointEntity point = validarEMontar(request);

        if (repository.existsByName(point.getName()))
            throw new BadRequestException("Já existe um ponto cadastrado com o nome informado.");

        point.setId(UUID.randomUUID());
        repository.insert(point);
        return point;
    }

    public List<PointEntity> findAll() {
        return repository.findAll();
    }

    public PointEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(MSG_NAO_ENCONTRADO));
    }

    public PointEntity update(UUID id, PointRequest request) {
        findById(id);

        PointEntity point = validarEMontar(request);

        if (repository.existsByNameAndIdNot(point.getName(), id))
            throw new BadRequestException("Já existe outro ponto cadastrado com o nome informado.");

        point.setId(id);
        repository.update(point);
        return point;
    }

    public void delete(UUID id) {
        findById(id);
        repository.deleteById(id);
    }


    private PointEntity validarEMontar(PointRequest request) {
        if (request == null)
            throw new BadRequestException("Os dados do ponto devem ser informados.");

        String name = request.name() == null ? "" : request.name().trim();
        if (name.isEmpty())
            throw new BadRequestException("O nome é obrigatório e não pode conter apenas espaços.");
        if (name.length() > 100)
            throw new BadRequestException("O nome deve ter no máximo 100 caracteres.");

        String description = request.description() == null ? null : request.description().trim();
        if (description != null && description.length() > 255)
            throw new BadRequestException("A descrição deve ter no máximo 255 caracteres.");

        if (request.latitude() == null)
            throw new BadRequestException("A latitude é obrigatória.");
        if (foraDoIntervalo(request.latitude(), -90, 90))
            throw new BadRequestException("A latitude deve estar entre -90 e 90.");

        if (request.longitude() == null)
            throw new BadRequestException("A longitude é obrigatória.");
        if (foraDoIntervalo(request.longitude(), -180, 180))
            throw new BadRequestException("A longitude deve estar entre -180 e 180.");

        PointEntity point = new PointEntity();
        point.setName(name);
        point.setDescription(description);
        point.setLatitude(request.latitude());
        point.setLongitude(request.longitude());
        return point;
    }

    private boolean foraDoIntervalo(BigDecimal valor, int minimo, int maximo) {
        return valor.compareTo(BigDecimal.valueOf(minimo)) < 0
                || valor.compareTo(BigDecimal.valueOf(maximo)) > 0;
    }
}