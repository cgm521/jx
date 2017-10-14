package com.choice.wechat.util.tenpay;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.choice.test.utils.Commons;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.WeChatUtil;
import com.wxap.util.MD5Util;
import com.wxap.util.XMLUtil;

/**
 * 支付成功后订单查询
 * @author Sunlight
 *
 */
public class WXOrderQuery {
	private String partnerKey;
	private String appid;
	private String mch_id;
	private String transaction_id;
	private String out_trade_no;
	private String nonce_str;
	private String sign;
	private String partner;
	
	//请求订单查询接口

	@SuppressWarnings({ "unchecked" })
	public Map<String, String> reqOrderquery() throws Exception{
		
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/orderquery");
		String xml = getPackage();//构建查询参数xml格式
		LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付成功后查询支付订单参数===WXOrderQuery.class"
				+ new Throwable().getStackTrace()[0].getLineNumber()+"行==" + xml 
				+ System.getProperty("line.separator"));
		StringEntity entity;
		Map<String, String> map = null;
		try {
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);

			HttpResponse httpResponse;

			// post请求
			httpResponse = closeableHttpClient.execute(httpPost);

			// getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity != null) {

				// 打印响应内容
				String result = EntityUtils.toString(httpEntity, "UTF-8");

				// 过滤
				result = result.replaceAll("<![CDATA[|]]>", "");
				LogUtil.writeToTxt(LogUtil.TENPAY, "==微信支付成功后查询支付订单返回的数据===WXOrderQuery.class"+new Throwable().getStackTrace()[0].getLineNumber()+"行=="+result+System.getProperty("line.separator"));
				map = XMLUtil.doXMLParse(result);
			      
				System.out.println("支付金额："+map.get("total_fee"));
				System.out.println(result);
			}

			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public String getPackage() throws Exception {
		// 如果商户号和密钥为空，从配置文件读取
		if(!StringUtils.hasText(partner)) {
			partner = Commons.partner;
		}
		if(!StringUtils.hasText(partnerKey)) {
			partnerKey = Commons.partner_key;
		}
		
		// 是否服务商模式
		String tenPaySeriveFlag = Commons.getConfig().getProperty("tenPaySeriveFlag");
		
		TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
		if ("0".equals(tenPaySeriveFlag)) {
			// 非服务商模式
			treeMap.put("appid", Commons.appId);
			treeMap.put("mch_id", partner);
		} else {
			// 服务商模式
			treeMap.put("appid", "wx35f67221fe52ad3c");
			treeMap.put("mch_id", "1271747601");
			treeMap.put("sub_appid", Commons.appId);
			treeMap.put("sub_mch_id", partner);
		}
		treeMap.put("transaction_id", this.transaction_id);
		treeMap.put("nonce_str", this.nonce_str);
		treeMap.put("out_trade_no", this.out_trade_no);
		
		StringBuilder sb = new StringBuilder();
		sb.append(WeChatUtil.orderParams(treeMap));//将map内的参数按ASCII 码 从小到大排序（字典序）
		sb.append("&key=" + partnerKey);
		sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		treeMap.put("sign", sign);
		
		//构建xml文件参数
		String xml = WeChatUtil.getRequestXml(treeMap);
		return xml;
	}
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mchId) {
		mch_id = mchId;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transactionId) {
		transaction_id = transactionId;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String outTradeNo) {
		out_trade_no = outTradeNo;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonceStr) {
		nonce_str = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPartnerKey() {
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}
}
