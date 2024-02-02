package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

public class LigacaoH implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	private Integer error;
	private String reason;
	private Integer total_records;
	private Integer total_time;
	private String total_time_text;
	private String total_size;
	private Integer limit;
	private Integer offset;
	private Integer records;
	
	@OneToMany
	private List<Ligacao> data = new ArrayList<>();
	
	public LigacaoH() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getError() {
		return error;
	}

	public void setError(Integer error) {
		this.error = error;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getTotal_records() {
		return total_records;
	}

	public void setTotal_records(Integer total_records) {
		this.total_records = total_records;
	}

	public Integer getTotal_time() {
		return total_time;
	}

	public void setTotal_time(Integer total_time) {
		this.total_time = total_time;
	}

	public String getTotal_time_text() {
		return total_time_text;
	}

	public void setTotal_time_text(String total_time_text) {
		this.total_time_text = total_time_text;
	}

	public String getTotal_size() {
		return total_size;
	}

	public void setTotal_size(String total_size) {
		this.total_size = total_size;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;
	}

	public List<Ligacao> getData() {
		return data;
	}

	public void setData(List<Ligacao> data) {
		this.data = data;
	}

}
