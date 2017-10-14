package com.choice.test.persistence;

import java.util.List;
import java.util.Map;

import com.choice.test.domain.Card;
import com.choice.test.domain.CardRules;
import com.choice.test.domain.CardTyp;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.Voucher;
import com.choice.wechat.domain.myCard.LabelFavorite;
/**
 * 微信会员
 * @author 孙胜彬
 *
 */
public interface WeChatCrmMapper {
	/**
	 * 获取会员信息
	 * @param queryCardNo 卡号
	 * @param queryName 姓名
	 * @param queryTele 手机号
	 * @return
	 * @throws Exception
	 */
	public List<Card> queryCardWebService(Card condition) throws Exception;

	/**
	 * 充值记录
	 * @param queryCardNo 卡号
	 * @param wechatId 微信号
	 * @return
	 * @throws Exception
	 */
	public List<ChargeRecord> chargeRecordWebService(String queryCardNo,String wechatId) throws Exception;

	/**
	 * 消费记录
	 * @param queryCardNo 卡号
	 * @param wechatId 微信号
	 * @return
	 * @throws Exception
	 */
	public List<ConsumeRecord> consumeRecordWebService(String queryCardNo,String wechatId) throws Exception;

	
	/**
	 * 会员充值
	 * @param cardNo 卡号
	 * @param inAmt 充值金额
	 * @param empNo 业务员工号
	 * @param empName 业务员姓名
	 * @param firmId 门店编码
	 * @param invoiceNo 发票号
	 * @param invoiceAmt 发票额
	 * @param dateTime 充值时间
	 * @param payMentCode 支付方式编码
	 * @param payMent 支付方式
	 * @return
	 * @throws Exception
	 */
//	public String cardInAmtWebService(String cardNo, double inAmt, int empNo,String empName, int firmId, String invoiceNo, double invoiceAmt, Date dateTime, int payMentCode,String payMent) throws Exception ;
	
	/**
	 * 会员扣款
	 * @param cardNo 卡号
	 * @param outAmt 扣款金额
	 * @param firmId 分店编号
	 * @param date 扣款日期
	 * @param invoiceAmt 发票额
	 * @param empName 收银员姓名
	 * @return
	 * @throws Exception
	 */
//	public int cardOutAmtWebService(String cardNo,double outAmt,int firmId,Date date,double invoiceAmt,String empName) throws Exception;
	
	/**
	 * 新增会员信息（为微信接口使用）
	 * @param name 姓名
	 * @param tele 手机号
	 * @param wechatId 微信号
	 * @param typ 卡类别id
	 * @param typDes 卡类别名称
	 * @param chlb 会员类别
	 * @param othersShareCode 推荐人的推荐码
	 * @return
	 * @throws Exception
	 */
	public String addCardWebService(String name,String tele,String wechatId,String typ, String typDes, String chlb, String rest, String passwd,String othersShareCode) throws Exception;
	
	/**
	 * 微信绑定老会员（为微信接口使用）
	 * @param cardno 卡号
	 * @param wechatId 微信号
	 * @return
	 * @throws Exception
	 */
	public String addWXCardWebService(String cardNo,String wechatId, String passwd) throws Exception;
	
	/**
	 * 获取短信验证码
	 * @param tele
	 * @return
	 * @throws Exception
	 */
	public String generateRandom(String tele) throws Exception;
	
	/**
	 * 获取电子卷
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<Voucher> getVoucher(String openid) throws Exception;
	/**
	 * 会员卡规则注释表
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<CardRules> getCardRules(String openid) throws Exception;
	/**
	 * 修改手机号
	 * @param tele
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public String updateTele(String openid,String tele) throws Exception;
	/**
	 * 查询卡类型
	 * @param tele
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<CardTyp> getCardTyp(String id) throws Exception;
	/**
	 * 充值
	 * @param cardId 卡ID
	 * @param totalAmt 充值总额
	 * @param zAmt 充值前余额
	 * @param empNo 业务员编号
	 * @param firmId 分店编号
	 * @param isCredit 是否充值信用额度
	 * @param invoiceNo 发票号
	 * @param isGrpCard 是否团队卡
	 * @param invoiceAmt 发票额
	 * @param inAmt 充值本金
	 * @param giftAmt 赠送金额
	 * @param paymentCode 支付方式编码
	 * @param payment 支付方式名称
	 * @param dateTime 充值日期
	 * @param endDate 会员卡有效期截止
	 * @param empName 业务员姓名
	 * @param oEmpName 操作员姓名
	 * @return
	 */
	public int changeAmt(int cardId,double totalAmt,double zAmt,String empNo,String firmId,String isCredit,String invoiceNo,String isGrpCard,
			double invoiceAmt,double inAmt,double giftAmt,String payMentCode,String payMent,String dateTime,String endDate,String empName,String oEmpName);
	
	/**
	 * 
	 * @param cardId 卡ID
	 * @param totalAmt 充值总额
	 * @param zAmt 充值前余额
	 * @param empNo 业务员编号
	 * @param firmId 分店编号
	 * @param invoiceNo 发票号
	 * @param rsn 反冲原因编号
	 * @param invoiceAmt 发票额
	 * @param rmbAmt 充值本金
	 * @param giftAmt 赠送金额
	 * @param payMentCode 支付方式编码
	 * @param payMent 支付方式名称
	 * @param dateTime 充值日期
	 * @param empName 业务员姓名
	 * @param oEmpName 操作员姓名
	 * @param tim 充值时间戳
	 * @param sft 班次
	 * @param posId pos编号
	 * @param flag 0：充值； 1：反冲
	 * @param poserial pos流水号
	 * @return
	 */
	public int charge(int cardId, double totalAmt, double zAmt, String empNo, String firmId, String invoiceNo, int rsn, 
			double invoiceAmt, double rmbAmt, double giftAmt, String payMentCode, String payMent, String dateTime, String empName,String oEmpName,
			String tim, String sft, String posId, int flag, String poserial);
	
	/**
	 * 查询剩余、获得、支出积分
	 * @param cardNo 会员卡号
	 * @param cardId 会员卡ID
	 * @param wechatId 微信ID
	 * @param pk_group 企业ID
	 * @return
	 */
	public Map<String, Object> getIntegrationRecodeSum(String cardNo,String cardId, String wechatId, String pk_group);
	/**
	 * 查询积分明细
	 * @param cardNo 卡号
	 * @param cardId 卡ID
	 * @param openid 微信id
	 * @param pk_group 企业ID
	 * @param type 类型
	 * @return
	 */
	public List<Map<String, Object>> getIntegrationRecodes(String cardNo,
			String cardId, String openid, String pk_group, String type);
	
	/**
	 * 根据会员卡类型及门店编码查询数据是否存在
	 * @param typ
	 * @param rest
	 * @return
	 * @throws Exception
	 */
	public boolean isCardTypRestExist(String typ, String rest) throws Exception;
	/**
	 * 查询会员可以选择的所有标签和兴趣爱好
	 * @return
	 */
	public List<LabelFavorite> getAllLabelFavorite();
	/**
	 * 生成会员卡二维码和条形码
	 * @param cardNo
	 * @param cardId
	 */
	public void generateQRCodeAndBarCode(String cardNo, String cardId);
}
