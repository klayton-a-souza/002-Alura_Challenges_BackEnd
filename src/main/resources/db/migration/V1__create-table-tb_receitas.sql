create table tb_receitas(

    id_receita bigint not null auto_increment,
    descricao varchar(100) not null,
    valor decimal(10,2),
    data timestamp,
    ativo tinyint,

    primary key(id_receita)
);