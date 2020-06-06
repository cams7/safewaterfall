alter table if exists TB_SIRENE drop constraint if exists UK_TB_SIR_ID_DISP;
alter table if exists TB_SIRENE add constraint UK_TB_SIR_ID_DISP unique (ID_DISPOSITIVO);
alter table if exists TB_SENSOR drop constraint if exists UK_TB_SEN_ID_DISP;
alter table if exists TB_SENSOR add constraint UK_TB_SEN_ID_DISP unique (ID_DISPOSITIVO);