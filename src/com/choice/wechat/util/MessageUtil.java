package com.choice.wechat.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.choice.test.domain.Card;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.domain.cookie.Cookie;
import com.choice.wechat.domain.cookie.WeChatWall;
import com.choice.wechat.domain.reply.Article;
import com.choice.wechat.domain.reply.KeyWord;
import com.choice.wechat.domain.reply.NewsMessage;
import com.choice.wechat.domain.reply.TextMessage;
import com.choice.wechat.persistence.cookie.CookieMapper;
import com.choice.wechat.persistence.cookie.impl.CookieMapperImpl;
import com.choice.wechat.persistence.reply.KeyWordMapper;
import com.choice.wechat.service.game.IGameService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 * 
 */
@Component
public class MessageUtil {
	@Autowired
	private KeyWordMapper keyWordMapper;
	@Autowired
	private IGameService gameService;
	
	static CookieMapper cookieMapper = new CookieMapperImpl();
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";
	
	/**
	 * 扫描二维码触发事件Map,主键-openid， 值-0:扫码点餐; 1:扫码下单
	 */
	public static Map<String, String> scanEventMap = new HashMap<String, String>();

	public static Map<String, String> getScanEventMap() {
		return scanEventMap;
	}

	public static void setScanEventMap(Map<String, String> scanEventMap) {
		MessageUtil.scanEventMap = scanEventMap;
	}
	
	/**
	 * 用户信息，主键-openid
	 */
	public static Map<String, WeChatUser> UserMap = new HashMap<String, WeChatUser>();

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage 文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage 图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				public void startNode(String name, Class clazz) {
					super.startNode(name);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
	
	/**
	 * 
	 * @param toUsername 用户openid
	 * @param fromUsername 开发这微信号
	 * @param keyword
	 * @param msgType 消息类型 text|event
	 */
	public String replyMsg(String toUsername,String fromUsername,String keyword,String msgType){
		String replyContent = "";
		if(StringUtils.hasText(keyword)){
			String temp = keyword.replaceAll(".*([';]+|(--)+).*", " ");//过滤非法字符
			if(StringUtils.hasText(temp)){
				//查询是否已经进入微信墙
				Cookie cookie = new Cookie();
				cookie.setOpenid(toUsername);
				List<Cookie> listCookie = cookieMapper.findLoginInfoCookie(cookie);
				if(listCookie!=null && listCookie.size()>0){
					if("下墙".equals(keyword)){
						cookieMapper.deleteLoginInfoCookie(cookie);//退出微信墙
					}else{
						AccessToken assk = WeChatUtil.getAccessToken(Commons.appId, Commons.secret);
						try {
							if("image".equals(msgType)) {
								URL url = new URL(keyword);
								java.io.BufferedInputStream bis = new BufferedInputStream(url.openStream());
								byte[] bytes = new byte[100];
								keyword = CodeHelper.createUUID()+".jpg";//生成新的url
								OutputStream bos = new FileOutputStream(new File(Commons.wallImgPath+keyword));
								int len;
								while((len = bis.read(bytes)) > 0) {
									bos.write(bytes, 0, len);
								}
								bis.close();
								bos.flush();
								bos.close();
							}
							
							WeChatUser user = WeChatUtil.getWeChatUser(toUsername, assk.getToken());
							WeChatWall weChatWall = new WeChatWall();
							weChatWall.setOpenid(toUsername);
							weChatWall.setHeadimgurl(user.getHeadimgurl());
							weChatWall.setNickname(user.getNickname());
							weChatWall.setPk_store(listCookie.get(0).getCookie());
							weChatWall.setVcontent(keyword);
							weChatWall.setMsgType(msgType);
							cookieMapper.insertWeChatWall(weChatWall);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return "";
				}else{
					String actmword = Commons.getConfig().getProperty("actmword");
					if(StringUtils.hasText(actmword)){
						if(temp.startsWith(actmword)){//再处理消息（现 暂时只根据活动送券）
							String vcode = temp.replaceAll(".*([';]+|(--)+).*", "").replace(actmword, "").trim();//活动编码
							List<Card> listCard = CardSearch.listCard(toUsername);;
							
							if (listCard != null && listCard.size() > 0) {//已注册绑定的执行送券活动
								final String final_pk_actm = vcode;
								final String final_toUsername = toUsername;
								final String final_cardNo = listCard.get(0).getCardNo();
								Thread t = new Thread(){
									public void run(){
										gameService.keywordAction(final_pk_actm, final_toUsername, final_cardNo,Commons.notify_url, "");
									}
								};
								t.start();
								return "";
							}else{//还未注册绑定的发送必须先注册图文消息
								NewsMessage news = new NewsMessage();
								news.setArticleCount(1);
								news.setCreateTime(new Date().getTime());
								news.setFromUserName(fromUsername);
								news.setFuncFlag(0);
								news.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
								news.setToUserName(toUsername);
								
								List<Article> articles = new ArrayList<Article>();
								Article article = new Article();
								article.setDescription("您还未注册绑定微信号，请先绑定成为会员");
								article.setPicUrl(Commons.vregestPic);
								article.setTitle("请先注册会员");
								
								StringBuffer target_url = new StringBuffer(Commons.notify_url);
								target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
								target_url = target_url.delete(target_url.lastIndexOf("/"), target_url.length());
								target_url.append("/myCard/cardInfo.do?openid=").append(toUsername)
									.append("&pk_group=").append("");
								article.setUrl(target_url.toString());
								articles.add(article);
								
								news.setArticles(articles);
								replyContent = MessageUtil.newsMessageToXml(news);
								return replyContent;
							}
						}
					}
//					if("text".equals(msgType)){
						List<KeyWord> list =  keyWordMapper.findMsgFormKeyWord(Commons.appId,temp,msgType);
						if(list != null && list.size() > 0){
							KeyWord msgContent = list.get(0);
							if("0".equals(msgContent.getReplyType())){//文本消息
								TextMessage textMessage = new TextMessage();
								textMessage.setContent(msgContent.getContent());
								textMessage.setToUserName(toUsername);
								textMessage.setFromUserName(fromUsername);
								textMessage.setCreateTime(new Date().getTime());
								textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
								textMessage.setFuncFlag(0);
								replyContent = MessageUtil.textMessageToXml(textMessage);
							}else if("1".equals(msgContent.getReplyType())){//图文消息
								NewsMessage news = new NewsMessage();
								news.setArticleCount(1);
								news.setCreateTime(new Date().getTime());
								news.setFromUserName(fromUsername);
								news.setFuncFlag(0);
								news.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
								news.setToUserName(toUsername);
								
								StringBuffer bohUrl = new StringBuffer(Commons.getConfig().getProperty("BOHUrl"));
								bohUrl = bohUrl.delete(bohUrl.lastIndexOf("/"), bohUrl.length());
								String picUrl = bohUrl.toString() + msgContent.getPicUrl();
								
								List<Article> articles = new ArrayList<Article>();
								Article article = new Article();
								article.setDescription(msgContent.getDescription());
								article.setPicUrl(Commons.vpiceure+msgContent.getPicUrl());
								article.setTitle(msgContent.getTitle());
								article.setUrl(msgContent.getUrl());
								articles.add(article);
								
								news.setArticles(articles);
								replyContent = MessageUtil.newsMessageToXml(news);
							}else if("2".equals(msgContent.getReplyType())){//图片消息
								System.out.println("图片类型的消息要发送了");
								replyContent = "<xml>" + 
								"<ToUserName><![CDATA["+toUsername+"]]></ToUserName>" + 
								"<FromUserName><![CDATA["+fromUsername+"]]></FromUserName>" + 
								"<CreateTime>"+new Date().getTime()+"</CreateTime>" + 
								"<MsgType><![CDATA[image]]></MsgType>" + 
								"<Image>" + 
								"<MediaId><![CDATA["+msgContent.getMedia_id()+"]]></MediaId>" + 
								"</Image>" + 
								"</xml>";
							}
						}
//					}
				}
			}
		}
		return replyContent;
	}
	
	/**
	 * 设置WeChatUser
	 * @param openid
	 * @param user
	 */
	public static void setUser(String openid, WeChatUser user) {
		UserMap.put(openid, user);
	}
	
	/**
	 * 根据openid获取WeChatUser
	 * @param openid
	 * @return
	 */
	public static WeChatUser getUser(String openid) {
		if(UserMap.containsKey(openid)) {
			return UserMap.get(openid);
		}
		
		return null;
	}
}