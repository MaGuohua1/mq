package com.ma.embed;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
/**
 * 
 * @author mgh_2
 *
 * @desription 嵌入式mq
 */
public class EmbedMQ {

	public static void main(String[] args) {
		try {
			BrokerService service = new BrokerService();
			service.addConnector("tcp://localhost:67000");
			service.setBrokerName("EmbedMQ");
			service.setManagementContext(new ManagementContext());
			service.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
