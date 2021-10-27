CREATE TABLE IF NOT EXISTS j_car (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS j_brand (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(2000),
                                      role_id INT NOT NULL REFERENCES j_car(id)
);