-- INSERIR FORMA DE COBRANÇA --
INSERT INTO tbformapag(nome, situacao)
VALUES 
('BOLETO', true),
('DEPÓSITO', true),
('CHEQUE PRÉ', true),
('DESCONTO COMISSÃO', true) ;
--------------------------------
-- INSERIR CONDIÇÃO DE PAGAMENTO --
INSERT INTO tbcondicaopgto(diasentreparc,nome,qtdeparcelas,situacao)
values 
(0,'Avista',1,true),
(7,'07 Dias',1,true),
(14,'14 Dias',1,true),
(14,'14/28/42 Dias',3,true),
(14,'14/28/42/56 Dias',4,true),
(20,'20/40/60 Dias',3,true),
(30,'30 Dias',1,true),
(30,'30/60/90 Dias',3,true);
----------------------------------
-- INSERIR TIPO DE ENDEREÇOS --
INSERT INTO tbtipoendereco(nome)
values
('Entrega'),('Cobrança'),('Localização');
----------------------------------






