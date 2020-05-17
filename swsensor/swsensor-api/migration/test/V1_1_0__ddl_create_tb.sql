drop table TB_SENSOR if exists;
create table TB_SENSOR (
	ID_SENSOR char(36) not null,
	ARDUINO_STATUS_CRON varchar(30) not null,
	ENV_STATUS_CRON varchar(30) not null,
	ENV_ALERTA_CRON varchar(30) not null,
	DISTANCIA_MIN integer not null,
	DISTANCIA_MAX integer not null,
	primary key (ID_SENSOR)
);