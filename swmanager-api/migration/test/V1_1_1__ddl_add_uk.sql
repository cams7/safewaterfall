alter table TB_SIRENE add constraint UK_TB_SIR_ID_DISP unique (ID_DISPOSITIVO);
alter table TB_SENSOR add constraint UK_TB_SEN_ID_DISP unique (ID_DISPOSITIVO);