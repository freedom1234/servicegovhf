package com.dc.esb.servicegov.process.impl;

import org.drools.SystemEventListenerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.workitem.wsht.SyncWSHumanTaskHandler;
import org.jbpm.task.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.local.LocalTaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

@Service("jbpmSupport")
public class JbpmSupport {

	@Resource
	@Qualifier("entityManagerFactory")
	private EntityManagerFactory entityManagerFactory;

	@Resource
	@Qualifier("ksession")
	private StatefulKnowledgeSession ksession;

	private TaskServiceSession taskServiceSession;

	/**
	 * <dt>核心就是两个session：
	 * <dd>KnowledgeSession 可以完全通过spring drools配置成功
	 * <dd>TaskServiceSession 这里通过手动编码产生local service
	 */
	@PostConstruct
	public void init() {
		// 为 ksession 设置log
		new JPAWorkingMemoryDbLogger(ksession);
		new JPAProcessInstanceDbLog(ksession.getEnvironment());

		// 创建 local human service 及其 handler
		org.jbpm.task.service.TaskService tService = new org.jbpm.task.service.TaskService(entityManagerFactory,
				SystemEventListenerFactory.getSystemEventListener());
		taskServiceSession = tService.createSession();
		// TODO 事务该如何设置？
		// taskServiceSession.setTransactionType("RESOURCE_LOCAL");
		SyncWSHumanTaskHandler humanTaskHandler = new SyncWSHumanTaskHandler(new LocalTaskService(taskServiceSession),
				ksession);
		humanTaskHandler.setLocal(true);
		humanTaskHandler.connect();
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);

		// set user group callback
		System.setProperty("jbpm.usergroup.callback", "org.jbpm.task.service.DefaultUserGroupCallbackImpl");
	}

	public StatefulKnowledgeSession getKsession() {
		return ksession;
	}

	public TaskService getTaskService() {
		return new LocalTaskService(taskServiceSession);
	}

}
