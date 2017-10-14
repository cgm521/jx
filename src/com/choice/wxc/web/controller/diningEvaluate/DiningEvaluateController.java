package com.choice.wxc.web.controller.diningEvaluate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.choice.wxc.constants.DiningEvaluateConstants;
import com.choice.wxc.domain.diningEvaluate.NetOrderDtl;
import com.choice.wxc.domain.diningEvaluate.OlpEvaluate;
import com.choice.wxc.persistence.diningEvaluate.DiningEvaluateMapper;

/**
 * 就餐评价
 */
@Controller
@RequestMapping(value = "evaluate")
public class DiningEvaluateController {
	@Autowired
	private DiningEvaluateMapper evaluateMapper;
	
	@RequestMapping(value = "toEvaluate")
	public ModelAndView toEvaluatePage(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		String ordersId = request.getParameter("ordersId");
		NetOrderDtl dtl = new NetOrderDtl();
		dtl.setOrdersid(ordersId);
		
		// 获取订单评价主表数据
		OlpEvaluate eva = new OlpEvaluate();
		eva.setOrdersId(ordersId);
		List<OlpEvaluate> evaList = evaluateMapper.getListEvaluate(eva);
		if(null != evaList && !evaList.isEmpty()) {
			// 已评价
			modelMap.put("hasEvaluated", "true");
			modelMap.put("evaInfo", evaList.get(0));
		} else {
			// 尚未评价
			modelMap.put("hasEvaluated", "false");
			OlpEvaluate evaInfo = new OlpEvaluate();
			// 默认五星评价
			evaInfo.setEntiretyPoint("5");
			evaInfo.setEnvPoint("5");
			evaInfo.setServicePoint("5");
			evaInfo.setTastePoint("5");
			
			modelMap.put("evaInfo", evaInfo);
		}
		
		// 订单详情
		List<NetOrderDtl> orderDtl = evaluateMapper.getListOrderDtl(dtl);
		modelMap.put("orderDtl", orderDtl);
		modelMap.put("ordersId", ordersId);
		
		return new ModelAndView(DiningEvaluateConstants.EVALUATE_PAGE);
	}
	
	/**
	 * 保存评价信息
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param evaluateInfo
	 * @return
	 */
	@RequestMapping(value = "saveEvaluate")
	@ResponseBody
	public String saveEvaluate(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response, OlpEvaluate evaluateInfo) {
		int result = evaluateMapper.saveEvaluateInfo(evaluateInfo);
		
		return result + "";
	}
	
	@RequestMapping(value = "takeOutEvaluate")
	public ModelAndView takeOutEvaluate(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		String ordersId = request.getParameter("ordersId");
		
		// 获取订单评价主表数据
		OlpEvaluate eva = new OlpEvaluate();
		eva.setOrdersId(ordersId);
		List<OlpEvaluate> evaList = evaluateMapper.getListEvaluate(eva);
		if(null != evaList && !evaList.isEmpty()) {
			// 已评价
			modelMap.put("hasEvaluated", "true");
			modelMap.put("evaInfo", evaList.get(0));
		} else {
			// 尚未评价
			modelMap.put("hasEvaluated", "false");
			OlpEvaluate evaInfo = new OlpEvaluate();
			// 默认五星评价
			evaInfo.setEntiretyPoint("5");
			
			modelMap.put("evaInfo", evaInfo);
		}
		
		modelMap.put("ordersId", ordersId);
		
		return new ModelAndView(DiningEvaluateConstants.TAKEOUT_EVALUATE_PAGE);
	}
}