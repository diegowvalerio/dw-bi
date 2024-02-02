package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;

public class Ligacao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private Integer record_id;
	private String calldate;
	private String clid;
	private String source;
	private String destination;
	private Integer duration;
	private String duration2;
	private String size;
	
	private String dataoriginal;
	
	@ManyToOne
	private LigacaoH ligacao;
	
	public Ligacao() {
		super();
	}

	public Integer getRecord_id() {
		return record_id;
	}

	public void setRecord_id(Integer record_id) {
		this.record_id = record_id;
	}

	public String getCalldate() {
		return calldate;
	}

	public void setCalldate(String calldate) {
		this.calldate = calldate;
	}

	public String getClid() {
		return clid;
	}

	public void setClid(String clid) {
		this.clid = clid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDuration2() {
		return duration2;
	}

	public void setDuration2(String duration2) {
		this.duration2 = duration2;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public LigacaoH getLigacao() {
		return ligacao;
	}

	public void setLigacao(LigacaoH ligacao) {
		this.ligacao = ligacao;
	}

	public String getDataoriginal() {
		return dataoriginal;
	}

	public void setDataoriginal(String dataoriginal) {
		this.dataoriginal = dataoriginal;
	}

	
}
