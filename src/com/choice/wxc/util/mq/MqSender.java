package com.choice.wxc.util.mq;

import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.choice.wxc.util.Commons;

/**
 * 消息发送者
 */
public class MqSender {
	
	public static final String BROKER_URL = Commons.getConfig().getProperty("broker_url");//MQ发送地址
//	public static final String BROKER_URL = "tcp://183.62.56.51:61616";//MQ发送地址
	
	/**
	 * 
	 * @param queneName 
	 * @throws Exception
	 */
	public static void sendOrders(String str, String queneName,int effectiveTime) throws Exception {
		QueueConnection connection = null;
		QueueSession session = null;
		
		try {
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD,BROKER_URL);
			connection = factory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queneName);
			QueueSender sender = session.createSender(queue);
			sender.setDeliveryMode(DeliveryMode.PERSISTENT);
			//如果有限时间不等于0
			if(effectiveTime!=0){
				sender.setTimeToLive(effectiveTime);//设置消息有效时间
			}
			//调用发送方法
			sendMessage(session, sender,str);
//			LogUtil.writeToTxt(LogUtil.MQSENDORDER, "发送消息到"+queneName+"内容："+str);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
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
	/**
	 * 方式数据到mq消息队列中
	 * @author  ybg
	 * @param session
	 * @param sender
	 * @param str
	 * @throws Exception
	 */
	public static void sendMessage(QueueSession session, QueueSender sender,String str) throws Exception {
            
//            ObjectMessage message = session.createObjectMessage();
//		message.setObject(str);
//			str = new String(str.getBytes("GBK"), "ISO-8859-1");
            TextMessage message = session.createTextMessage(str);
            message.setIntProperty("text", 0);
            message.setStringProperty("language", "C");
            sender.send(message);
//            try {  
//                Thread.sleep(1000*2);  
//            } catch (InterruptedException x) {  
//            } 
	}
	
    public static void main(String[] args) throws Exception{
    	String str = "{ \"delmenuList\" : [  ], \"id\" : \"\", \"menuList\" : [ { \"cnt\" : \"2\", \"fujiaList\" : [  ], \"money\" : \"0.2\", \"price\" : \"0.1\", \"vcode\" : \"104001\", \"vname\" : \"\u8336\u4f4d\u8d39\", \"ycnt\" : \"2\", \"ymoney\" : \"0.2\", \"zcnt\" : \"0\", \"zmoney\" : \"0.00\" } ], \"paymentList\" : [  ], \"paymoney\" : \"0.20\", \"resv\" : \"15052914483767\", \"type\" : \"5\" }";
//    	String str = "DELETE from cboh_folio_3ch WHERE vscode eq '1001' and vposid eq '1' and dworkdate eq '2015-05-06' and vorderid eq 'P000012'^insert into cboh_folio_3ch(PK_FOLIO,vbcode,vscode,pk_store,vecode,icanrefound,vtablenum,ipeolenum,ipeolenumman,ipeolenumwoment,verrorstring,vorderid,vposid,vjecode,dbrtime,dertime,dortime,vorclass,VANTICLASS,dbcashdrawer,decashdrawer,dokrtime, icclass,ilclass,nmoney,nymoney,nzmoney,novermoney,iprintcount,ICHANGETABLE,dworkdate,istate,idatasource,vename,vinputcode,vinputname,vhandevid,dbtabletime,detabletime,vpaycode,vselllistid,voldorderid,vtcstate,nbzero,iopeninvoice,ninvoicemoney,imisorderid,nsvr,vordertoendtime,vservingendtime,vpreprinttime,VCLEARZEROYN,nTip,nTransdif,ioldpeoplenum,ichildrennum,pk_group,VSHOUCLIENT)values('20150506184407985000','1001120150506P000012','1001','d3fcb9bc434a4702a456','2','1','(12)','1','1','0','','P000012','1','1','2015-05-06 18:44:02','2015-05-06 18:44:05','2015-05-06 18:44:07','1','0','2015-05-06 18:44:07','2015-05-06 18:44:07','2015-05-06 18:44:07','3','1','10','10','0','0','1','1','2015-05-06','4','1','李四','','','888','2015-05-06 18:44:00','2015-05-06 18:44:05','','','','','0','0','0','0','1','2015-05-06 18:44:04','2015-05-06 18:44:04','2015-05-06 18:44:05','1','0.000','0.000','0','0','~','0')^DELETE from cboh_foliopayment_3ch WHERE vscode eq '1001' and vposid eq '1' and dworkdate eq '2015-05-06' and vorders eq 'P000012'^insert into cboh_foliopayment_3ch(PK_FOLIOPAYMENT,pk_store,vbcode,vscode,vecode,tctime,vrcode,nspend,ngiftspend,nbeforeje,nbeforejf,vposid,voperate,nmoney,vorders,dworkdate,ncashone,novercash,icashcount,isshow,NPAIDMONEY,NPOUNDAGE,VHUNGACOUNTNO,ICHANGETABLE,pk_group)values('20150506184407986000','d3fcb9bc434a4702a456','1001120150506P000012','1001','2','2015-05-06 18:44:07','000','0','0','0','0','1','10','11','P000012','2015-05-06','0','0','1','0','0','0','','1','~')^DELETE from cboh_ordr_3ch WHERE VSCODE eq '1001' and VPOSID eq '1' and DWORKDATE eq '2015-05-06' and VORDERID eq 'P000012'^insert into cboh_ordr_3ch(VORDERID,PK_ORDR,vbcode,pk_store,vscode,nprice,vpcode,vccleassname,vpname,nmoney,nymoney,nzmoney,nzsmoney,ncount,nycount,nzcount,ntcount,vdept,ioperates,dworkdate,vtccode,nprintorderid,nkitchenprintorderid,nproductshoworderid,vpit,vposid,ichangetable,ncounts,nycounts,nzcounts,vunits,nunitcur,ikitchencount,ipcount,irushnumber,nservicefee,iccallupcount,iaddcount,iaddnumber,pk_package,iflag,CLEARZEROYN,nbzero,DBRTIME,VRECODE,DRUSHTIME,VRUSHECODE,DPULLTIME,VPULLECODE,VRETURNTIME,VRETURNECODE,VRETURNACCREDIT,VGIVEACCREDIT,DGIVETIME,VGIVEECODE,irushcount,ipullcount,ISUBLISTID)values('P000012','20150506184407989000','1001120150506P000012','d3fcb9bc434a4702a456','1001','5','1113','配菜','鸡蛋','5','5','0','0','1','1','0','0','1','1','2015-05-06','0','0','0','0','A','1','1','0','0','0','份','1','0','1','0','0.5','0','0','0','','0','1','0.00000','2015-05-06 18:44:02','2','','','','','','','','','','','0','0','')";
//    	JSONObject json = JSONObject.fromObject(str);
//    	json.put("type", "1");
//    	System.out.println(json.toString());
	    	MqSender.sendOrders( str,"orderreList",0);
    	System.exit(0);
    }
}
