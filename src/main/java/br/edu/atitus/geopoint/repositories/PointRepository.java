package br.edu.atitus.geopoint.repositories;

import org.springframework.jdbc.core.JdbcTemplate;

public class PointRepository {

    private final JdbcTemplate jdbcTemplate;

    public PointRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
