alter table tb_despesas change tipo categoria varchar(100);
update tb_despesas set categoria = "Outras"