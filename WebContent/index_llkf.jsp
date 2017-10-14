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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>  
<%
  	//WeiXinHandler为内部类不能使用非final类型的对象  
      final String TOKEN="mytoken";  
      final HttpServletRequest final_request=request;   
      final HttpServletResponse final_response=response;
  %>  
<%
  	class WeiXinHandler{  
      public void valid(){  
          String echostr=final_request.getParameter("echostr");  
          if(null==echostr||echostr.isEmpty()){  
              responseMsg();  
          }else{
              this.print("error");                                                                                          
          }  
      }  
      //自动回复内容  
      public void responseMsg(){  
          String postStr=null;  
          try{  
              postStr=this.readStreamParameter(final_request.getInputStream());  
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
              String time = new Date().getTime()+"";
              
              String content="";
              String textTpl = "";
              if("text".equals(msgType)){
  	            try{
  	            	if(null!=keyword&&!keyword.equals("")){
  	            		if(keyword.equals("123")){
//   	            			String photoTpl="<xml>"+
// 					  	            			"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
// 					  	            			"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
// 					  	            			"<CreateTime>12345</CreateTime>"+
// 					  	            			"<MsgType><![CDATA[video]]></MsgType>"+
// 					  	            			"<Video>"+
// 					  	            			"<MediaId><![CDATA[8nVy0YZQSocJ_le6Em2vyV0nCm1c1YOdQ9tlT200_fLMZJ85g802q-cF7DA9syly]]></MediaId>"+
// 					  	            			"<Title><![CDATA[测试视频]]></Title>"+
// 					  	            			"<Description><![CDATA[视频播放]]></Description>"+
// 					  	            			"</Video>"+
// 				  	            			"</xml>";
//   	            			this.print(photoTpl);
  	            		}else{
  	            			content= Client_CRM.getCard(fromUsername,keyword);
  	            		}
  	            	}else{
  	            		content="您还不是会员，请注册";
  	            	}
//   	            	textTpl="<xml>"+
//   		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
//   		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
//   		            		"<CreateTime>123</CreateTime>"+
//   		            		"<MsgType><![CDATA[news]]></MsgType>"+
//   		            		"<ArticleCount>1</ArticleCount>"+
//   		            		"<Articles>"+
//   		            		"<item>"+
//   		            		"<Title><![CDATA[会员卡]]></Title>"+
//   		            		"<Description><![CDATA["+content+"]]></Description>"+
//   		            		"<PicUrl><![CDATA[http://image226-c.poco.cn/mypoco/myphoto/20140120/16/1743238092014012016294701.jpg]]></PicUrl>"+
//   		            		"<Url><![CDATA[http://www.choicesoft.com.cn/]]></Url>"+
//   		            		"</item>"+
//   		            		"</Articles>"+
//   		            		"</xml>";
  	            	textTpl = "<xml>"+
              			"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
              			"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
              			"<CreateTime>111</CreateTime>"+
              			"<MsgType><![CDATA[text]]></MsgType>"+
              			"<Content><![CDATA["+content.substring(content.indexOf("@")+1,content.length())+"]]></Content>"+
              			"</xml>";
  	            }catch(Exception e){
  	            	e.printStackTrace();
  	            }
              }else if("event".equals(msgType)){
            	  String event=root.elementTextTrim("Event");
            	  if(event.equals("subscribe")){
            		String texteventKey=root.elementTextTrim("EventKey");
            		if(texteventKey !=null && !texteventKey.equals("")){
	             		textTpl="<xml>"+
	  		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
	  		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
	  		            		"<CreateTime>123</CreateTime>"+
	  		            		"<MsgType><![CDATA[news]]></MsgType>"+
	  		            		"<ArticleCount>1</ArticleCount>"+
	  		            		"<Articles>"+
	  		            		"<item>"+
	  		            		"<Title><![CDATA["+Commons.vtitle+"]]></Title>"+
	  		            		"<Description><![CDATA[您已经进入"+PubitemSearch.getStoreTable(texteventKey.substring(texteventKey.indexOf("_")+1,texteventKey.length()))+"号台位，请点击我点菜]]></Description>"+
	  		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
	  		            		"<Url><![CDATA["+Commons.vtakeOrdrurl+"?scene_id="+texteventKey+"&openid="+fromUsername+"]]></Url>"+
	  		            		"</item>"+
	  		            		"</Articles>"+
	  		            		"</xml>" ;
            		}else{
            			textTpl="<xml>"+
	  		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
	  		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
	  		            		"<CreateTime>123</CreateTime>"+
	  		            		"<MsgType><![CDATA[news]]></MsgType>"+
	  		            		"<ArticleCount>1</ArticleCount>"+
	  		            		"<Articles>"+
	  		            		"<item>"+
	  		            		"<Title><![CDATA["+Commons.vtitle+"]]></Title>"+
	  		            		"<Description><![CDATA["+Commons.vcontent+"]]></Description>"+
	  		            		"<PicUrl><![CDATA["+Commons.vcomPic+"]]></PicUrl>"+
	  		            		"<Url><![CDATA["+Commons.vcomUrl+"]]></Url>"+
	  		            		"</item>"+
	  		            		"</Articles>"+
	  		            		"</xml>" ;
            		}
            	  }else if(event.equals("SCAN")){
            		  String texteventKey=root.elementTextTrim("EventKey");
            		  textTpl="<xml>"+
    		            		"<ToUserName><![CDATA["+fromUsername+"]]></ToUserName>"+
    		            		"<FromUserName><![CDATA["+toUsername+"]]></FromUserName>"+
    		            		"<CreateTime>123</CreateTime>"+
    		            		"<MsgType><![CDATA[news]]></MsgType>"+
    		            		"<ArticleCount>1</ArticleCount>"+
    		            		"<Articles>"+
    		            		"<item>"+
    		            		"<Title><![CDATA["+Commons.vtitle+"]]></Title>"+
    		            		"<Description><![CDATA[您已经进入"+PubitemSearch.getStoreTable(texteventKey)+"号台位，请点击我点菜]]></Description>"+
    		            		"<PicUrl><![CDATA["+Commons.vtakeOrdr+"]]></PicUrl>"+
//     		            		"<Url><![CDATA["+Commons.vtakeOrdrurl+"?scene_id="+texteventKey+"&openid="+fromUsername+"]]></Url>"+
    		            		"<Url><![CDATA["+Commons.vwifi+"/url="+Commons.vtakeOrdrurl+"/url_param=scene_id="+texteventKey+"/openid="+fromUsername+"]]></Url>"+
    		            		"</item>"+
    		            		"</Articles>"+
    		            		"</xml>" ;
            	  }else if(event.equals("unsubscribe")){
            		  content="取消关注！";
            	  }else if(event.equals("CLICK")){
            		  String eventKey = root.elementTextTrim("EventKey");
            		  if(null!=eventKey&&!eventKey.equals("")){
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
                	  }
            	  }
              }
          	this.print(textTpl);
          }else {
              this.print("");  
          }  
      }  

      //向请求端发送返回数据  
      public void print(String content){  
          try{  
              final_response.getWriter().print(content);  
              final_response.getWriter().flush();  
              final_response.getWriter().close();  
          }catch(Exception e){  
              
          }  
      }  

      //从输入流读取post参数用户发送的消息  
      public String readStreamParameter(ServletInputStream in){  
          StringBuilder buffer = new StringBuilder();  
          BufferedReader reader=null;  
          try{  
              reader = new BufferedReader(new InputStreamReader(in));  
              String line=null;
              while((line = reader.readLine())!=null){  
                  buffer.append(line);  
              }
          }catch(Exception e){
              e.printStackTrace();
          }finally{
              if(null!=reader){
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