--liquibase formatted sql
--changeset postgres:1

create table texttemplate(
	id_template serial primary key,
	mtemplate text,
	params text
);
insert into texttemplate (mtemplate, params) values ('Hello, dear % ', '1 - Name')

create TABLE parameter(
  id_param SERIAL PRIMARY KEY ,
  key text,
  description text
);

alter table texttemplate drop column params;

CREATE TABLE TEMPLATE_PARAMS(
  id_template_params SERIAL PRIMARY KEY ,
  id_template int REFERENCES texttemplate (id_template) ,
  id_param int REFERENCES parameter (id_param)
);

alter table template_params drop column id_template_params;