package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="dwbi_pedidolog")
public class CPedidoLog  implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private Integer idlog;
	
	@Column(nullable=false)
	private String pedido;
	
	@Column(nullable=false,columnDefinition="varchar(50)")
	private String usuario;
	
	@Column(nullable=false,columnDefinition="varchar(250)")
	private String descricao;
	
	@Column(nullable=false,columnDefinition="varchar(100)")
	private String status; //LIBERADO - NÃO-LIBERADO -
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
		
	public CPedidoLog() {
		super();
	}

	public Integer getIdlog() {
		return idlog;
	}

	public void setIdlog(Integer idlog) {
		this.idlog = idlog;
	}

	public String getPedido() {
		return pedido;
	}

	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idlog == null) ? 0 : idlog.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CPedidoLog other = (CPedidoLog) obj;
		if (idlog == null) {
			if (other.idlog != null)
				return false;
		} else if (!idlog.equals(other.idlog))
			return false;
		return true;
	}

	
}
