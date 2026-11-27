CREATE TABLE IF NOT EXISTS points (
   id UUID PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    latitude NUMERIC(10, 7) NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL
    );

CREATE UNIQUE INDEX IF NOT EXISTS ux_points_name ON points (LOWER(name));