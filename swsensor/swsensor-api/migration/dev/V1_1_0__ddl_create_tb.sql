drop sequence if exists SQ_SENSOR;
create sequence SQ_SENSOR start with 1 increment by 1;

drop table TB_SENSOR if exists;
create table TB_SENSOR (
	ID_SENSOR bigint not null,
	ARDUINO_STATUS_CRON varchar(30) not null,
	ENV_STATUS_CRON varchar(30) not null,
	ENV_ALERTA_CRON varchar(30) not null,
	DISTANCIA_MIN integer not null,
	DISTANCIA_MAX integer not null,
	primary key (ID_SENSOR)
);