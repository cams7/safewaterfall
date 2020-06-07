DROP TABLE IF EXISTS tb_sensor CASCADE;
CREATE TABLE tb_sensor
(
    id_sensor character varying(36) NOT NULL,
    distancia_max smallint NOT NULL,
    distancia_min smallint NOT NULL,
    env_alerta_cron character varying(30) NOT NULL,
    env_status_cron character varying(30) NOT NULL,
    arduino_status_cron character varying(30) NOT NULL,
    CONSTRAINT pk_tb_sensor PRIMARY KEY (id_sensor),
    CONSTRAINT tb_sensor_distancia_max_check CHECK (distancia_max >= 0 AND distancia_max <= 255),
    CONSTRAINT tb_sensor_distancia_min_check CHECK (distancia_min >= 0 AND distancia_min <= 255)
)