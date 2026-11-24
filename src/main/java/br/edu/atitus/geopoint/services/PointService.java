package br.edu.atitus.geopoint.services;

import br.edu.atitus.geopoint.dtos.PointRequest;
import br.edu.atitus.geopoint.entities.PointEntity;

import java.math.BigDecimal;
import java.util.UUID;

public class PointService {

    public void insert(PointRequest pointRequest) {
        //TODO validar os dados
        if (pointRequest == null)
            throw new RuntimeException("objeto nulo");
        if(pointRequest.name() == null || pointRequest.name().isBlank())
            throw new RuntimeException("Nome invalido");
        if (pointRequest.latitude() == null
        || pointRequest.latitude().compareTo(BigDecimal.valueOf(-90)) < 0
        || pointRequest.latitude().compareTo(BigDecimal.valueOf(90)) > 0)
            throw new RuntimeException("Latitude inválida - inter");

        //
        //

        PointEntity point = new PointEntity();

        point.setId(UUID.randomUUID());
        point.setName(pointRequest.name());
        point.setDescription(pointRequest.description().trim());
        point.setLatitude(pointRequest.latitude());
        point.setLongitude(pointRequest.longitude());





    }
}
