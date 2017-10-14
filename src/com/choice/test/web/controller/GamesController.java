package com.choice.test.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.choice.test.constants.CardConstants;
import com.choice.test.constants.GamesConstants;
import com.choice.test.domain.Card;
import com.choice.test.domain.Games;
import com.choice.test.domain.Prize;
import com.choice.test.service.CardSearch;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;

/**
 * 抽奖
 * @author lwj
 */
@Controller
@RequestMapping(value="games")
public class GamesController {
	private Integer baseNo = 1000;//基数
	/**
	 * 进入刮刮卡
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="toScratch")
	public ModelAndView toScratch(ModelMap modelMap, String code,String state){
		try {
			//查询微信号
			String openid = CodeHelper.getOpenID(Commons.appId, Commons.secret, code);
			//返回微信号
			modelMap.put("openid", openid);
			//查询是否注册会员
			List<Card> listCard=CardSearch.listCard(openid);
			if(null ==listCard || listCard.size()==0){
				//未注册会员跳转到绑定会员页面
				//返回是否会员标识
				modelMap.put("cardFlag", true);
				//return new ModelAndView(GamesConstants.SCRATCH_HAPPY,modelMap);
			}
			//查询当前抽奖游戏
			
			Games games = new Games();
			games.setCount(5);
			games.setRemark("欢迎刮刮卡，刮刮中大奖！");
			
			//查询当前抽奖的奖品信息及概率
			List<Prize> prizeList = new ArrayList<Prize>();
			Prize p1 = new Prize();
			p1.setCount(2);
			p1.setLevel("一等奖");
			p1.setName("1000积分");
			p1.setProbability(0.01D);
			
			Prize p2 = new Prize();
			p2.setCount(20);
			p2.setLevel("二等奖");
			p2.setName("600积分");
			p2.setProbability(0.1D);
			
			Prize p3 = new Prize();
			p3.setCount(30);
			p3.setLevel("三等奖");
			p3.setName("200积分");
			p3.setProbability(0.3D);
			
			prizeList.add(p1);prizeList.add(p2);prizeList.add(p3);
			
			Prize winning = isLucky(prizeList);
			
			modelMap.put("games", games);
			modelMap.put("prizeList", prizeList);
			modelMap.put("prize", winning);
			//已注册会员跳转到刮奖页面
			return new ModelAndView(GamesConstants.SCRATCH_HAPPY,modelMap);
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 抽奖程序
	 * @param prizeList
	 * @return
	 */
	private Prize isLucky(List<Prize> prizeList){
		Random random = new Random();
		Integer flagNo = random.nextInt(baseNo+1);
		Prize winning = null;
		pross:for(Prize prize:prizeList){
			Double probability = prize.getProbability();
			List<Integer> noList = getNoList(probability);
			if(noList.contains(flagNo)){
				winning = prize;
				break pross;
			}
		}
		//打印中奖信息
		if(winning!=null){
			System.out.println("中奖号码："+flagNo);
			System.out.println("中奖奖品："+winning.getLevel());
		}else{
			System.out.println("随机码："+flagNo+",未中奖");
		}
		return winning;
	}
	
	/**
	 * 计算随机数
	 * @param proba
	 * @return
	 */
	private List<Integer> getNoList(Double proba){
		List<Integer> noList = new ArrayList<Integer>();
		Random random = new Random();
		Integer count = (int) (baseNo*proba);
		for(int i =0;i<count;i++){
			Integer flagNo = random.nextInt(baseNo+1);
			//确保不重复
			if(noList.contains(flagNo)){
				flagNo = random.nextInt(baseNo+1);
			}
			noList.add(flagNo);
		}
		System.out.println("当前概率："+proba);
		System.out.println("应产生随机数："+noList.size());
		System.out.println("实际产生随机数："+noList.size());
		System.out.println("随机数分别为：");
		for(Integer j: noList){
			System.out.println(j);
		}
		return noList;
	}
}
