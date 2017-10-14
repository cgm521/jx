package com.choice.wxc.util.mq;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.choice.wxc.util.DateFormat;

/**
 * 消息接收者
 */
public class MqReceiver {
	
	public static final String BROKER_URL = com.choice.wxc.util.Commons.getConfig().getProperty("broker_url");
	
	public static void getMqMessage(String queueName) throws Exception {
		QueueConnection connection = null;
		QueueSession session = null;
		
		try {
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD,BROKER_URL);
			connection = factory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);
			QueueReceiver receiver = session.createReceiver(queue);

			receiver.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message msg) {
					if(msg != null){
						TextMessage mess = (TextMessage)msg;
//						ObjectMessage mess = (ObjectMessage)msg;
						try {
//							Net_Orders net_Orders = (Net_Orders) mess.getObject();
							String str = mess.getText();
							System.out.println(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss")+str);
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
			Thread.sleep(1000*5);
			session.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			if(session != null){
				session.close();
			}
			if(connection != null){
				connection.close();
			}
		}
	}
 
    public static void main(String[] args) throws Exception {
    	MqReceiver.getMqMessage("1");
    	System.exit(0);
    }
}