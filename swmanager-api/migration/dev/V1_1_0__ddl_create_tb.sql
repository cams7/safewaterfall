drop sequence if exists SQ_SIRENE;
drop sequence if exists SQ_SENSOR;

create sequence SQ_SIRENE start with 1 increment by 1;
create sequence SQ_SENSOR start with 1 increment by 1;

drop table TB_SENSOR if exists;
drop table TB_SIRENE if exists;

create table TB_SIRENE (
	ID_SIRENE bigint not null,
	ID_DISPOSITIVO char(36) not null,
	ENDERECO_SIRENE varchar(100) not null,
	primary key (ID_SIRENE)
);
create table TB_SENSOR (
	ID_SENSOR bigint not null,
	ID_DISPOSITIVO char(36) not null,
	ARDUINO_STATUS_CRON varchar(30) not null,
	ENV_STATUS_CRON varchar(30) not null,
	ENV_ALERTA_CRON varchar(30) not null,
	DISTANCIA_MIN integer not null,
	DISTANCIA_MAX integer not null,
	ID_SIRENE bigint not null,
	ENDERECO_SENSOR varchar(100) not null,
	primary key (ID_SENSOR)
);