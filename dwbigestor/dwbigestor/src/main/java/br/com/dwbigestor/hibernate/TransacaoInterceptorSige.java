package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.dwbigestor.fabrica.EntityManagerProducerSige.Corporativo;


@Priority(Interceptor.Priority.APPLICATION)
@Interceptor
@TransacaoSige
public class TransacaoInterceptorSige implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	@Corporativo
	private  EntityManager manager;

	@AroundInvoke
	@Corporativo
	public Object invoke(InvocationContext context) throws Exception {
		EntityTransaction transaction = manager.getTransaction();
		boolean owner = false;

		try {
			if (!transaction.isActive()) {
				transaction.begin();
				transaction.rollback();

				transaction.begin();

				owner = true;
			}

			return context.proceed();
		} catch (Exception e) {
			if (transaction != null && owner) {
				transaction.rollback();
			}

			throw e;
		} finally {
			if (transaction != null && transaction.isActive() && owner) {
				transaction.commit();
			}
		}
	}

}
