package com.choice.wechat.persistence.WeChatPay;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.choice.test.domain.Net_Orders;
import com.choice.test.exception.CRUDException;
import com.choice.wechat.domain.weChatPay.WxOrderActm;

public interface WxPayMapper {
	/**
	 *  查询该门店可用的电子券
	 * @param firmid
	 * @param dat
	 * @throws CRUDException
	 */
	public List<Map<String,Object>> queryCouponByFirm(String pk_group,String firmid,String dat);
	/**
	 * 查询微信会员绑定的实体卡信息
	 * @param pk_group
	 * @param openid
	 * @return
	 */
	public List<Map<String, Object>> queryCardByechatid(String pk_group,String openid);
	/**
	 * 查询本企业的会员支付方式
	 * @param pk_group
	 * @return
	 */
	public List<Map<String, Object>> queryCardPaytyp(String pk_group);
	/**
	 * 保存账单支付方式详情
	 * @param map
	 * @return
	 */
	public int addPayment(Map<String, Object> map) throws Exception;
	/**
	 * 查询该企业定义的电子券基本信息
	 * @param pk_group
	 * @return
	 */
	public List<Map<String, Object>> listCouponBase(String pk_group);
	/**
	 * 计算账单菜品折扣金额并保存
	 * @param pk_group
	 * @return
	 */
	public int insertPubitemDiscAmt(Map<String, Object> map) throws Exception;
	/**
	 * 查询门店信息
	 * @param firmid
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getFirmInfo(String firmid) throws Exception;
	/**
	 * 查询已下单未支付账单的支付金额
	 * @param orders
	 * @return
	 */
	public Map<String, Object> getFolioPaymoney(Net_Orders orders);
	/**
	 * 查询账单支付信息
	 * @param orders
	 */
	public List<Map<String, Object>> queryFolioPayment(Net_Orders orders);
	/**
	 * 查询发票抬头记录
	 * @param mapParam
	 * @return
	 */
	public List<Map<String, Object>> queryVinvoicetitle(Map<String, Object> mapParam);
	/**
	 * 插入发票抬头记录
	 * @param mapParam
	 */
	public void addVinvoicetitle(Map<String, Object> mapParam);
	/**
	 * 修改账单应付金额及存储门店返回的账单折扣信息
	 * @param json
	 */
	public void updateFolioDisc(JSONObject json);
	public List<Map<String, Object>> selectPubitem();
	/**
	 * 查询订单赠送信息
	 * @param orders
	 * @return
	 */
	public List<Map<String, Object>> listPreferentialZs(Net_Orders orders);
	/**
	 * 查询订单退菜信息
	 * @param orders
	 * @return
	 */
	public List<Map<String, Object>> listPreferentialTc(Net_Orders orders);
	/**
	 * 查询优惠信息（折扣）
	 * @param orders
	 * @return
	 */
	List<Map<String, Object>> listPreferentialZk(Net_Orders orders);
	/**
	 * 其他异常信息
	 * @param orders
	 * @return
	 */
	public List<Map<String, Object>> listPreferentialOtherInfo(Net_Orders orders);
	/**
	 * 添加账单电子券使用的错误信息
	 * @param object
	 * @param id 
	 */
	public void addCouponErrMsg(String object, String id);
	/**
	 * 查询菜谱方案优先级最高的那个
	 * @param pk_group
	 * @param firmid
	 * @param dat
	 * @return
	 */
	public String queryItemprgmFromBOH(String pk_group,String orderid,String firmid, String dat);

	/**
	 * 插入支付记录，供对账使用
	 * @param mapParam
	 */
	public void addTransactionRec(Map<String, Object> mapParam);
	
	/**
	 * 团购券验证记录
	 * @param orderid
	 * @param list
	 */
	public void addGroupRecord(String orderid,List<Map<String,String>> list);
	
	/**
	 * 获取团购券验证记录
	 * @param orderid
	 * @return
	 */
	public List<Map<String,Object>> getGroupRecord(String orderid);
	
	/**
	 * 修改团购券验证记录
	 * @param orderid
	 */
	public void updateGroupRecordSql(String orderid);
	
	/**
	 * 根据订单id查询使用的活动
	 * @param orderid
	 * @return
	 */
	public List<WxOrderActm> getListOrderActm(String orderid);
	
	/**
	 * 保存订单使用的活动
	 * @param orderActm
	 * @return
	 */
	public int saveOrderActm(WxOrderActm orderActm);
	
	/**
	 * 删除订单使用的活动
	 * @param orderActm
	 * @return
	 */
	public int deleteOrderActm(WxOrderActm orderActm);
	
	/**
	 * 微信支付购买电子券
	 * 保存购买电子券信息
	 * */
	public void addCouponItem(String outTradeNo,String cardNo,String openid, String pk_group,String couponcodes,String couponnums,String sumpoint,String sumprice,String vnum);
	
	/**
	 * 支付成功修改电子券信息
	 * */
	public void updateCouponItem(String outTradeNo);
	/**
	 * 查询购买电子券信息
	 * @param outTradeNo
	 * */
	public List<Map<String,Object>> queryWxCouponItem(String outTradeNo);
}
