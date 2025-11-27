create table persona
(
    id             serial
        primary key,
    nombre         varchar(100) not null,
    genero         varchar(20),
    edad           integer,
    identificacion varchar(20)  not null
        unique,
    direccion      varchar(200),
    telefono       varchar(20)
);

create table cliente
(
    cliente_id serial
        primary key
        constraint fk_cliente_persona
            references persona,
    contrasena varchar(100) not null,
    estado     boolean default true
);

create table cuenta
(
    numero_cuenta varchar(20)    not null
        primary key,
    tipo_cuenta   varchar(20)    not null,
    saldo_inicial numeric(15, 2) not null,
    estado        boolean default true,
    cliente_id    integer        not null
);

create table movimientos
(
    id              serial
        primary key,
    fecha           timestamp default CURRENT_TIMESTAMP,
    tipo_movimiento varchar(50)    not null,
    valor           numeric(15, 2) not null,
    saldo           numeric(15, 2) not null,
    numero_cuenta   varchar(20)    not null
        constraint fk_cuenta
            references cuenta
);
