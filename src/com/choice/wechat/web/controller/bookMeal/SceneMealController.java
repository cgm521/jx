package com.choice.wechat.web.controller.bookMeal;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.domain.Net_Orders;
import com.choice.test.utils.Commons;
import com.choice.wechat.common.AccessToken;
import com.choice.wechat.common.WeChatUser;
import com.choice.wechat.constants.bookMeal.BookMealConstants;
import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.bookDesk.StoreTable;
import com.choice.wechat.domain.bookMeal.FdItemType;
import com.choice.wechat.persistence.bookDesk.BookDeskMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.util.Sign;
import com.choice.wechat.util.WeChatUtil;

/**
 * 
 * @author
 * 扫描二维码点菜
 */
@Controller
@RequestMapping(value = "sceneMeal")
public class SceneMealController {
	@Autowired
	private BookMealMapper bookMealMapper;
	@Autowired
	private BookDeskMapper bookDeskMapper;

	/**
	 * 转发方法，防止url过长，不能产生二维码
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(value = "scanScene")
	public ModelAndView scanScene(ModelMap modelMap, String scene_id) throws ServletException, IOException{
		/*
		StringBuilder sb = new StringBuilder("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0bfdd531a019d59d&redirect_uri=https://kingss.pagekite.me/ChoiceWeChat/sceneMeal/gotoMenu.do?");
		sb.append("scene_id=").append(scene_id);
		sb.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
		return sb.toString();
		*/
		modelMap.put("scene_id", scene_id);
		return new ModelAndView(BookMealConstants.SCANSCENE, modelMap);
	}
	
	/**
	 * 进入菜单页面
	 * @param modelMap
	 * @param request
	 * @param code 微信权限参数
	 * @param state 微信权限参数
	 * @return
	 */
	@RequestMapping(value = "gotoMenu")
	public ModelAndView gotoMenu(ModelMap modelMap, HttpServletRequest request, String code, String state) {
		try{
			String scene_id = request.getParameter("scene_id");
			StoreTable table = null;
			if(scene_id.indexOf("qrscene_") >= 0) {
				scene_id = scene_id.replace("qrscene_", "");
			}
			List<StoreTable> tableList = bookDeskMapper.findRestTbl(scene_id);
			if(null != tableList && tableList.size() > 0){
				table = tableList.get(0);
			}

			String pk_group = table.getPk_group();
			String openid = request.getParameter("openid");
			if(null == openid || "".equals(openid)) {
				// 根据pk_group，获取配置信息
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

				// 通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				openid = oauth2[0];
			}

			pk_group = "";
			List<FdItemType> listItemType = bookMealMapper.getFdItemTypeList(pk_group, table.getFirmid(), null, null);
			modelMap.put("listItemType", listItemType);

			int count = bookMealMapper.hasPackage(pk_group, table.getFirmid());

			if(count == 0){//判断是否有套餐
				modelMap.put("count", "");
			}else{
				modelMap.put("count", "精美套餐");
			}

			Net_Orders orders = new Net_Orders();
			orders.setPk_group(pk_group);
			orders.setFirmid(table.getFirmid());
			orders.setOpenid(openid);
			orders.setTables(table.getTbl());

			// 门店列表
			List<Firm> listFirm = bookMealMapper.getFirmList(pk_group, null, table.getFirmid());
			if(listFirm != null && !listFirm.isEmpty()) {
				modelMap.put("firm", listFirm.get(0));
			}

			modelMap.put("openid", openid);
			modelMap.put("pk_group", pk_group);
			modelMap.put("orders", orders);
			return new ModelAndView(BookMealConstants.LISTMENU, modelMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookMealConstants.FAILEDPAGE, modelMap);
	}
	/**
	 * 打开扫码下单页面获取台位号并列出已经保存的订单
	 * @param modelMap
	 * @param request
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "openScanSceneForUpdateOrder")
	public ModelAndView openScanSceneForUpdateOrder(HttpSession session,ModelMap modelMap, HttpServletRequest request, String state,String pk_group,String code) {
		try {
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
			
			modelMap.put("appId", appId);
	
			AccessToken token = WeChatUtil.getAccessToken(appId, secret);
			
			WeChatUser user = (WeChatUser) session.getAttribute("WeChatUser");
			if(user == null){//返回按钮，已取得用户授权的情况
				//通过网页授权方式获取openID
				String oauth2[] = WeChatUtil.getOAuth2(appId, secret, code);
				//获取用户基本信息
				user = WeChatUtil.getWeChatUser(oauth2[0],token.getToken());
				if(StringUtils.hasText(user.getOpenid())){
					session.setAttribute("WeChatUser", user);
				}
			}
			
			/*StringBuffer sb = request.getRequestURL();
			
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

			Map<String, String> signMap = Sign.sign(WeChatUtil.getJsapiTicket(appId, token.getToken()).getTicket(),sb.toString());
			modelMap.put("signMap", signMap);*/
			
			modelMap.put("openid", user.getOpenid());
			modelMap.put("pk_group", pk_group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(BookMealConstants.SCANTABLESCENE, modelMap);
	}
	/**
	 * 扫码下单后台方法
	 * @param modelMap
	 * @param request
	 * @param code 微信权限参数
	 * @param state 微信权限参数
	 * @return
	 */
	@RequestMapping(value = "scanSceneUpdateOrder")
	public void scanSceneUpdateOrder(ModelMap modelMap, HttpServletRequest request, String code, String state) {
		try{
			String scene_id = request.getParameter("scene_id");
			System.out.println("=========================="+scene_id);
		}catch (Exception e) {
			e.printStackTrace();
		}
//		return new ModelAndView(BookMealConstants.FAILEDPAGE, modelMap);
	}
}
