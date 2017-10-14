package com.choice.webService;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name="CRMWebService")
public interface CRMWebservice {

	/**
	 * 获取会员信息
	 * @param queryCardNo 卡号
	 * @param queryName 姓名
	 * @param queryMobTel 手机号
	 * @return
	 * @throws Exception
	 */
	public String queryCardWebService(
			@WebParam(name = "queryCardNo",targetNamespace = "http://webService.choice.com/")String queryCardNo,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/")String password,
			@WebParam(name = "queryName",targetNamespace = "http://webService.choice.com/")String queryName,
			@WebParam(name = "queryTele",targetNamespace = "http://webService.choice.com/")String queryMobTel,
			@WebParam(name = "wechatId",targetNamespace = "http://webService.choice.com/")String wechatId,
			@WebParam(name = "cardId",targetNamespace = "http://webService.choice.com/")String cardId) throws Exception;
	
	
	/**
	 * 充值记录
	 * @param queryCardNo 卡号
	 * @param wechatId 微信号
	 * @return
	 * @throws Exception
	 */
	public String chargeRecordWebService(
			@WebParam(name = "queryCardNo",targetNamespace = "http://webService.choice.com/")String queryCardNo,
			@WebParam(name = "wechatId",targetNamespace = "http://webService.choice.com/") String wechatId) throws Exception;

	/**
	 * 消费记录
	 * @param queryCardNo 卡号
	 * @param wechatId 微信号
	 * @return
	 * @throws Exception
	 */
	public String consumeRecordWebService(
			@WebParam(name = "queryCardNo",targetNamespace = "http://webService.choice.com/")String queryCardNo,
			@WebParam(name = "wechatId",targetNamespace = "http://webService.choice.com/") String wechatId) throws Exception;

	/**
	 * 计算赠送金额
	 * @param cardId
	 * @param firmId
	 * @param dateString
	 * @param amt
	 * @param grp
	 * @param payment
	 * @return
	 * @throws Exception
	 */
	public double calcChangeAmtDiscPayment(
			@WebParam(name = "cardId",targetNamespace = "http://webService.choice.com/")String cardId, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId, 
			@WebParam(name = "dateString",targetNamespace = "http://webService.choice.com/")String dateString,
			@WebParam(name = "amt",targetNamespace = "http://webService.choice.com/")double amt, 
			@WebParam(name = "grp",targetNamespace = "http://webService.choice.com/")String grp, 
			@WebParam(name = "payment",targetNamespace = "http://webService.choice.com/")String payment 
			) throws Exception;
	
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
	public String cardInAmtWebService(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo, 
			@WebParam(name = "inAmt",targetNamespace = "http://webService.choice.com/")double inAmt, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/")String empNo,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/")String empName, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId, 
			@WebParam(name = "invoiceNo",targetNamespace = "http://webService.choice.com/")String invoiceNo, 
			@WebParam(name = "invoiceAmt",targetNamespace = "http://webService.choice.com/")double invoiceAmt, 
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/")String dateTime, 
			@WebParam(name = "payMentCode",targetNamespace = "http://webService.choice.com/")String payMentCode,
			@WebParam(name = "payMent",targetNamespace = "http://webService.choice.com/")String payMent) throws Exception ;

	
	/**
	 * 会员卡扣款
	 * @param cardNo 卡号
	 * @param firmId 分店编号
	 * @param firmName 分店名称
	 * @param outAmt 扣款金额
	 * @param dateString 扣款日期
	 * @param invoiceAmt 发票额
	 * @param emp 收银员编码
	 * @param empName 收银员姓名	 
	 * @param billAmt 账单金额
	 * @param conacct 备注
	 * @param discAmt 折扣金额
	 * @param billno 账单号
	 * @param grpsta 是否团队卡 默认N
	 * @param credit 信用额度
	 * @param pax 人数
	 * @param paytyp 结算方式
	 * @param fenAmt 可积分金额
	 * @return
	 * @throws Exception
	 */
	public String cardOutAmtWebService(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo,
			@WebParam(name = "outAmt",targetNamespace = "http://webService.choice.com/")double outAmt,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId,
			@WebParam(name = "firmName",targetNamespace = "http://webService.choice.com/")String firmName,
			@WebParam(name = "dateString",targetNamespace = "http://webService.choice.com/")String dateString,
			@WebParam(name = "invoiceAmt",targetNamespace = "http://webService.choice.com/")double invoiceAmt,
			@WebParam(name = "emp",targetNamespace = "http://webService.choice.com/")String emp,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/")String empName,
			@WebParam(name = "billAmt",targetNamespace = "http://webService.choice.com/")double billAmt,
			@WebParam(name = "conacct",targetNamespace = "http://webService.choice.com/")String conacct,
			@WebParam(name = "discAmt",targetNamespace = "http://webService.choice.com/")double discAmt,
			@WebParam(name = "billno",targetNamespace = "http://webService.choice.com/")String billno,
			@WebParam(name = "grpsta",targetNamespace = "http://webService.choice.com/")String grpsta,
			@WebParam(name = "credit",targetNamespace = "http://webService.choice.com/")double credit,
			@WebParam(name = "pax",targetNamespace = "http://webService.choice.com/")int pax,
			@WebParam(name = "paytyp",targetNamespace = "http://webService.choice.com/")String paytyp,
			@WebParam(name = "fenAmt",targetNamespace = "http://webService.choice.com/")double fenAmt) throws Exception;
	
	/**
	 * 新增会员信息
	 * @param name 姓名
	 * @param tele 手机号
	 * @param wechatId 微信号
	 * @param typId 卡类型编码
	 * @return
	 * @throws Exception
	 */
	public String addCardWebService(
			@WebParam(name = "name",targetNamespace = "http://webService.choice.com/")String name,
			@WebParam(name = "tele",targetNamespace = "http://webService.choice.com/")String tele,
			@WebParam(name = "wechatId",targetNamespace = "http://webService.choice.com/")String wechatId,
			@WebParam(name = "typDes",targetNamespace = "http://webService.choice.com/")String typId) throws Exception;
	
	
	
	/**
	 * 微信绑定老会员（为微信接口使用）
	 * @param cardno 卡号
	 * @param wechatId 微信号
	 * @return
	 * @throws Exception
	 */
	public String addWXCardWebService(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo,
			@WebParam(name = "wechatId",targetNamespace = "http://webService.choice.com/")String wechatId) throws Exception;
	
	/**
	 * 解除微信绑定老会员（为微信接口使用）
	 * @param cardno 卡号
	 * @param wechatId 微信号
	 * @return
	 * @throws Exception
	 */
	public String removeWXCardWebService(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo,
			@WebParam(name = "wechatId",targetNamespace = "http://webService.choice.com/")String wechatId) throws Exception;
	
	/**
	 * 获取openID
	 * @param appId
	 * @param secret
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getOpenIDWebService(
			@WebParam(name = "appId",targetNamespace = "http://webService.choice.com/")String appId,
			@WebParam(name = "secret",targetNamespace = "http://webService.choice.com/")String secret,
			@WebParam(name = "code",targetNamespace = "http://webService.choice.com/")String code) throws Exception;
	
	/**
	 * 根据卡号生成二维码
	 * @param cardNo
	 * @return
	 * @throws Exception
	 */
	public String generateQRCodeWebService(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo) throws Exception;
	/**
	 * 获取短信验证码
	 * @param tele
	 * @return
	 * @throws Exception
	 */
	public String generateRandom(@WebParam(name = "tele",targetNamespace = "http://webService.choice.com/")String tele) throws Exception;
	
	/**
	 * 获取电子卷
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public String getVoucher(@WebParam(name = "openid",targetNamespace = "http://webService.choice.com/")String openid) throws Exception;
	/**
	 * 会员卡规则注释表
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public String getCardRules(@WebParam(name = "openid",targetNamespace = "http://webService.choice.com/")String openid) throws Exception;
	/**
	 * 修改手机号
	 * @param tele
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public String updateTele(
			@WebParam(name = "openid",targetNamespace = "http://webService.choice.com/")String openid,
			@WebParam(name = "tele",targetNamespace = "http://webService.choice.com/")String tele) throws Exception;
	
	/**
	 * 完善客户资料
	 * @param tele
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public String updateCardInfoWechat(
			@WebParam(name = "openid",targetNamespace = "http://webService.choice.com/")String openid,
			@WebParam(name = "name",targetNamespace = "http://webService.choice.com/")String name,
			@WebParam(name = "sex",targetNamespace = "http://webService.choice.com/")String sex,
			@WebParam(name = "birthday",targetNamespace = "http://webService.choice.com/")String birthday,
			@WebParam(name = "email",targetNamespace = "http://webService.choice.com/")String email,
			@WebParam(name = "addr",targetNamespace = "http://webService.choice.com/")String addr,
			@WebParam(name="label",targetNamespace="http://webService.choice.com/")String label,
			@WebParam(name="favorite",targetNamespace="http://webService.choice.com/")String favorite) throws Exception;
	
	
	/**
	 * 查询卡类型
	 * @param tele
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public String getCardTyp(
			@WebParam(name = "id",targetNamespace = "http://webService.choice.com/")String id) throws Exception;

	
	/************************************************************向阳渔港微信会员独立出的接口*************************************************************/
	
	/**
	 * 获取会员信息（向阳渔港）
	 * @param queryCardNo 卡号
	 * @param idCard 身份证
	 * @return
	 * @throws Exception
	 */
	public String findCardWebService(@WebParam(name = "findCardWebService",targetNamespace = "http://webService.choice.com/")String queryCardNo,String idCard) throws Exception;

	/**
	 * 微信绑定会员（向阳渔港）
	 * @param cardno 卡号
	 * @param idNo 身份证
	 * @return
	 * @throws Exception
	 */
	public String saveWXCardWebService(@WebParam(name = "addWXCardWebService",targetNamespace = "http://webService.choice.com/")String cardNo,String idNo) throws Exception;
	
	/**
	 * 执行查询语句
	 * @param sql 查询sql
	 * @param colCount 结果列的个数
	 * @return
	 */
	public String commonExecQuerySql(
			@WebParam(name = "passInfo",targetNamespace = "http://webService.choice.com/")String passInfo,
			@WebParam(name = "sql",targetNamespace = "http://webService.choice.com/")String sql);
	
	/**
	 * 执行新增或修改语句
	 * @param sql
	 * @return
	 */
	public String commonExecSaveOrUpdateSql(
			@WebParam(name = "passInfo",targetNamespace = "http://webService.choice.com/")String passInfo,
			@WebParam(name = "sql",targetNamespace = "http://webService.choice.com/")String sql);
	
	/**
	 * 执行存储过程
	 * @param procedureWithParam 带参数的存储过程
	 * @param outparamIndex
	 * @param outparamCount
	 * @return
	 */
	public String commonExecProcedure(
			@WebParam(name = "passInfo",targetNamespace = "http://webService.choice.com/")String passInfo,
			@WebParam(name = "procedureWithParam",targetNamespace = "http://webService.choice.com/")String procedureWithParam,
			@WebParam(name = "outparamCount",targetNamespace = "http://webService.choice.com/")int outparamCount);

	/**
	 * 验证团购码是否能够使用
	 * @param type 类型（1美团2糯米3大众4代金券）
	 * @param code 团购码
	 * @return 0 不能使用 1可以使用
	 */
	public String validateCouponCode(
			@WebParam(name = "type",targetNamespace = "http://webService.choice.com/")String type,
			@WebParam(name = "code",targetNamespace = "http://webService.choice.com/")String code,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId,
			@WebParam(name = "firmName",targetNamespace = "http://webService.choice.com/")String firmName);
	
	/**
	 * 团购券消费接口
	 * @param type 类型（1美团2糯米3大众4代金券）
	 * @param code 团购码
	 * @return 0 消费成功 1消费失败
	 */
	public String consumerCouponCode(
			@WebParam(name = "type",targetNamespace = "http://webService.choice.com/")String type,
			@WebParam(name = "code",targetNamespace = "http://webService.choice.com/")String code,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId,
			@WebParam(name = "firmName",targetNamespace = "http://webService.choice.com/")String firmName);
	
	/**
	 * 团购券冲正接口
	 * @param type 类型（1美团2糯米3大众4代金券）
	 * @param code 团购码
	 * @return 0冲正成功1冲正失败
	 */
	public String reverserCouponCode(
			@WebParam(name = "type",targetNamespace = "http://webService.choice.com/")String type,
			@WebParam(name = "code",targetNamespace = "http://webService.choice.com/")String code,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId,
			@WebParam(name = "firmName",targetNamespace = "http://webService.choice.com/")String firmName);
	
	/**
	 * 协议账户挂账
	 * @param cardNo
	 * @param itcode
	 * @param amt
	 * @param empNo
	 * @param folio
	 * @param sft
	 * @param firmName
	 * @param tbl
	 * @param tbldes
	 * @param des
	 * @param discamt
	 * @param whemp
	 * @return
	 */
	public String costAcct(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo,
			@WebParam(name = "itcode",targetNamespace = "http://webService.choice.com/")String itcode,
			@WebParam(name = "amt",targetNamespace = "http://webService.choice.com/")double amt,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/")String empNo,
			@WebParam(name = "folio",targetNamespace = "http://webService.choice.com/")String folio,
			@WebParam(name = "sft",targetNamespace = "http://webService.choice.com/")int sft,
			@WebParam(name = "firmName",targetNamespace = "http://webService.choice.com/")String firmName,
			@WebParam(name = "tbl",targetNamespace = "http://webService.choice.com/")int tbl,
			@WebParam(name = "tbldes",targetNamespace = "http://webService.choice.com/")String tbldes,
			@WebParam(name = "des",targetNamespace = "http://webService.choice.com/")String des,
			@WebParam(name = "discamt",targetNamespace = "http://webService.choice.com/")double discamt,
			@WebParam(name = "whemp",targetNamespace = "http://webService.choice.com/")String whemp);
	
/************************************************会员在线消费***********************************************************/	
	/**
	 * 根据卡号获取会员信息
	 * @param queryCardNo 卡号
	 * @return 该卡号所有信息 
	 * @throws Exception
	 */
	public String queryCardByCardNo(
			@WebParam(name = "queryCardNo",targetNamespace = "http://webService.choice.com/")String queryCardNo) throws Exception;
	
	/**
	 * 根据卡ID获取会员信息
	 * @param queryCardId 卡ID
	 * @return 该卡号所有信息 
	 * @throws Exception
	 */
	public String queryCardByCardId(
			@WebParam(name = "queryCardId",targetNamespace = "http://webService.choice.com/")String queryCardId) throws Exception;
	
	/**
	 * 根据手机号获取所有关联的卡号
	 * @param queryMobTel 手机号
	 * @return 所有关联的卡号
	 * @throws Exception
	 */
	public String queryCardByMobTel(
			@WebParam(name = "queryMobTel",targetNamespace = "http://webService.choice.com/")String queryMobTel) throws Exception;
	
	/**
	 * 新增会员
	 * @param cardNo
	 * @param name
	 * @param mobTel
	 * @param cardTypId
	 * @param password
	 * @return
	 */
	public String addCard(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo,
			@WebParam(name = "name",targetNamespace = "http://webService.choice.com/")String name,
			@WebParam(name = "mobTel",targetNamespace = "http://webService.choice.com/")String mobTel,
			@WebParam(name = "cardTypeId",targetNamespace = "http://webService.choice.com/")int cardTypeId,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/")String password);

	/**
	 * 修改支付密码
	 * @param cardNo
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public String updatePassword(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo, 
			@WebParam(name = "oldPassword",targetNamespace = "http://webService.choice.com/")String oldPassword,
			@WebParam(name = "newPassword",targetNamespace = "http://webService.choice.com/")String newPassword);

	/**
	 * 会员充值
	 * @param cardNo 卡号
	 * @param zAmt 充值前余额
	 * @param rmbAmt 本金
	 * @param giftAmt 赠送
	 * @param inAmt 充值总额
	 * @param empNo 充值员工编号
	 * @param empName 充值员工姓名
	 * @param firmId 门店编码
	 * @param invoiceNo 发票号
	 * @param invoiceAmt 发票金额
	 * @param dateTime 充值时间（yyyy-MM-dd HH:mm:ss）
	 * @param paymentCode 支付方式编码
	 * @param payment 支付方式
	 * @param sft 班次
	 * @param posid posID
	 * @param serial 流水号
	 * @return
	 */
	public String cardInAmt(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo,
			@WebParam(name = "zAmt",targetNamespace = "http://webService.choice.com/")double zAmt,
			@WebParam(name = "rmbAmt",targetNamespace = "http://webService.choice.com/")double rmbAmt,
			@WebParam(name = "giftAmt",targetNamespace = "http://webService.choice.com/")double giftAmt,
			@WebParam(name = "inAmt",targetNamespace = "http://webService.choice.com/")double inAmt, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/")String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/")String empName, 
			@WebParam(name = "oEmpName",targetNamespace = "http://webService.choice.com/")String oEmpName,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId, 
			@WebParam(name = "invoiceNo",targetNamespace = "http://webService.choice.com/")String invoiceNo, 
			@WebParam(name = "invoiceAmt",targetNamespace = "http://webService.choice.com/")double invoiceAmt, 
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/")String dateTime, 
			@WebParam(name = "paymentCode",targetNamespace = "http://webService.choice.com/")int paymentCode, 
			@WebParam(name = "payment",targetNamespace = "http://webService.choice.com/")String payment,
			@WebParam(name = "sft",targetNamespace = "http://webService.choice.com/")String sft,
			@WebParam(name = "posid",targetNamespace = "http://webService.choice.com/")String posid, 
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/")String serial);

	/**
	 * 充值冲正
	 * @param cardNo
	 * @param empNo
	 * @param empName
	 * @param firmId
	 * @param dateTime
	 * @param rsn
	 * @param sft
	 * @param posid
	 * @param serial
	 * @param password
	 * @return
	 */
	public String cardInAmtReverse(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/")String cardNo, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/")String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/")String empName, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/")String firmId, 
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/")String dateTime ,
			@WebParam(name = "rsn",targetNamespace = "http://webService.choice.com/")int rsn, 
			@WebParam(name = "sft",targetNamespace = "http://webService.choice.com/")String sft,
			@WebParam(name = "posid",targetNamespace = "http://webService.choice.com/")String posid, 
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/")String serial,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/")String password);
	
	/**
	 * 会员消费
	 * @param cardNo
	 * @param firmId
	 * @param fenAmt
	 * @param cardAmt
	 * @param dateString
	 * @param invoiceAmt
	 * @param empNo
	 * @param empName
	 * @param billAmt
	 * @param conacct
	 * @param discAmt
	 * @param billno
	 * @param pax
	 * @param paytypeCode
	 * @param paytyp
	 * @param jifenAmt
	 * @param posid
	 * @param sft
	 * @param serial
	 * @param password
	 * @return
	 */
	public String cardOutAmt(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "fenAmt",targetNamespace = "http://webService.choice.com/") double fenAmt,
			@WebParam(name = "cardAmt",targetNamespace = "http://webService.choice.com/") double cardAmt,
			@WebParam(name = "dateString",targetNamespace = "http://webService.choice.com/") String dateString,
			@WebParam(name = "invoiceAmt",targetNamespace = "http://webService.choice.com/") double invoiceAmt,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "billAmt",targetNamespace = "http://webService.choice.com/") double billAmt,
			@WebParam(name = "conacct",targetNamespace = "http://webService.choice.com/") String conacct,
			@WebParam(name = "discAmt",targetNamespace = "http://webService.choice.com/") double discAmt,
			@WebParam(name = "billno",targetNamespace = "http://webService.choice.com/") String billno,
			@WebParam(name = "pax",targetNamespace = "http://webService.choice.com/") int pax,
			@WebParam(name = "paytypeCode",targetNamespace = "http://webService.choice.com/") int paytypeCode,
			@WebParam(name = "paytyp",targetNamespace = "http://webService.choice.com/") String paytyp,
			@WebParam(name = "jifenAmt",targetNamespace = "http://webService.choice.com/") double jifenAmt,
			@WebParam(name = "posid",targetNamespace = "http://webService.choice.com/") String posid,
			@WebParam(name = "sft",targetNamespace = "http://webService.choice.com/") String sft,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password);
	
	/**
	 * 会员消费冲正
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param serial
	 * @return
	 */
	public String cardOutAmtReverse(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime, 
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password);

	/**
	 * 会员充积分
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param fen
	 * @param serial
	 * @return
	 */
	public String cardInPoint(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime, 
			@WebParam(name = "fen",targetNamespace = "http://webService.choice.com/") int fen, 
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial);
	
	/**
	 * 会员充积分冲正
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param serial
	 * @param password
	 * @return
	 */
	public String cardInPointReverse(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password);

	/**
	 * 会员扣积分
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param fen
	 * @param serial
	 * @param password
	 * @return
	 */
	public String cardOutPoint(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime, 
			@WebParam(name = "fen",targetNamespace = "http://webService.choice.com/") int fen,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password);

	/**
	 * 会员扣积分冲正
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param serial
	 * @return
	 */
	public String cardOutPointReverse(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo, 
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial);
	
	/**
	 * 会员新增电子券
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param actCode
	 * @param dateTime
	 * @param couponCode
	 * @param num
	 * @param validdate
	 * @param serial
	 * @return
	 */
	public String cardInCoupon(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "actCode",targetNamespace = "http://webService.choice.com/") String actCode,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime,
			@WebParam(name = "couponCode",targetNamespace = "http://webService.choice.com/") String couponCode,
			@WebParam(name = "num",targetNamespace = "http://webService.choice.com/") int num,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial);
	
	/**
	 * 会员新增电子券冲正
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param password
	 * @param serial
	 * @return
	 */
	public String cardInCouponReverse(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId, 
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime, 
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial);
	
	/**
	 * 会员扣电子券
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param couponCode
	 * @param password
	 * @return
	 */
	public String cardOutCoupon(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo, 
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime,
			@WebParam(name = "couponCode",targetNamespace = "http://webService.choice.com/") String couponCode,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial,
			@WebParam(name = "billno",targetNamespace = "http://webService.choice.com/") String billno);
	
	/**
	 * 会员扣电子券冲正
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param couponCode
	 * @return
	 */
	public String cardOutCouponReverse(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "dateTime",targetNamespace = "http://webService.choice.com/") String dateTime,
			@WebParam(name = "couponCode",targetNamespace = "http://webService.choice.com/") String couponCode,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial);
	
	/**
	 * 会员挂失
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param couponCode
	 * @param password
	 * @return
	 */
	public String cardLoss(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "rsn",targetNamespace = "http://webService.choice.com/") int rsn,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password);
	
	/**
	 * 会员退卡
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param couponCode
	 * @param password
	 * @return
	 */
	public String cardBack(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "rsn",targetNamespace = "http://webService.choice.com/") int rsn,
			@WebParam(name = "amt",targetNamespace = "http://webService.choice.com/") double amt,
			@WebParam(name = "giftAmt",targetNamespace = "http://webService.choice.com/") double giftAmt,
			@WebParam(name = "backAmt",targetNamespace = "http://webService.choice.com/") double backAmt,
			@WebParam(name = "password",targetNamespace = "http://webService.choice.com/") String password);
	
	/**
	 * 查询可以反充的所有记录
	 * @param cardNo
	 */
	public String findReverseInAmt(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo);
	
	/***********************************************班禾**************************************************************/
	/**
	 * 会员退卡
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param couponCode
	 * @param password
	 * @return
	 */
	public String cardBackBH(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "backAmt",targetNamespace = "http://webService.choice.com/") double backAmt,
			@WebParam(name = "datetime",targetNamespace = "http://webService.choice.com/") String datetime,
			@WebParam(name = "posserial",targetNamespace = "http://webService.choice.com/") String posserial,
			@WebParam(name = "posid",targetNamespace = "http://webService.choice.com/") String posid,
			@WebParam(name = "init",targetNamespace = "http://webService.choice.com/") int init);
	
	/**
	 * 会员发卡
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param couponCode
	 * @param password
	 * @return
	 */
	public String cardSendBH(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "cardTypId",targetNamespace = "http://webService.choice.com/") String cardTypId,
			@WebParam(name = "datetime",targetNamespace = "http://webService.choice.com/") String datetime,
			@WebParam(name = "amt",targetNamespace = "http://webService.choice.com/") double amt,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName);
	
	/**
	 * 会员制卡
	 * @param cardNo
	 * @param firmId
	 * @param empNo
	 * @param empName
	 * @param dateTime
	 * @param couponCode
	 * @param password
	 * @return
	 */
	public String cardMakeBH(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "cardTypId",targetNamespace = "http://webService.choice.com/") String cardTypId,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "empNo",targetNamespace = "http://webService.choice.com/") String empNo,
			@WebParam(name = "empName",targetNamespace = "http://webService.choice.com/") String empName,
			@WebParam(name = "amt",targetNamespace = "http://webService.choice.com/") double amt,
			@WebParam(name = "datetime",targetNamespace = "http://webService.choice.com/") String datetime);
	
	/**
	 * 撤销最近一次操作
	 * @param cardNo
	 * @param firmId
	 * @param posid
	 * @param serial
	 * @return
	 */
	public String cancelLastOperate(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "firmId",targetNamespace = "http://webService.choice.com/") String firmId,
			@WebParam(name = "posid",targetNamespace = "http://webService.choice.com/") String posid,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial);
	
	/**
	 * 根据卡号流水号查询消费记录
	 * @param cardNo
	 * @param serial
	 * @return
	 */
	public String findCardordrs(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "serial",targetNamespace = "http://webService.choice.com/") String serial);
	
	
	/**
	 * 根据卡号获取所有有消费记录的日期
	 * @param cardNo
	 * @param bdat
	 * @param edat
	 * @return
	 */
	public String getCardOutAmtDate(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "bdat",targetNamespace = "http://webService.choice.com/") String bdat,
			@WebParam(name = "edat",targetNamespace = "http://webService.choice.com/") String edat);
	
	/**
	 * 根据卡号消费日期查询账单详情
	 * @param cardNo
	 * @param dateString
	 * @return
	 */
	public String getCardOutAmtDetail(
			@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo,
			@WebParam(name = "dateString", targetNamespace = "http://webService.choice.com/") String dateString);
	
	
	/*********************************************************************************************************************/
	/**
	 * 开卡（微信开卡时调用，实现开卡赠送电子券功能）
	 * @param cardNo
	 * @return
	 */
	public String openCard(@WebParam(name = "cardNo",targetNamespace = "http://webService.choice.com/") String cardNo);
	/**
	 * 非微信会员点外卖时，自动注册为微信会员
	 *正确返回1，错误返回-3 
	 */
	public String takeOutMemberRegesiter(
			@WebParam(name="name",targetNamespace="http://webService.choice.com/") String name,
			@WebParam(name="mobTel",targetNamespace="http://webService.choice.com/") String mobTel,
			@WebParam(name="address",targetNamespace="http://webService.choice.com/") String address,
			@WebParam(name="firmId",targetNamespace="http://webService.choice.com/") String firmId,
			@WebParam(name="billAmt",targetNamespace="http://webService.choice.com/") String billAmt,
			@WebParam(name="wechatId",targetNamespace="http://webService.choice.com/") String wechatId
			);
}
