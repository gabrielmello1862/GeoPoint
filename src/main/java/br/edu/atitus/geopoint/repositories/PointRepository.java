package br.edu.atitus.geopoint.repositories;

import br.edu.atitus.geopoint.entities.PointEntity;
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


    private final RowMapper<PointEntity> pointMapper = (rs, rowNum) -> {
        PointEntity point = new PointEntity();
        point.setId(rs.getObject("id", UUID.class));
        point.setName(rs.getString("name"));
        point.setDescription(rs.getString("description"));
        point.setLatitude(rs.getBigDecimal("latitude"));
        point.setLongitude(rs.getBigDecimal("longitude"));
        return point;
    };

    public void insert(PointEntity point) {
        String sql = "INSERT INTO points (id, name, description, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, point.getId(), point.getName(), point.getDescription(),
                point.getLatitude(), point.getLongitude());
    }

    public List<PointEntity> findAll() {
        String sql = "SELECT id, name, description, latitude, longitude FROM points ORDER BY name";
        return jdbcTemplate.query(sql, pointMapper);
    }

    public Optional<PointEntity> findById(UUID id) {
        String sql = "SELECT id, name, description, latitude, longitude FROM points WHERE id = ?";
        return jdbcTemplate.query(sql, pointMapper, id).stream().findFirst();
    }

    public void update(PointEntity point) {
        String sql = "UPDATE points SET name = ?, description = ?, latitude = ?, longitude = ? WHERE id = ?";
        jdbcTemplate.update(sql, point.getName(), point.getDescription(),
                point.getLatitude(), point.getLongitude(), point.getId());
    }

    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM points WHERE id = ?", id);
    }

    public boolean existsByName(String name) {
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM points WHERE LOWER(name) = LOWER(?)", Integer.class, name);
        return total != null && total > 0;
    }

    public boolean existsByNameAndIdNot(String name, UUID id) {
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM points WHERE LOWER(name) = LOWER(?) AND id <> ?",
                Integer.class, name, id);
        return total != null && total > 0;
    }
}