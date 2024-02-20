package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class ClienteNovo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigocliente;
	private String nomecliente;
	private String nomefantasia;
	private String cpfcnpj;
	private Date datacadastro;
	private BigDecimal codigovendedor;
	private String nomevendedor;
	private String gestor;
	private BigInteger vendas;
	private Date primeira;
	private Date ultima;
	private BigDecimal totalvenda;

	public ClienteNovo() {
		super();
	}

	public BigDecimal getCodigovendedor() {
		return codigovendedor;
	}

	public void setCodigovendedor(BigDecimal codigovendedor) {
		this.codigovendedor = codigovendedor;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}

	public BigDecimal getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(BigDecimal codigocliente) {
		this.codigocliente = codigocliente;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public String getNomefantasia() {
		return nomefantasia;
	}

	public void setNomefantasia(String nomefantasia) {
		this.nomefantasia = nomefantasia;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public Date getDatacadastro() {
		return datacadastro;
	}

	public void setDatacadastro(Date datacadastro) {
		this.datacadastro = datacadastro;
	}

	public BigInteger getVendas() {
		return vendas;
	}

	public void setVendas(BigInteger vendas) {
		this.vendas = vendas;
	}

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}

	public Date getPrimeira() {
		return primeira;
	}

	public void setPrimeira(Date primeira) {
		this.primeira = primeira;
	}

	public Date getUltima() {
		return ultima;
	}

	public void setUltima(Date ultima) {
		this.ultima = ultima;
	}

	public BigDecimal getTotalvenda() {
		return totalvenda;
	}

	public void setTotalvenda(BigDecimal totalvenda) {
		this.totalvenda = totalvenda;
	}
	

}
