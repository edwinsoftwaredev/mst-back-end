create table tbl_user(
    id bigint(20) auto_increment,
    login varchar(50) not null,
    password_hash varchar(60) not null,
    email varchar(254) not null,
    created_by varchar(50),
    created_data timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp,
    constraint pk_user primary key(id)
);

create table tbl_token(
    id bigint(20) auto_increment,
    access_token varchar(254),
    token_type varchar(254),
    scope varchar(254),
    expires_in integer,
    refresh_token varchar(254),
    constraint pk_token primary key(id)
);

alter table tbl_user
add column token_id bigint(20);

alter table tbl_user
add constraint fk_token foreign key(token_id) references tbl_token(id);

alter table tbl_user
add column has_token boolean;

