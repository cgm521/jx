package com.choice.wechat.web.controller.homeIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Card;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.Commons;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.homeIndex.HomeIndexConstants;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.util.CallWebService;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.WeChatUtil;
/**
 * 首页
 * @author ZGL
 *
 */
@Controller
@RequestMapping(value="homeIndex")
public class HomeIndexController {

	/**
	 *  跳转到首页（我的）
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="myPage")
	public ModelAndView homePage(ModelMap modelMap, HttpServletRequest request,String code) throws Exception{

		List<Map<String,Object>> listCoupon = new ArrayList<Map<String,Object>>();
		String pk_group = request.getParameter("pk_group");
		//根据pk_group，获取配置信息
		Company company = WeChatUtil.getCompanyInfo(pk_group);
		
		String appId = Commons.appId;
		String secret = Commons.secret;
		if(null != company) {
			if (null != company.getAppId() && !"".equals(company.getAppId())) {
				appId = company.getAppId();
			}
			if (null != company.getSecret() && !"".equals(company.getSecret())) {
				secret = company.getSecret();
			}
		}
		
		AccessToken token = WeChatUtil.getAccessToken(appId, secret);
		WeChatUser user = (WeChatUser) request.getSession().getAttribute("WeChatUser");
		if(user == null || user.getOpenid()==null || "".equals(user.getOpenid())){//返回按钮，已取得用户授权的情况
			//通过网页授权方式获取openID
			String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
			//获取用户基本信息
			user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
			if(StringUtils.hasText(user.getOpenid())){
				request.getSession().setAttribute("WeChatUser", user);
			}
		}
		
		StringBuffer sb = request.getRequestURL();
		String queryString = request.getQueryString();
		if(queryString != null && !"".equals(queryString)){
			sb.append("?");
			int index = queryString.indexOf("#");
			if(index < 0){
				sb.append(queryString);
			}else{
				sb.append(queryString.substring(0, index));
			}
		}
		//获取jsJDK需要的参数
		Map<String, String> signMap = Sign.sign(WeChatUtil.getJsapiTicket(appId, token.getToken()).getTicket(),sb.toString());
		//----------------------------------------查询会员信息----------------------------------------
		//查询微信绑定的会员实体卡
		/***20150625 如果Unionid不为空，则用他取值***/
		List<Card> listCardInfo = null;
		if(StringUtils.hasText(user.getUnionid())){
			listCardInfo = CardSearch.listCard(user.getUnionid());
		}else{
			listCardInfo = CardSearch.listCard(user.getOpenid());
		}
		if (listCardInfo != null && !listCardInfo.isEmpty()
				&& listCardInfo.size() > 0) {
			Card card = listCardInfo.get(0);
			// 调用接口获取当前会员的详细信息
			// -1未查到-2查询异常
			// 卡ID @卡号@卡姓名@手机号@有效期@卡状态@卡类别@卡余额@卡积分余额@本金@赠送@手续费@退卡金额@电子券列表
			// 电子券列表组成如下：
			// 电子券列表：券唯一编码1,电子券code1,券名称1,面额1,有效期1,折扣比率1,折扣金额1#券唯一编码2,电子券code2,券名称2,面额2,有效期2,折扣比率2,折扣金额2...
			try {
				String memInfo = CallWebService.httpCallWebServiceGet(
						"CRMWebService/queryCardByCardNo",
						"queryCardNo=" + card.getCardNo());
				String[] memList = memInfo.split("@");
				modelMap.put("memList", memList);// 用户信息
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			modelMap.put("listCoupon", listCoupon);// 用户可用的电子券
		}else{
			String[] memList = new String[14];
			memList[2] = user.getNickname();
			memList[7] = "0.0";
			modelMap.put("memList", memList);//用户信息
		}
		
		modelMap.put("headimg", user.getHeadimgurl());
		modelMap.put("signMap", signMap);
		modelMap.put("appId", appId);
		modelMap.put("company", company);
		modelMap.put("code", code);
		modelMap.put("openid", user.getOpenid());
		modelMap.put("pk_group", pk_group);
		modelMap.put("vcompanyname", Commons.vcompanyname);
		modelMap.put("vcompanytel", Commons.vcompanytel);
		return new ModelAndView(HomeIndexConstants.MYPAGE,modelMap);
	}
}
