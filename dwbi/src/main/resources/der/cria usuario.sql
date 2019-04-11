INSERT INTO tbpessoa(dtcadastro, nome, situacao, tipojf)
VALUES ('2018-01-29', 'Admin', true, 'F');

INSERT INTO tbfisica(cpf, dtnasc, rg, idpessoa)
    VALUES ('001.001.001-01', '2018-01-29', '01.001.001-0', 1);

INSERT INTO tbusuario(login, senha, idpessoa)
    VALUES ('admin', '123', 1);