package br.edu.atitus.geopoint.repositories;

import br.edu.atitus.geopoint.entities.PointEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PointRepository {

    private final JdbcTemplate jdbcTemplate;

    public PointRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void criarTabelaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS points (
                id UUID PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE,
                description TEXT,
                latitude NUMERIC(10, 7) NOT NULL,
                longitude NUMERIC(10, 7) NOT NULL
            )
            """;
        jdbcTemplate.execute(sql);
    }

    private final RowMapper<PointEntity> rowMapper = (rs, rowNum) -> {
        PointEntity point = new PointEntity();
        point.setId(rs.getObject("id", UUID.class));
        point.setName(rs.getString("name"));
        point.setDescription(rs.getString("description"));
        point.setLatitude(rs.getBigDecimal("latitude"));
        point.setLongitude(rs.getBigDecimal("longitude"));
        return point;
    };

    public PointEntity save(PointEntity point) {
        String sql = "INSERT INTO points (id, name, description, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                point.getId(),
                point.getName(),
                point.getDescription(),
                point.getLatitude(),
                point.getLongitude());
        return point;
    }

    public List<PointEntity> findAll() {
        String sql = "SELECT id, name, description, latitude, longitude FROM points ORDER BY name ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<PointEntity> findById(UUID id) {
        String sql = "SELECT id, name, description, latitude, longitude FROM points WHERE id = ?";
        List<PointEntity> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.stream().findFirst();
    }

    public PointEntity update(PointEntity point) {
        String sql = "UPDATE points SET name = ?, description = ?, latitude = ?, longitude = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                point.getName(),
                point.getDescription(),
                point.getLatitude(),
                point.getLongitude(),
                point.getId());
        return point;
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM points WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM points WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM points WHERE LOWER(name) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public boolean existsByNameAndIdNot(String name, UUID id) {
        String sql = "SELECT COUNT(*) FROM points WHERE LOWER(name) = LOWER(?) AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, id);
        return count != null && count > 0;
    }
}