package com.choice.wechat.service.game;

import com.choice.test.domain.Card;

public interface IGameService {
	/**
	 * 
	 * @param openid
	 * @param clientID
	 * @param gameType
	 * 赠送奖品
	 */
	public String giftAction(String openid,String clientID,String gameType,String snCode,String url,String pk_group);
	
	/**
	 * 
	 * @param openid
	 * @param clientID
	 * @param url
	 * @param pk_group
	 * @return
	 * 发送关键字送券
	 */
	public String keywordAction(String vcode,String openid,String clientID,String url,String pk_group);
	/**
	 * @param drawCount 
	 * @param openid 
	 * @param card 
	 * @param url 
	 * @param pk_group 
	 * 我要领券活动
	 */
	public String getCouponAction(Card card, String openid,String url, String pk_group);
}
