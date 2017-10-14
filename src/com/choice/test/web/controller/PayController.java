
package com.choice.test.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.constants.PubitemConstants;
import com.choice.test.utils.Commons;
import com.wxap.util.PayUtil;

/**
 * 菜品
 * @author 孙胜彬
 */
@Controller
@RequestMapping(value="view/wxpay")
public class PayController {
	
	/**
	 * 组装微信支付需要相关的信息
	 * @Description:
	 * @Title:jspay
	 * @Author:dwh
	 * @Date:2014-12-19 下午8:01:02 	
	 * @param modelMap
	 * @param request 
	 * @param response 
	 * @param paymoney 支付价额
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="jspay")
	@ResponseBody
	public Object jspay(ModelMap modelMap,HttpServletRequest request,  
            HttpServletResponse response,String paymoney)throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
//		System.out.println(paymoney);
		paymoney = Integer.parseInt(paymoney)*100+"";
		TreeMap<String, String> goodsInfo = new TreeMap<String, String>();
		goodsInfo.put("body", "");
		goodsInfo.put("total_fee", paymoney); //商品总金额,以分为单位
		String packegeValue = PayUtil.packageValueInfo(request, response, goodsInfo);
		String sign = PayUtil.getSign();
		String timestamp = PayUtil.getTimestamp();
		String nonce = PayUtil.getNonceStr();
		
//		System.out.println("packagevalue:"+packegeValue);
//		System.out.println("sign:"+sign);
//		System.out.println("timestamp:"+timestamp);
//		System.out.println("noncestr:"+nonce);
		map.put("APPID", Commons.appId);
		map.put("PACKEGEVALUE", packegeValue);
		map.put("SIGN", sign);
		map.put("TIMESTAMP",timestamp);
		map.put("NONCE",nonce);
		return map;
//		return new ModelAndView(PubitemConstants.JSPAY,modelMap);
	}
	
	
	
	
	/**
	 * 微信支付测试
	 * @Description:
	 * @Title:testjsapi
	 * @Author:dwh
	 * @Date:2014-12-16 上午9:41:59 	
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param paymoney
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="testjsapi")
	public ModelAndView testjsapi(ModelMap modelMap,HttpServletRequest request,  
            HttpServletResponse response,String paymoney)throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println(paymoney);
		if(null==paymoney||("").equals(paymoney)){
			paymoney="1";
		}
//		paymoney = Integer.parseInt(paymoney)*100+"";
//		paymoney="1";
		TreeMap<String, String> goodsInfo = new TreeMap<String, String>();
		goodsInfo.put("body", "assss");
		goodsInfo.put("total_fee", paymoney); //商品总金额,以分为单位
		String packegeValue = PayUtil.packageValueInfo(request, response, goodsInfo);
		String sign = PayUtil.getSign();
		String timestamp = PayUtil.getTimestamp();
		String nonce = PayUtil.getNonceStr();
		System.out.println("packagevalue:"+packegeValue);
		System.out.println("sign:"+sign);
		System.out.println("timestamp:"+timestamp);
		System.out.println("noncestr:"+nonce);
		map.put("APPID", Commons.appId);
		map.put("PACKEGEVALUE", packegeValue);
		map.put("SIGN", sign);
		map.put("TIMESTAMP",timestamp);
		map.put("NONCE",nonce);
//		return map;
		modelMap.put("APPID", Commons.appId);
		modelMap.put("PACKEGEVALUE", packegeValue);
		modelMap.put("SIGN", sign);
		modelMap.put("TIMESTAMP",timestamp);
		modelMap.put("NONCE",nonce);
		modelMap.put("SIGNTYPE","SHA1");
		return new ModelAndView(PubitemConstants.TESTJSAPI,modelMap);
	}
	
}
