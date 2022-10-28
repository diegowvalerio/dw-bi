package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;


import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.classe.PrazoPedido;
import br.com.dwbidiretor.servico.ServicoFasePedido;
import br.com.dwbidiretor.servico.ServicoPedidoFase;
import br.com.dwbidiretor.servico.ServicoPrazoPedido;


@Named
@ViewScoped
public class BeanPrazoPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	private PrazoPedido prazopedido = new PrazoPedido();
	@Inject
	private ServicoPrazoPedido servico;
	private List<PrazoPedido> lista = new ArrayList<>();
	
	int venda,outros = 1 ;
	int selecionado  = 1;
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	

	@PostConstruct
	public void init() {
	
		
	}
	
	public void filtrar() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(selecionado == 1) {
			venda = 1;
			outros = 1;
		}else if(selecionado == 2) {
			venda = 1;
			outros = 0;
		}else if(selecionado == 3) {
			venda = 0;
			outros = 1;
		}
		lista = servico.prazopedido(venda, outros, data_grafico, data_grafico2);
		if(lista.size()>0) {
			for(PrazoPedido p:lista) {
				Date a,b = null;
				long dt = 0;
				int d = 0;
				
				if(p.getFase_atual().equals("DIGITACAO")) {
					a = sdf.parse(p.getDt_digitacao());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("FINANCEIRO")) {
					a = sdf.parse(p.getDt_financeiro());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_financeiro(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("CONFERENCIA")) {
					a = sdf.parse(p.getDt_conferencia());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_conferencia(d);
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("ANALISE GESTOR")) {
					a = sdf.parse(p.getDt_analisegestor());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_analisegestor(d);
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			        
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);
			        
			       
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("COLOR SELECT")) {
					a = sdf.parse(p.getDt_color());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_color(d);
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        if(p.getDt_analisegestor() == null) {
			        	p.setDt_analisegestor(p.getDt_conferencia());
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			        
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);
			        
			        a = sdf.parse(p.getDt_analisegestor());
					b = sdf.parse(p.getDt_color());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_analisegestor(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("PROGRAMACAO")) {
					a = sdf.parse(p.getDt_programacao());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_programacao(d);
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        if(p.getDt_analisegestor() == null) {
			        	p.setDt_analisegestor(p.getDt_conferencia());
			        }
			        if(p.getDt_color() == null) {
			        	p.setDt_color(p.getDt_analisegestor());
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			        
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);
			        
			        a = sdf.parse(p.getDt_analisegestor());
					b = sdf.parse(p.getDt_color());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_analisegestor(d);
			        
			        a = sdf.parse(p.getDt_color());
					b = sdf.parse(p.getDt_programacao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_color(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("PRODUCAO COLOR")) {
					if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        if(p.getDt_analisegestor() == null) {
			        	p.setDt_analisegestor(p.getDt_conferencia());
			        }
			        if(p.getDt_color() == null) {
			        	p.setDt_color(p.getDt_analisegestor());
			        }
			        if(p.getDt_programacao() == null) {
			        	p.setDt_programacao(p.getDt_color());
			        }
					if(p.getDt_producaocolor() == null) {
			        	p.setDt_producaocolor(p.getDt_programacao());
			        }
					
					a = sdf.parse(p.getDt_producaocolor());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_producaocolor(d);
			        
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			        
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);
			        
			        a = sdf.parse(p.getDt_analisegestor());
					b = sdf.parse(p.getDt_color());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_analisegestor(d);
			        
			        a = sdf.parse(p.getDt_color());
					b = sdf.parse(p.getDt_programacao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_color(d);
			        
			        a = sdf.parse(p.getDt_programacao());
					b = sdf.parse(p.getDt_producaocolor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_programacao(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("PRODUCAO")) {
					a = sdf.parse(p.getDt_producao());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_producao(d);
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        if(p.getDt_analisegestor() == null) {
			        	p.setDt_analisegestor(p.getDt_conferencia());
			        }
			        if(p.getDt_color() == null) {
			        	p.setDt_color(p.getDt_analisegestor());
			        }
			        if(p.getDt_programacao() == null) {
			        	p.setDt_programacao(p.getDt_color());
			        }
			        if(p.getDt_producaocolor() == null) {
			        	p.setDt_producaocolor(p.getDt_programacao());
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			        
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);
			        
			        a = sdf.parse(p.getDt_analisegestor());
					b = sdf.parse(p.getDt_color());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_analisegestor(d);
			        
			        a = sdf.parse(p.getDt_color());
					b = sdf.parse(p.getDt_programacao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_color(d);
			        
			        a = sdf.parse(p.getDt_programacao());
					b = sdf.parse(p.getDt_producaocolor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_programacao(d);
			        
			        a = sdf.parse(p.getDt_producaocolor());
					b = sdf.parse(p.getDt_producao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_producaocolor(d);
			        
			        if(d < 0) {
			        	String t = p.getDt_producaocolor();
						p.setDt_producaocolor(p.getDt_producao());
						p.setDt_producao(t);
						
						a = sdf.parse(p.getDt_programacao());
						b = sdf.parse(p.getDt_producaocolor());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_programacao(d);
				        
				        a = sdf.parse(p.getDt_producaocolor());
						b = sdf.parse(p.getDt_producao());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_producaocolor(d);
				        
				        a = sdf.parse(p.getDt_producao());
						b = new Date();
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_producao(d);
					}
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("EXPEDICAO")) {
					a = sdf.parse(p.getDt_expedicao());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_expedicao(d);
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        if(p.getDt_analisegestor() == null) {
			        	p.setDt_analisegestor(p.getDt_conferencia());
			        }
			        if(p.getDt_color() == null) {
			        	p.setDt_color(p.getDt_analisegestor());
			        }
			        if(p.getDt_programacao() == null) {
			        	p.setDt_programacao(p.getDt_color());
			        }
			        if(p.getDt_producaocolor() == null) {
			        	p.setDt_producaocolor(p.getDt_programacao());
			        }
			        if(p.getDt_producao() == null) {
			        	p.setDt_producao(p.getDt_producaocolor());
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			       
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);	        
			        
			        a = sdf.parse(p.getDt_analisegestor());
					b = sdf.parse(p.getDt_color());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_analisegestor(d);			        
			        
			        a = sdf.parse(p.getDt_color());
					b = sdf.parse(p.getDt_programacao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_color(d);		        
			        
			        a = sdf.parse(p.getDt_programacao());
					b = sdf.parse(p.getDt_producaocolor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_programacao(d);
			        
			        a = sdf.parse(p.getDt_producaocolor());
					b = sdf.parse(p.getDt_producao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_producaocolor(d);
			        
			        if(d < 0) {
			        	String t = p.getDt_producaocolor();
						p.setDt_producaocolor(p.getDt_producao());
						p.setDt_producao(t);
						
						a = sdf.parse(p.getDt_programacao());
						b = sdf.parse(p.getDt_producaocolor());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_programacao(d);
				        
				        a = sdf.parse(p.getDt_producaocolor());
						b = sdf.parse(p.getDt_producao());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_producaocolor(d);
				        
					}
			        
			        a = sdf.parse(p.getDt_producao());
					b = sdf.parse(p.getDt_expedicao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_producao(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
				}
				
				if(p.getFase_atual().equals("FATURAMENTO")) {
					a = sdf.parse(p.getDt_faturamento());
					b = new Date();
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_faturamento(d);
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        if(p.getDt_analisegestor() == null) {
			        	p.setDt_analisegestor(p.getDt_conferencia());
			        }
			        if(p.getDt_color() == null) {
			        	p.setDt_color(p.getDt_analisegestor());
			        }
			        if(p.getDt_programacao() == null) {
			        	p.setDt_programacao(p.getDt_color());
			        }
			        if(p.getDt_producaocolor() == null) {
			        	p.setDt_producaocolor(p.getDt_programacao());
			        }
			        if(p.getDt_producao() == null) {
			        	p.setDt_producao(p.getDt_producaocolor());
			        }
			        
			        if(p.getDt_expedicao() == null) {
			        	p.setDt_expedicao(p.getDt_producao());
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			       
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);	        
			        
			        a = sdf.parse(p.getDt_analisegestor());
					b = sdf.parse(p.getDt_color());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_analisegestor(d);			        
			        
			        a = sdf.parse(p.getDt_color());
					b = sdf.parse(p.getDt_programacao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_color(d);		        
			        
			        a = sdf.parse(p.getDt_programacao());
					b = sdf.parse(p.getDt_producaocolor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_programacao(d);
			        
			        a = sdf.parse(p.getDt_producaocolor());
					b = sdf.parse(p.getDt_producao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_producaocolor(d);	
			        
			        if(d < 0) {
			        	String t = p.getDt_producaocolor();
						p.setDt_producaocolor(p.getDt_producao());
						p.setDt_producao(t);
						
						a = sdf.parse(p.getDt_programacao());
						b = sdf.parse(p.getDt_producaocolor());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_programacao(d);
				        
				        a = sdf.parse(p.getDt_producaocolor());
						b = sdf.parse(p.getDt_producao());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_producaocolor(d);
				        
					}
			        
			        a = sdf.parse(p.getDt_producao());
					b = sdf.parse(p.getDt_expedicao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_producao(d);
			        
			        a = sdf.parse(p.getDt_expedicao());
					b = sdf.parse(p.getDt_faturamento());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_expedicao(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
			        
			        if(p.getStatus().equals("IMPORTADO")) {
				        p.setDias_faturamento(0);
			        }
				}
				
				if(p.getFase_atual().equals("POS VENDA")) {
			        
			        if(p.getDt_financeiro() == null) {
			        	if(p.getDt_conferencia() != null) {
			        		p.setDt_financeiro(p.getDt_conferencia());	
			        	}else {
			        		p.setDt_financeiro(p.getDt_digitacao());
			        	}
			        }
			        if(p.getDt_conferencia() == null) {
			        	p.setDt_conferencia(p.getDt_financeiro());
			        }
			        if(p.getDt_analisegestor() == null) {
			        	p.setDt_analisegestor(p.getDt_conferencia());
			        }
			        if(p.getDt_color() == null) {
			        	p.setDt_color(p.getDt_analisegestor());
			        }
			        if(p.getDt_programacao() == null) {
			        	p.setDt_programacao(p.getDt_color());
			        }
			        if(p.getDt_producaocolor() == null) {
			        	p.setDt_producaocolor(p.getDt_programacao());
			        }
			        if(p.getDt_producao() == null) {
			        	p.setDt_producao(p.getDt_producaocolor());
			        }
			        if(p.getDt_expedicao() == null) {
			        	p.setDt_expedicao(p.getDt_producao());
			        }
			        if(p.getDt_faturamento() == null) {
			        	p.setDt_faturamento(p.getDt_expedicao());
			        }
			        
			        a = sdf.parse(p.getDt_financeiro());
					b = sdf.parse(p.getDt_conferencia());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_financeiro(d);
			       
			        a = sdf.parse(p.getDt_conferencia());
					b = sdf.parse(p.getDt_analisegestor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_conferencia(d);	        
			        
			        a = sdf.parse(p.getDt_analisegestor());
					b = sdf.parse(p.getDt_color());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_analisegestor(d);			        
			        
			        a = sdf.parse(p.getDt_color());
					b = sdf.parse(p.getDt_programacao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_color(d);		        
			        
			        a = sdf.parse(p.getDt_programacao());
					b = sdf.parse(p.getDt_producaocolor());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_programacao(d);
			        
			        a = sdf.parse(p.getDt_producaocolor());
					b = sdf.parse(p.getDt_producao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
			        p.setDias_producaocolor(d);	
			        
			        if(d < 0) {
			        	String t = p.getDt_producaocolor();
						p.setDt_producaocolor(p.getDt_producao());
						p.setDt_producao(t);
						
						a = sdf.parse(p.getDt_programacao());
						b = sdf.parse(p.getDt_producaocolor());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_programacao(d);
				        
				        a = sdf.parse(p.getDt_producaocolor());
						b = sdf.parse(p.getDt_producao());
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_producaocolor(d);
				        
					}
			        
			        a = sdf.parse(p.getDt_producao());
					b = sdf.parse(p.getDt_expedicao());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_producao(d);
			        
			        a = sdf.parse(p.getDt_expedicao());
					b = sdf.parse(p.getDt_faturamento());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_expedicao(d);
			        
			        a = sdf.parse(p.getDt_faturamento());
					b = sdf.parse(p.getDt_posvenda());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_faturamento(d);
			        
			        a = sdf.parse(p.getDt_digitacao());
					b = sdf.parse(p.getDt_financeiro());
					dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;	
					d = (int) dt;
					if(d < 0) {
						d= 0;
					}
			        p.setDias_digitacao(d);
			        
			        if(!p.getStatus().equals("IMPORTADO")) {
			        	a = sdf.parse(p.getDt_posvenda());
						b = new Date();
						dt = b.getTime() - a.getTime();
						dt = dt / 86400000L;	
						d = (int) dt;
				        p.setDias_posvenda(d);
			        }
				}
				
		        d = p.getDias_digitacao() + p.getDias_financeiro() + p.getDias_conferencia()
		        	+ p.getDias_analisegestor()+ p.getDias_color()+ p.getDias_programacao() 
		        	+ p.getDias_producaocolor() + p.getDias_producao()+ p.getDias_expedicao()
		        	+ p.getDias_faturamento();
		        p.setTotaldias(d);		        	        
			}
		}
	}


	public PrazoPedido getPrazopedido() {
		return prazopedido;
	}

	public void setPrazopedido(PrazoPedido prazopedido) {
		this.prazopedido = prazopedido;
	}

	public List<PrazoPedido> getLista() {
		return lista;
	}

	public void setLista(List<PrazoPedido> lista) {
		this.lista = lista;
	}

	public int getVenda() {
		return venda;
	}

	public void setVenda(int venda) {
		this.venda = venda;
	}

	public int getOutros() {
		return outros;
	}

	public void setOutros(int outros) {
		this.outros = outros;
	}

	public int getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(int selecionado) {
		this.selecionado = selecionado;
	}

	public Date getData_grafico() {
		return data_grafico;
	}

	public void setData_grafico(Date data_grafico) {
		this.data_grafico = data_grafico;
	}

	public Date getData_grafico2() {
		return data_grafico2;
	}

	public void setData_grafico2(Date data_grafico2) {
		this.data_grafico2 = data_grafico2;
	}

	
	
}
