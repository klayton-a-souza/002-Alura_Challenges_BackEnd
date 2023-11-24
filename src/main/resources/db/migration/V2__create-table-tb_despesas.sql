create table tb_despesas(

    id_despesa bigint not null auto_increment,
    descricao varchar(100) not null,
    valor decimal(10,2),
    data timestamp,
    ativo tinyint,
    tipo varchar(100) not null,

    primary key(id_despesa)
);