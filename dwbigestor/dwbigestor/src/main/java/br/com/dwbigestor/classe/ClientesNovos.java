package br.com.dwbigestor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class ClientesNovos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigovendedor;
	private String nomevendedor;
	
	private BigDecimal codigocliente;
	private String nomecliente;
	private String nomefantasia;
	private String cpfcnpj;
	private Date datacadastro;

	public ClientesNovos() {
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

}
