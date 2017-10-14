<%@page import="com.choice.wechat.util.ValueCheck"%>
<%@page import="java.util.Date"%>  
<%@page import="org.dom4j.Element"%>  
<%@page import="org.dom4j.DocumentHelper"%>  
<%@page import="org.dom4j.Document"%>  
<%@page import="java.io.IOException"%>  
<%@page import="java.io.InputStreamReader"%>  
<%@page import="java.io.BufferedReader"%>  
<%@page import="java.io.Reader"%>  
<%@page import="java.security.MessageDigest"%>  
<%@page import="java.util.Arrays"%>  
<%@page import="com.choice.test.utils.Client_CRM"%>
<%@page import="com.choice.test.utils.Commons"%>
<%@page import="com.choice.test.service.PubitemSearch"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.choice.wechat.util.MessageUtil"%>
<%@page import="com.choice.wechat.persistence.common.location.LocationMapper"%>
<%@page import="javax.servlet.ServletContext"%>
<%@page import="com.choice.test.domain.Firm"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>  
<%String path = request.getContextPath();
	String port ="80".equals((request.getServerPort()+""))?"":(":"+request.getServerPort()+"");
	final String basePath = request.getScheme()+"://"+request.getServerName()+port+path;
%>
<%
  	//WeiXinHandler为内部类不能使用非final类型的对象  
      final String TOKEN="mytoken";  
      final HttpServletRequest final_request=request;   
      final HttpServletResponse final_response=response;
      final ServletContext context = final_request.getSession().getServletContext();
      final WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);
  %>  
<%
  	class WeiXinHandler{  
        public void valid(){  
            String echostr=final_request.getParameter("echostr");  
            if(null==echostr||echostr.isEmpty()){
            	try {
                    responseMsg();
            	} catch(Exception ex) {
            		String returnMsg = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[SUCCESS]]></return_msg></xml>";
            		this.print(returnMsg);
            	}
            }else{
                this.print(echostr);                                                                                          
            }  
        }  
        //自动回复内容  
        public void responseMsg(){  
            String postStr=null;  
            try{
                postStr=this.readStreamParameter(final_request.getInputStream());  
//                 postStr = new String(postStr.getBytes(),"UTF-8");
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            //System.out.println(postStr);  
            if (null!=postStr&&!postStr.isEmpty()){  
                Document document=null;  
                try{  
                    document = DocumentHelper.parseText(postStr);  
                }catch(Exception e){  
                    e.printStackTrace();  
                }  
                if(null==document){  
                    this.print("");  
                    return;  
                }  
                Element root=document.getRootElement();  
                String fromUsername = root.elementText("FromUserName");  //发送方账号
                String toUsername = root.elementText("ToUserName");  //开发者微信号
                String keyword = root.elementTextTrim("Content");//内容
                String msgType=root.elementTextTrim("MsgType");//类型
                String PicUrl=root.elementTextTrim("PicUrl");//类型
                String time = new Date().getTime()+"";
               
                String content="";
                String textTpl = "";
                if("text".equals(msgType)){
    	            try{
    	            	if(null!=keyword&&!keyword.equals("")){
    	            		// 此处写消息存储的方法
    	            		// pubitemSearch.xxx(openid);
    	            		MessageUtil util = (MessageUtil)springContext.getBean("messageUtil");
    	            		textTpl = util.replyMsg(fromUsername,toUsername,keyword,"text");
    	            	}
    	            }catch(Exception e){
    	            	e.printStackTrace();
    	            }
                }else if("image".equals(msgType)){
    	            try{
    	            	if(null!=PicUrl&&!PicUrl.equals("")){
    	            		// 此处写消息存储的方法
    	            		// pubitemSearch.xxx(openid);
    	            		MessageUtil util = (MessageUtil)springContext.getBean("messageUtil");
    	            		textTpl = util.replyMsg(fromUsername,toUsername,PicUrl,"image");
    	            	}
    	            }catch(Exception e){
    	            	e.printStackTrace();
    	            }
                }else if("event".equals(msgType)){
              	  String event=root.elementTextTrim("Event");
              	  if(event.equals("subscribe")){
              		String texteventKey=root.elementTextTrim("EventKey");
              		if(texteventKey !=null && !texteventKey.equals("")){
                		  // 如果是微信墙，存储
                		  if(texteventKey.indexOf("wechatWall") >= 0) {
                  			String firmid = texteventKey.split("_")[1];
                  			Firm  listFirm = PubitemSearch.getStoreByFirmId(firmid);
	                  		  String firmName = "";
	                  		  if(null != listFirm ) {
	                  			  firmName = listFirm.getFirmdes();
	                  		  }
                			  //此处写存储openid和门店对应关系的方法  
                			  PubitemSearch.addRelationShipFirmOpenid(toUsername,fromUsername,firmid); 
                			  //推送微信墙消息
                			  textTpl="<xml>"+
        		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
        		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
        		            		"<CreateTime>123</CreateTime>"+
        		            		"<MsgType><![CDATA[news]]></MsgType>"+
        		            		"<ArticleCount>1</ArticleCount>"+
        		            		"<Articles>"+
        		            		"<item>"+
//         		            		"<Title><![CDATA[你已进入太二屌炸天弹幕系统啦，点键盘标志说话呀！]]></Title>"+
        		            		"<Title><![CDATA[尊敬的顾客，欢迎进入"+firmName+"微信墙，点键盘标志发言]]></Title>"+
        		            		"<Description><![CDATA[想说唠叨点儿就快点动手打字吧！输入【下墙】退出微信墙。]]></Description>"+
        		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
        		            		"<Url><![CDATA["+Commons.vtakeOrdrurl+"?tables=1&openid="+fromUsername+"&firmid=" + firmid + "&createTime="+"]]></Url>"+
        		            		"</item>"+
        		            		"</Articles>"+
        		            		"</xml>" ;
						  } else if(texteventKey.endsWith("_register")){
                    			//推送注册信息
                			  textTpl="<xml>"+
        		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
        		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
        		            		"<CreateTime>123</CreateTime>"+
        		            		"<MsgType><![CDATA[news]]></MsgType>"+
        		            		"<ArticleCount>1</ArticleCount>"+
        		            		"<Articles>"+
        		            		"<item>"+
        		            		"<Title><![CDATA[尊敬的顾客，请先注册]]></Title>"+
        		            		"<Description><![CDATA[点击消息进入注册页面。]]></Description>"+
        		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
        		            		"<Url><![CDATA["+basePath+"/myCard/cardInfo.do?openid="+fromUsername+"&firmid="+texteventKey.split("_")[1]+"]]></Url>"+
        		            		"</item>"+
        		            		"</Articles>"+
        		            		"</xml>" ;
                		  } else  if(texteventKey.split("_").length == 2) {
//                   			String telephone = texteventKey.split("_")[3];
                  			String res = PubitemSearch.setTeleWithOpenid(texteventKey,fromUsername);
                  			if("0".equals(res)){
	              			  //推送等位消息
	              			  textTpl="<xml>"+
	      		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
	      		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
	      		            		"<CreateTime>123</CreateTime>"+
	      		            		"<MsgType><![CDATA[news]]></MsgType>"+
	      		            		"<ArticleCount>1</ArticleCount>"+
	      		            		"<Articles>"+
	      		            		"<item>"+
	      		            		"<Title><![CDATA[尊敬的顾客，欢迎使用微信等位系统]]></Title>"+
	      		            		"<Description><![CDATA[点击消息查看当前等位信息。]]></Description>"+
	      		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
	      		            		"<Url><![CDATA["+basePath+"/waitSeat/myWaitInfo.do?openId="+fromUsername+"]]></Url>"+
	      		            		"</item>"+
	      		            		"</Articles>"+
	      		            		"</xml>" ;
                  				}else{
                  					textTpl="<xml>"+
        	      		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
        	      		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
        	      		            		"<Content><![CDATA[查询错误请重新扫码]]></Content>"+
        	      		            		"<CreateTime>123</CreateTime>"+
        	      		            		"<MsgType><![CDATA[text]]></MsgType>"+
        	      		            		"<FuncFlag><![CDATA[0]]></FuncFlag>"+
        	      		            		"</xml>" ;
                  				}
              		  } else {

                    			String firmid = texteventKey.split("_")[1];
                        		String tableId = texteventKey.split("_")[2];
                        		long createTime = System.currentTimeMillis();
                        	    String expireTime = Commons.getConfig().getProperty("scanExpireTime");
                        	    if(null == expireTime || expireTime.isEmpty()) {
                        		    expireTime = "5";
                        	    }

	                        	  Firm  listFirm = PubitemSearch.getStoreByFirmId(firmid);
	                    		  String firmName = "";
	                    		  if(null != listFirm ) {
	                    			  firmName = listFirm.getFirmdes();
	                    		  }
	                    		  Map<String,Object> storeSeatMap = PubitemSearch.getStoreSeatInfo(firmid,tableId);
	                    		  String seatName = "";
	                    		  if(ValueCheck.IsNotEmpty(storeSeatMap)){
	                    			  seatName = storeSeatMap.get("vname")+"";
	                    		  }
	                    		  String isShow = PubitemSearch.isWxchatDianCan(firmid);
	                    		  
	                    		  if("Y".equals(isShow)){
	                    			  textTpl="<xml>"+
	          	  		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
	          	  		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
	          	  		            		"<CreateTime>123</CreateTime>"+
	          	  		            		"<MsgType><![CDATA[news]]></MsgType>"+
	          	  		            		"<ArticleCount>1</ArticleCount>"+
	          	  		            		"<Articles>"+
	          	  		            		"<item>"+
	              		            		"<Title><![CDATA[您已经进入"+firmName+"台位"+seatName+"，请点击图片点菜]]></Title>"+
	              		            		"<Description><![CDATA["+Commons.vtitle+"。此链接"+expireTime+"分钟内有效]]></Description>"+
	          	  		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
	          	  		            		"<Url><![CDATA["+Commons.vtakeOrdrurl+"?tables="+tableId+"&openid="+fromUsername+"&firmid=" + firmid + "&createTime=" + createTime + "]]></Url>"+
	          	  		            		"</item>"+
	          	  		            		"</Articles>"+
	          	  		            		"</xml>" ;
	                    		  }else{
	                    			  textTpl="<xml>"+
	        	      		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
	        	      		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
	        	      		            		"<Content><![CDATA[该店暂不提供微信自助点餐服务]]></Content>"+
	        	      		            		"<CreateTime>123</CreateTime>"+
	        	      		            		"<MsgType><![CDATA[text]]></MsgType>"+
	        	      		            		"<FuncFlag><![CDATA[0]]></FuncFlag>"+
	        	      		            		"</xml>" ;
	                    		  }
        	             		
                    		}
                		  System.out.println("1222==== ===="+textTpl);
                  		} else{
	        				MessageUtil util = (MessageUtil)springContext.getBean("messageUtil");
		            		textTpl = util.replyMsg(fromUsername,toUsername,"subscribe","event");
        				}
              	  }else if(event.equals("SCAN")){
          			  Map scanEventMap = MessageUtil.getScanEventMap();
          			  String scanEvent = "";
          			  if(null == scanEventMap) {
          				  scanEventMap = new HashMap<String, String>();
          			  }
          			  if(null != scanEventMap && null != scanEventMap.get(fromUsername)) {
          				  scanEvent = scanEventMap.get(fromUsername).toString();
          			  }
              		  String texteventKey=root.elementTextTrim("EventKey");
              		  // 如果是微信墙，存储
              		  if(texteventKey.indexOf("wechatWall") >= 0) {
                  			String firmid = texteventKey.split("_")[0];
                  			Firm  listFirm = PubitemSearch.getStoreByFirmId(firmid);
	                  		  String firmName = "";
	                  		  if(null != listFirm ) {
	                  			  firmName = listFirm.getFirmdes();
	                  		  }
               			  //此处写存储openid和门店对应关系的方法  
               			  PubitemSearch.addRelationShipFirmOpenid(toUsername,fromUsername,firmid); 
              			  //推送微信墙消息
              			  textTpl="<xml>"+
      		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
      		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
      		            		"<CreateTime>123</CreateTime>"+
      		            		"<MsgType><![CDATA[news]]></MsgType>"+
      		            		"<ArticleCount>1</ArticleCount>"+
      		            		"<Articles>"+
      		            		"<item>"+
//       		            		"<Title><![CDATA[你已进入太二屌炸天弹幕系统啦，点键盘标志说话呀！]]></Title>"+
      		            		"<Title><![CDATA[尊敬的顾客，欢迎进入"+firmName+"微信墙，点键盘标志发言]]></Title>"+
      		            		"<Description><![CDATA[想说唠叨点儿就快点动手打字吧！输入【下墙】退出微信墙。]]></Description>"+
      		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
      		            		"<Url><![CDATA[]]></Url>"+
      		            		"</item>"+
      		            		"</Articles>"+
      		            		"</xml>" ;
              		  } else if(texteventKey.endsWith("_register")){
              			//推送注册信息
            			  textTpl="<xml>"+
    		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
    		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
    		            		"<CreateTime>123</CreateTime>"+
    		            		"<MsgType><![CDATA[news]]></MsgType>"+
    		            		"<ArticleCount>1</ArticleCount>"+
    		            		"<Articles>"+
    		            		"<item>"+
    		            		"<Title><![CDATA[尊敬的顾客，请先注册]]></Title>"+
    		            		"<Description><![CDATA[点击消息进入注册页面。]]></Description>"+
    		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
    		            		"<Url><![CDATA["+basePath+"/myCard/cardInfo.do?openid="+fromUsername+"&firmid="+texteventKey.split("_")[0]+"]]></Url>"+
    		            		"</item>"+
    		            		"</Articles>"+
    		            		"</xml>" ;
              		  } else  if(texteventKey.split("_").length ==1) {
//                 			String telephone = texteventKey.split("_")[2];
                  			String res = PubitemSearch.setTeleWithOpenid(texteventKey,fromUsername);
                  			if("0".equals(res)){
	            			  //推送等位消息
	            			  textTpl="<xml>"+
	    		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
	    		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
	    		            		"<CreateTime>123</CreateTime>"+
	    		            		"<MsgType><![CDATA[news]]></MsgType>"+
	    		            		"<ArticleCount>1</ArticleCount>"+
	    		            		"<Articles>"+
	    		            		"<item>"+
	    		            		"<Title><![CDATA[尊敬的顾客，欢迎使用微信等位系统]]></Title>"+
	    		            		"<Description><![CDATA[点击消息查看当前等位信息。]]></Description>"+
	    		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
	    		            		"<Url><![CDATA["+basePath+"/waitSeat/myWaitInfo.do?openId="+fromUsername+"]]></Url>"+
	    		            		"</item>"+
	    		            		"</Articles>"+
	    		            		"</xml>" ;
	                  			}else{
                  					textTpl="<xml>"+
        	      		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
        	      		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
        	      		            		"<Content><![CDATA[查询错误请重新扫码]]></Content>"+
        	      		            		"<CreateTime>123</CreateTime>"+
        	      		            		"<MsgType><![CDATA[text]]></MsgType>"+
        	      		            		"<FuncFlag><![CDATA[0]]></FuncFlag>"+
        	      		            		"</xml>" ;
                  				}
            		  } else {
            			  System.out.print("==========="+texteventKey);
              			  String firmid = texteventKey.split("_")[0];
                		  String tableId = texteventKey.split("_")[1];
                  	      long createTime = System.currentTimeMillis();
                  	      String expireTime = Commons.getConfig().getProperty("scanExpireTime");
                  	      if(null == expireTime || expireTime.isEmpty()) {
                  		      expireTime = "5";
                  	      }
            			  // 如果是扫码点单，不执行此方法
            			  if(!"1".equals(scanEvent)) {
                    		  Firm listFirm = PubitemSearch.getStoreByFirmId(firmid);
                    		  String firmName = "";
                    		  if(null != listFirm) {
                    			  firmName = listFirm.getFirmdes();
                    		  }
                    		  Map<String,Object> storeSeatMap = PubitemSearch.getStoreSeatInfo(firmid,tableId);
                    		  String seatName = "";
                    		  if(ValueCheck.IsNotEmpty(storeSeatMap)){
                    			  seatName = storeSeatMap.get("vname")+"";
                    		  }
                    		  String isShow = PubitemSearch.isWxchatDianCan(firmid);
                    		  
                    		  if("Y".equals(isShow)){
                    			  textTpl="<xml>"+
          	  		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
          	  		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
          	  		            		"<CreateTime>123</CreateTime>"+
          	  		            		"<MsgType><![CDATA[news]]></MsgType>"+
          	  		            		"<ArticleCount>1</ArticleCount>"+
          	  		            		"<Articles>"+
          	  		            		"<item>"+
              		            		"<Title><![CDATA[您已经进入"+firmName+"台位"+seatName+"，请点击图片点菜]]></Title>"+
              		            		"<Description><![CDATA["+Commons.vtitle+"。此链接"+expireTime+"分钟内有效]]></Description>"+
          	  		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
          	  		            		"<Url><![CDATA["+Commons.vtakeOrdrurl+"?tables="+tableId+"&openid="+fromUsername+"&firmid=" + firmid + "&createTime=" + createTime + "]]></Url>"+
          	  		            		"</item>"+
          	  		            		"</Articles>"+
          	  		            		"</xml>" ;
                    		  }else{
                    			  textTpl="<xml>"+
        	      		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
        	      		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
        	      		            		"<Content><![CDATA[该店暂不提供微信自助点餐服务]]></Content>"+
        	      		            		"<CreateTime>123</CreateTime>"+
        	      		            		"<MsgType><![CDATA[text]]></MsgType>"+
        	      		            		"<FuncFlag><![CDATA[0]]></FuncFlag>"+
        	      		            		"</xml>" ;
                    		  }

                    		System.out.println(Commons.vtakeOrdrurl+"---tables="+tableId);
            			  } else {
            				  String orderfirmid = MessageUtil.getScanEventMap().get(fromUsername + "_firmid");
            				  if(!firmid.equals(orderfirmid)) {
                				  String orderid = MessageUtil.getScanEventMap().get(fromUsername + "_orderid");
            					  textTpl="<xml>"+
            		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
            		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
            		            		"<CreateTime>123</CreateTime>"+
            		            		"<MsgType><![CDATA[news]]></MsgType>"+
            		            		"<ArticleCount>1</ArticleCount>"+
            		            		"<Articles>"+
            		            		"<item>"+
            		            		"<Title><![CDATA[下单失败]]></Title>"+
            		            		"<Description><![CDATA[失败原因：订单门店与实际门店不一致。点击重新扫码下单]]></Description>"+
            		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
            		            		"<Url><![CDATA["+Commons.vtakeOrdrurl.replace("gotoMenu", "orderDetail")+
            		            		               "?orderid="+orderid+"&openid="+fromUsername+"&firmid=" + orderfirmid + "]]></Url>"+
            		            		"</item>"+
            		            		"</Articles>"+
            		            		"</xml>" ;
            				  } else {
            					  //扫码下单
                				  String res = PubitemSearch.updateOrdr(fromUsername, firmid, tableId);
            				  }
                			  scanEventMap.put(fromUsername, "0");
                			  MessageUtil.setScanEventMap(scanEventMap);
            			  }
              		  }
              	  }else if(event.equals("unsubscribe")){
              		  content="取消关注！";
              	  }else if(event.equals("CLICK")){
              		System.out.println("==============================="+event);  
              		  String eventKey = root.elementTextTrim("EventKey");
              		  if(null!=eventKey&&!eventKey.equals("")){
              			MessageUtil util = (MessageUtil)springContext.getBean("messageUtil");
	            		textTpl = util.replyMsg(fromUsername,toUsername,eventKey,"event");
              			  /*
                  		  try{
                  			  content= Client_CRM.getCard(fromUsername,eventKey);
                  			  if(eventKey.equals("1")){
  	                			  if(content.equals("regest")){
  // 		                			  textTpl="<xml>"+
  // 		            		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
  // 		            		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
  // 		            		            		"<CreateTime>123</CreateTime>"+
  // 		            		            		"<MsgType><![CDATA[news]]></MsgType>"+
  // 		            		            		"<ArticleCount>1</ArticleCount>"+
  // 		            		            		"<Articles>"+
  // 		            		            		"<item>"+
  // 		            		            		"<Title><![CDATA[会员卡]]></Title>"+
  // 		            		            		"<Description><![CDATA["+Commons.vregestCont+"]]></Description>"+
  // 		            		            		"<PicUrl><![CDATA["+Commons.regestPic+"]]></PicUrl>"+
  // 		            		            		"<Url><![CDATA["+Commons.addCard+fromUsername+"]]></Url>"+
  // 		            		            		"</item>"+
  // 		            		            		"</Articles>"+
  // 		            		            		"</xml>" ;
  	                			  }else{
  // 		                			  textTpl="<xml>"+
  // 		            		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
  // 		            		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
  // 		            		            		"<CreateTime>123</CreateTime>"+
  // 		            		            		"<MsgType><![CDATA[news]]></MsgType>"+
  // 		            		            		"<ArticleCount>1</ArticleCount>"+
  // 		            		            		"<Articles>"+
  // 		            		            		"<item>"+
  // 		            		            		"<Title><![CDATA[会员信息]]></Title>"+
  // 		            		            		"<Description><![CDATA["+content.substring(content.indexOf("@")+1,content.length())+"]]></Description>"+
  // 		            		            		"<PicUrl><![CDATA["+Commons.cardPic+content.substring(0,content.indexOf("@"))+".png]]></PicUrl>"+
  // 		            		            		"<Url><![CDATA["+Commons.listCard+fromUsername+"]]></Url>"+
  // 		            		            		"</item>"+
  // 		            		            		"</Articles>"+
  // 		            		            		"</xml>" ;
  	                			  }
                  			  }else if(eventKey.equals("3")){
  // 	                			  if(content.equals("order")){
  // 	                				  content="您还没有点菜，请点击这里进行点菜";
  // 		                			  textTpl="<xml>"+
  // 		            		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
  // 		            		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
  // 		            		            		"<CreateTime>123</CreateTime>"+
  // 		            		            		"<MsgType><![CDATA[news]]></MsgType>"+
  // 		            		            		"<ArticleCount>1</ArticleCount>"+
  // 		            		            		"<Articles>"+
  // 		            		            		"<item>"+
  // 		            		            		"<Title><![CDATA[点菜]]></Title>"+
  // 		            		            		"<Description><![CDATA["+content+"]]></Description>"+
  // 		            		            		"<PicUrl><![CDATA["+Commons.ordrPic+"]]></PicUrl>"+
  // 		            		            		"<Url><![CDATA["+Commons.addPubitem+fromUsername+"]]></Url>"+
  // 		            		            		"</item>"+
  // 		            		            		"</Articles>"+
  // 		            		            		"</xml>";
  // 	                			  }else{
  // 		                			  textTpl="<xml>"+
  // 		            		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
  // 		            		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
  // 		            		            		"<CreateTime>123</CreateTime>"+
  // 		            		            		"<MsgType><![CDATA[news]]></MsgType>"+
  // 		            		            		"<ArticleCount>1</ArticleCount>"+
  // 		            		            		"<Articles>"+
  // 		            		            		"<item>"+
  // 		            		            		"<Title><![CDATA[我的菜单]]></Title>"+
  // 		            		            		"<Description><![CDATA["+content+"]]></Description>"+
  // 		            		            		"<PicUrl><![CDATA["+Commons.ordrPic+"]]></PicUrl>"+
  // 		            		            		"<Url><![CDATA["+Commons.listPubitem+fromUsername+"]]></Url>"+
  // 		            		            		"</item>"+
  // 		            		            		"</Articles>"+
  // 		            		            		"</xml>" ;
  // 	                			  }
                  			  }
                  		  }catch(Exception e){
                  			  e.printStackTrace();
                  		  }
                  		*/
                  	  }
              	  }else if(event.equals("LOCATION")){
              		  /* */
              		  	LocationMapper locationMapper = (LocationMapper)springContext.getBean("locationMapperImpl");
  						String createTime = root.elementTextTrim("CreateTime"); //消息创建时间 （整型）
  						String latitude = root.elementTextTrim("Latitude"); //地理位置纬度
  						String longitude = root.elementTextTrim("Longitude"); //地理位置经度
  						locationMapper.saveOrUpdate(fromUsername, latitude, longitude, createTime);
  					
  					}
  				}
  				this.print(textTpl);
  			} else {
  				this.print("");
  			}
  		}

  		//向请求端发送返回数据  
  		public void print(String content) {
  			try {
  				final_response.getWriter().print(content);
  				final_response.getWriter().flush();
  				final_response.getWriter().close();
  			} catch (Exception e) {
				e.printStackTrace();
  			}
  		}

  		//从输入流读取post参数用户发送的消息  
  		public String readStreamParameter(ServletInputStream in) {
  			StringBuilder buffer = new StringBuilder();
  			BufferedReader reader = null;
  			try {
  				reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
  				String line = null;
  				while ((line = reader.readLine()) != null) {
  					buffer.append(line);
  				}
  			} catch (Exception e) {
  				e.printStackTrace();
  			} finally {
  				if (null != reader) {
  					try {
  						reader.close();
  					} catch (IOException e) {
  						e.printStackTrace();
  					}
  				}
  			}
  			return buffer.toString();
  		}
  	}
  %>
<%
    WeiXinHandler handler=new WeiXinHandler();
    handler.valid();
%>