package com.choice.test.utils;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.choice.webService.BOHWebservice;


public class Client_CRM {
	public static void main(String[] args){
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
	    factoryBean.setServiceClass(BOHWebservice.class);       
	    factoryBean.setAddress("http://61.144.230.172/CTF/webService/BOHWebService?wsdl"); 
	    BOHWebservice bohService = (BOHWebservice)factoryBean.create();
		try {
			String boh=bohService.getOrderDtlMenus("6128f1b5fcf6478185a04da701c3a829");
			System.out.println(boh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getCard(String fromid,String content) throws Exception{
//		if(n1>0){
//			CRMWebservice crmService = (CRMWebservice)factoryBean.create();
//			String name=content.substring(0,content.indexOf("@"));
//			String mobTel=content.substring(content.indexOf("@")+1,content.length());
//			String cont = crmService.addCardWebService(name, mobTel, fromid);
//			if(cont !=null){
//				return cont;
//			}else{
//				return "添加会员失败，请按格式重新输入";
//			}
//		}
//		if(content.equals("1")){
//			JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
//		    factoryBean.setServiceClass(BOHWebservice.class);       
//		    factoryBean.setAddress(Commons.CRMwebService); 
//		    BOHWebservice crmService = (BOHWebservice)factoryBean.create();
//			String getCard=findCard(crmService.queryCardWebService(null,null, null, null, fromid,null));
//			if(null==getCard || getCard.equals("")){
//				return "regest";
//			}else{
//				String cardNo=getCard.substring(getCard.indexOf("会员卡号为：")+6,getCard.indexOf("\n会员姓名"));
//				String cardnum=crmService.generateQRCodeWebService(cardNo);
//				return cardnum+"@"+getCard;
//			}
//		}else if(content.equals("2")){
//			return "";
//		}else if(content.equals("3")){
//			JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();       
//		    factoryBean.setServiceClass(BOHWebservice.class);       
//		    factoryBean.setAddress(Commons.BOHwebService); 
//		    BOHWebservice bohWebservice = (BOHWebservice)factoryBean.create();
//		    Net_Orders order=findOrderMenu(bohWebservice.getOrderMenus(null,"1", fromid,null,null,null));
//		    if(null==order.getResv() || order.getResv().equals("")){
//				return "order";
//			}else{
				return "您有未消费的菜单请点击查看";
//			}
//		}else{
//			return Commons.vmessage;
//		}
//		return null;
	}
}