drop sequence if exists SQ_SIRENE;
drop sequence if exists SQ_SENSOR;

create sequence SQ_SIRENE start 1 increment 1;
create sequence SQ_SENSOR start 1 increment 1;

drop table if exists TB_SENSOR cascade;
drop table if exists TB_SIRENE cascade;

create table TB_SIRENE
(
    ID_SIRENE bigint not null,
    ID_DISPOSITIVO character varying(36) not null,
    ENDERECO_SIRENE character varying(100) not null,
    constraint PK_TB_SIRENE primary key (ID_SIRENE)
);

create table TB_SENSOR
(
    ID_SENSOR bigint not null,
    ID_DISPOSITIVO character varying(36) not null,
    DISTANCIA_MAX smallint not null,
    DISTANCIA_MIN smallint not null,
    ENV_ALERTA_CRON character varying(30) not null,
    ENV_STATUS_CRON character varying(30) not null,
    ENDERECO_SENSOR character varying(100) not null,
    ARDUINO_STATUS_CRON character varying(30) not null,
    ID_SIRENE bigint,
    constraint PK_TB_SENSOR primary key (ID_SENSOR),
    constraint FK_TB_SEN_ID_SIR foreign key (ID_SIRENE) references TB_SIRENE (ID_SIRENE),
    constraint TB_SENSOR_DISTANCIA_MAX_CHECK check (DISTANCIA_MAX >= 0 and DISTANCIA_MAX <= 255),
    constraint TB_SENSOR_DISTANCIA_MIN_CHECK check (DISTANCIA_MIN >= 0 and DISTANCIA_MIN <= 255)
);