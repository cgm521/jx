package com.choice.wechat.persistence.myCard;

import java.util.List;

import com.choice.test.domain.Card;
import com.choice.test.domain.CardTyp;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.Voucher;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.myCard.Coupon;
import com.choice.wechat.domain.myCard.MemberRight;
import com.choice.wechat.domain.myCard.NetGoodsOrders;

public interface MyCardMapper {
	
	/**
	 * 查询充值记录
	 * @param record
	 * @return
	 */
	public List<ChargeRecord> listChargeRecord(ChargeRecord record);
	
	/**
	 * 查询消费记录
	 * @param record
	 * @return
	 */
	public List<ConsumeRecord> listConsumeRecord(ConsumeRecord record);
	
	/**
	 * 计算赠送金额
	 * @param cardNo
	 * @param chargeDate
	 * @param chargeAmt
	 * @return
	 */
	public Double calcGiftAmt(String cardNo, String chargeDate, double chargeAmt);
	
	/**
	 * 获取电子券列表
	 * @param pk_group
	 * @return
	 */
	public List<Coupon> getCouponList(String pk_group);
	
	/**
	 * 获取活动限制门店
	 * @param pk_group
	 * @param compType
	 * @return
	 */
	public List<Firm> getLimitFirmList(String pk_group, String compType);
	
	/**
	 * 保存商品订单
	 * @param order
	 * @return
	 */
	public String saveGoodsOrder(NetGoodsOrders order);

	/**
	 * 完善客户资料是否有记录
	 * @param cardNo
	 * @param openid
	 * @return
	 */
	public Integer getCountOfRows(String cardNo, String openid);
	
	/**
	 * 新增完善资料成功记录
	 * @param pk_id
	 * @param cardNo
	 * @param openid
	 */
	public void addCardUpdate(String pk_id, String cardNo, String openid);
	
	/**
	 * 获取会员卡类型列表
	 * @return
	 */
	public List<CardTyp> getCardTypList();

	/**
	 * 根据条件查询电子卷
	 * @param voucher
	 * @return
	 */
	public List<Voucher> getVoucherByCondition(Voucher voucher);
	
	/**
	 * 
	 * @param voucher
	 * @return
	 */
	public int updateCardIdOfVoucher(Voucher voucher);
	
	/**
	 * 根据卡类型获取会员特权列表
	 * @param cardType
	 * @return
	 */
	public List<MemberRight> getMemberRightList(String cardType);
	
	/**
	 * 更新会员信息
	 * @param card
	 * @return
	 */
	public int updateCardInfo(Card card);

	/**
	 * 保存积分支出记录
	 * @param cardId
	 * @param pxfJf
	 * @param vCnt
	 * @return
	 */
	public int addCostFen(int cardId, float pxfJf, float vCnt);
	
	/**
	 * 验证会员卡密码是否正确
	 * @param cardId
	 * @param passwd
	 * @return
	 */
	public boolean verifyPassword(String cardId, String passwd);
	
	/**
	 * 修改密码
	 * @param cardId
	 * @param passwd
	 * @return
	 */
	public int setPassword(String cardId, String passwd);
	
	/**
	 * 调用赠券存储过程
	 * @param cardId
	 * @param totalAmt
	 * @param firmId
	 * @param posserial
	 */
	public void callGiftVoucher(int cardId, double totalAmt, String firmId, String posserial);
	
	/**
	 * 是否是会员
	 * @param openid
	 * @return String
	 * */
	public String isVipofOpenid(String openid);
}
