package com.balicamp.webapp.scheduler;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RunMeTask {

	public static void main(String[] args) throws Exception {

		// new
		// ClassPathXmlApplicationContext("classpath:WEB-INF/applicationContext-dao.xml");

		new FileSystemXmlApplicationContext(
				"F:/webadmin2/mx-webadmin/webadmin-web/src/main/webapp/WEB-INF/applicationContext-dao.xml");

	}
}
