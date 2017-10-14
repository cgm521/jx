package com.choice.wechat.persistence.game;

import java.util.List;

import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.domain.game.DrawCount;
import com.choice.wechat.domain.game.Gift;
import com.choice.wechat.domain.game.SN;

public interface GameMapper {
	/**
	 * 
	 * @param openid
	 * @return
	 * 获取抽奖次数
	 */
	public List<DrawCount> getDrawCount(String openid, String gameType);
	
	/**
	 * 
	 * @param openid
	 * 更新抽奖次数
	 */
	public void updateDrawCount(String openid, String gameType);
	
	/**
	 * 
	 * @param openid
	 * @param gameType
	 * 插入抽奖次数记录
	 */
	public void insertDrawCount(String openid, String gameType);
	
	/**
	 * 
	 * @param sn
	 * 插入中奖记录
	 */
	public void insertSN(SN sn);
	
	/**
	 * 
	 * @param SN
	 * @param openid
	 * @param gameType
	 * @return
	 * 获取中奖记录
	 */
	public List<SN> getSN(String SN, String openid, String gameType);
	
	/**
	 * 
	 * @param SN
	 * @param draw
	 * 更新是否已领奖
	 */
	public void updateSN(String SN,int draw);
	
	/**
	 * 
	 * @param gameType
	 * @param lucky 奖级
	 * @return
	 * 查询奖品列表
	 */
	public List<Gift> getGift(String gameType,int lucky);
	
	/**
	 * 
	 * @param gameType
	 * @param lucky
	 * @return
	 * 更新奖品剩余可用数量
	 */
	public int updateGiftQuantity(String gameType,int lucky);
	
	/**
	 * 
	 * @param pk_actm
	 * @return
	 * 获取活动赠送券的明细
	 */
	public List<Actm> getActmDetailVoucher(String pk_actm);
	
	/**
	 * 
	 * @param pk_actm
	 * @return
	 * 通过编码获取活动赠送券的明细
	 */
	public List<Actm> getActmDetailVoucherFromVcode(String vcode);
	
	/**
	 * 
	 * @param pk_actm
	 * @return
	 * 获取活动赠送积分的明细
	 */
	public List<Actm> getActmDetailFen(String pk_actm);
	
	/**
	 * 
	 * @param pk_actm
	 * @return
	 * 获取赠送码送券表中活动条数
	 */
	public int getWordActmCount(String pk_actm);
	
	/**
	 * 
	 * @param pk_id
	 * @param pk_actm
	 * @param vcode
	 * 新增赠送码送券记录
	 */
	public void insertWordActm(String pk_actm,String vcode);
	
	/**
	 * 
	 * @param pk_actm
	 * 更新已送券数量
	 */
	public int updateWordActm(String pk_actm,int iticketnum);
}
