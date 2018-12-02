create table texttemplate(
	id_template serial primary key,
	mtemplate text,
	params text
)
insert into texttemplate (mtemplate, params) values ('Hello, dear % ', '1 - Name')