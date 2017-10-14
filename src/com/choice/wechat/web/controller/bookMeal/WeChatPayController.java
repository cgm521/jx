package com.choice.wechat.web.controller.bookMeal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Card;
import com.choice.test.domain.Net_Orders;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.weChatPay.WxPayConstants;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.service.WeChatPay.IWxPayService;
import com.choice.wechat.util.CallWebService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;
import com.choice.wechat.util.tenpay.MdlTemplate;
import com.choice.wechat.util.tenpay.WXOrderQuery;
import com.wxap.util.MD5SignUtil;
import com.wxap.util.PayUtil;
import com.wxap.util.Sha1Util;
import com.wxap.util.XMLUtil;

/**
 * 微信支付
 * @author 王恒军
 * @Date 2015-04-02 14:46:51
 */
@Controller
@RequestMapping(value="weChatPay")
public class WeChatPayController {
	/**
	 * 跳转到买单
	 * @param modelMap
	 * @param request
	 * @author 王恒军
	 * @Date 2015年4月1日 14:51:39
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getPackageInfo")
	@ResponseBody
	public Object getPackageInfo(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String total_fee = request.getParameter("total_fee");
			// 微信支付
			TreeMap<String, String> goodsInfo = new TreeMap<String, String>();
			goodsInfo.put("body", "订单支付");
			goodsInfo.put("total_fee", total_fee); //商品总金额,以分为单位
			String packegeValue = PayUtil.packageValueInfo(request, response,
					goodsInfo);
			String sign = PayUtil.getSign();
			String timestamp = PayUtil.getTimestamp();
			String nonce = PayUtil.getNonceStr();
			
			JSONObject result = new JSONObject();
			result.put("APPID", Commons.appId);
			result.put("PACKEGEVALUE", packegeValue);
			result.put("SIGN", sign);
			result.put("TIMESTAMP", timestamp);
			result.put("NONCE", nonce);
			result.put("SIGNTYPE", "SHA1");
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
}
