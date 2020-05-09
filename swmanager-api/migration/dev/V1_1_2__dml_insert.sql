insert into TB_SIRENE (ID_SIRENE, ENDERECO_SIRENE) values(nextval('SQ_SIRENE'), 'http://127.0.0.1:8280');
insert into TB_SENSOR (ID_SENSOR, ARDUINO_STATUS_CRON, ENV_STATUS_CRON, ENV_ALERTA_CRON, DISTANCIA_MIN, DISTANCIA_MAX, ID_SIRENE, ENDERECO_SENSOR) values(nextval('SQ_SENSOR'), '0/3 * * ? * * *', '0 0/1 * ? * * *', '0/10 * * ? * * *', 100, 255, 1, 'http://127.0.0.1:8080');
