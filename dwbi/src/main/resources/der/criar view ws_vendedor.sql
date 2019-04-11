create view ws_vendedor as select 
v.idpessoa,
v.perc_comissao,
p.nome,
p.situacao,
p.tipojf,
case when p.tipojf = 'F'
	then f.cpf
	else j.cnpj
	end as cpf_cnpj

from

tbvendedor v
inner join tbpessoa p on p.idpessoa = v.idpessoa
left join tbfisica f on f.idpessoa = p.idpessoa
left join tbjuridica j on j.idpessoa = p.idpessoa
;

