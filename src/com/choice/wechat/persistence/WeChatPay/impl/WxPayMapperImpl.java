package com.choice.wechat.persistence.WeChatPay.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.test.domain.Net_OrderDtl;
import com.choice.test.domain.Net_OrderPackageDetail;
import com.choice.test.domain.Net_Orders;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.wechat.domain.ListMapMapper;
import com.choice.wechat.domain.bookMeal.FdItemSale;
import com.choice.wechat.domain.bookMeal.NetDishAddItem;
import com.choice.wechat.domain.bookMeal.NetDishProdAdd;
import com.choice.wechat.domain.bookMeal.ProductAdditional;
import com.choice.wechat.domain.weChatPay.WxOrderActm;
import com.choice.wechat.persistence.WeChatPay.WxPayMapper;
import com.choice.wechat.persistence.bookMeal.BookMealMapper;
import com.choice.wechat.service.WeChatPay.IWxPayService;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.ValueCheck;
import com.choice.wechat.util.WeChatUtil;

@Repository
public class WxPayMapperImpl implements WxPayMapper{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private IWxPayService wxPayService;
	@Autowired
	private BookMealMapper bookMealMapper;
	/**
	 * 查询该门店可用的电子券
	 */
	private final static String queryCouponFromBOH = "select distinct a.vvouchercode"+
				" from cboh_actm_3ch a,cboh_actstr_3ch f"+
				" where a.pk_actm = f.pk_actm and a.enablestate=2 and a.vvoucherdisc='Y'  ";
	public List<Map<String, Object>> queryCouponByFirm(String pk_group,String firmid, String dat) {
		
		StringBuilder sb = new StringBuilder(queryCouponFromBOH);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(pk_group)){
			sb.append(" AND a.pk_group=? ");
			valuesList.add(pk_group);
		}
		
		if(StringUtils.hasText(firmid)){
			sb.append(" AND f.pk_store=? ");
			valuesList.add(firmid);
		}
		if(StringUtils.hasText(dat)){
			sb.append(" and (f.xzdate='N' or (f.xzdate='Y' ");
			sb.append(" and (( mdbegintime1 is not null and mdendtime1 is not null and ? between mdbegintime1 and mdendtime1) or (mdbegintime1 is not null and mdendtime1 is null and ? >= mdbegintime1) or (mdbegintime1 is null and mdendtime1 is not null and ? <= mdendtime1)) ");
			sb.append(" and (( mdbegintime2 is not null and mdendtime2 is not null and ? not between mdbegintime2 and mdendtime2) or( mdbegintime2 is not null and mdendtime2 is null and ? not between mdbegintime2 and '2099-01-01') or( mdbegintime2 is null and mdendtime2 is not null and ? not between '1990-01-01' and mdendtime2) or ( mdbegintime2 is null and mdendtime2 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ");
			sb.append(" and (( mdbegintime3 is not null and mdendtime3 is not null and ? not between mdbegintime3 and mdendtime3) or( mdbegintime3 is not null and mdendtime3 is null and ? not between mdbegintime3 and '2099-01-01') or( mdbegintime3 is null and mdendtime3 is not null and ? not between '1990-01-01' and mdendtime3) or ( mdbegintime3 is null and mdendtime3 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ");
			sb.append(" and (( mdbegintime4 is not null and mdendtime4 is not null and ? not between mdbegintime4 and mdendtime4) or( mdbegintime4 is not null and mdendtime4 is null and ? not between mdbegintime4 and '2099-01-01') or( mdbegintime4 is null and mdendtime4 is not null and ? not between '1990-01-01' and mdendtime4) or ( mdbegintime4 is null and mdendtime4 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ");
			sb.append(" and (( mdbegintime5 is not null and mdendtime5 is not null and ? not between mdbegintime5 and mdendtime5) or( mdbegintime5 is not null and mdendtime5 is null and ? not between mdbegintime5 and '2099-01-01') or( mdbegintime5 is null and mdendtime5 is not null and ? not between '1990-01-01' and mdendtime5) or ( mdbegintime5 is null and mdendtime5 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ");
			sb.append("  and (( mdbegintime6 is not null and mdendtime6 is not null and ? not between mdbegintime6 and mdendtime6) or( mdbegintime6 is not null and mdendtime6 is null and ? not between mdbegintime6 and '2099-01-01') or( mdbegintime6 is null and mdendtime6 is not null and ? not between '1990-01-01' and mdendtime6) or ( mdbegintime6 is null and mdendtime6 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ");
			sb.append("  and (( mdbegintime7 is not null and mdendtime7 is not null and ? not between mdbegintime7 and mdendtime7) or( mdbegintime7 is not null and mdendtime7 is null and ? not between mdbegintime7 and '2099-01-01') or( mdbegintime7 is null and mdendtime7 is not null and ? not between '1990-01-01' and mdendtime7) or ( mdbegintime7 is null and mdendtime7 is null and ? between case when mdbegintime1 is null then '1990-01-01' else mdbegintime1 end and case when mdendtime1 is null then '2099-01-01' else mdendtime1 end)) ");
			sb.append(" ))");
			valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat);
			valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat);
			valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat);
			valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat); valuesList.add(dat);
			valuesList.add(dat); valuesList.add(dat); valuesList.add(dat);
		}
		
		sb.append("ORDER BY vvouchercode ASC");
		
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(), new ListMapMapper());
	}
	/**
	 * 查询微信会员绑定的实体卡信息
	 * @author ZGL
	 * @Date 2015-04-04 16:13:28
	 */
	private final static String queryCardByWechatId = "select c.* from card c,cardtyp t"+
			" where c.typ = t.id and t.wechatstate='N' and c.locked='N'  ";
	@Override
	public List<Map<String, Object>> queryCardByechatid(String pk_group,String openid) {
		StringBuilder sb = new StringBuilder(queryCardByWechatId);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(pk_group)){
//			sb.append(" AND c.pk_group=? ");
//			valuesList.add(pk_group);
		}
		
		if(StringUtils.hasText(openid)){
			sb.append(" AND c.wechatid=? ");
			valuesList.add(openid);
		}
//		else{
//			return new ArrayList<Map<String, Object>>();
//		}
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(), new ListMapMapper());
	}
	/**
	 * 查询会员支付方式
	 * @author ZGL
	 * @Date 2015-04-04 16:13:28
	 */
	private final static String queryCardPaytyp = "select a.* from cboh_paymode_3ch a,cboh_payment_3ch b "+
			" where a.pk_payment = b.pk_id and b.ivalue=32  ";
	@Override
	public List<Map<String, Object>> queryCardPaytyp(String pk_group) {
		StringBuilder sb = new StringBuilder(queryCardPaytyp);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(pk_group)){
//			sb.append(" AND a.pk_group=? ");
//			valuesList.add(pk_group);
		}
		sb.append(" order by a.vcode,a.vname");
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(), new ListMapMapper());
	}
	/**
	 * 保存账单支付方式详情
	 * @author ZGL
	 * @throws Exception 
	 * @Date 2015-04-09 14:18:51
	 */
	@Override
	public int addPayment(Map<String, Object> map) throws Exception {
		int res = 0;
		List<Object> valuesList = new ArrayList<Object>();
		try{
			//保存支付信息
			String insertFolioPayment = "insert into net_foliopayment(pk_foliopayment,resv,vsettlementcode,vsettlementname,visactive," +
						"npayamt,nrefundamt,noveramt,namt,voperator,vpaydate,vpayaccount,nserviceamt,vactmtyp)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String pk_foliopayment = "";
			valuesList.add(CodeHelper.createUUID());//主键
			valuesList.add(map.get("resv"));//账单号
			valuesList.add(map.get("paycode"));//支付方式编码
			valuesList.add(map.get("payname"));//支付方式名称
			valuesList.add("N");//是否活动
			valuesList.add(map.get("billAmt"));//支付的总金额
			valuesList.add("-"+map.get("discAmt"));//优免金额
			valuesList.add("0");//超收
			valuesList.add(map.get("cardAmt"));//实收
			valuesList.add("微会员");//操作员
			valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//支付时间
			valuesList.add("");//付款账号
			valuesList.add("0");//手续费
			valuesList.add("");//活动类型
			System.out.println(insertFolioPayment+"====================");
			for(Object obj : valuesList){
				System.out.println("============"+(ValueCheck.IsEmpty(obj)==true?"":obj));
			}
			res = jdbcTemplate.update(insertFolioPayment, valuesList.toArray());
			//保存折扣信息
			if(ValueCheck.IsNotEmpty(map.get("discAmt")) && Double.parseDouble(map.get("discAmt")+"0")!=0){
				valuesList = new ArrayList<Object>();
				pk_foliopayment = CodeHelper.createUUID();
				valuesList.add(pk_foliopayment);//主键
				valuesList.add(map.get("resv"));//账单号
				valuesList.add(map.get("discpaycode"));//支付方式编码
				valuesList.add(map.get("discpayname"));//支付方式名称
				valuesList.add("N");//是否活动
				valuesList.add(map.get("discAmt"));//支付的总金额
				valuesList.add("-"+map.get("discAmt"));//优免金额
				valuesList.add("0");//超收
				valuesList.add("0");//实收
				valuesList.add("微会员");//操作员
				valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//支付时间
				valuesList.add("");//付款账号
				valuesList.add("0");//手续费
				valuesList.add("");//活动类型
				res = jdbcTemplate.update(insertFolioPayment, valuesList.toArray());
				// 计算账单菜品折扣金额并保存
				map.put("pk_foliopayment", pk_foliopayment);
				insertPubitemDiscAmt(map);
			}
		}catch(Exception e){
			e.printStackTrace();
			wxPayService.cancelPayWithCard(map);
		}
		return res;
	}
	
	/**
	 * 查询该企业定义的电子券基本信息
	 * @author ZGL
	 * @date 2015-04-10 10:30:05
	 */
	@Override
	public List<Map<String, Object>> listCouponBase(String pk_group) {
		String sql = " select a.vcode,a.vname,b.vcode as actmcode,b.vname as actmname from coupon a,cboh_actm_3ch b" +
				" where a.vactmcode = b.vcode and b.enablestate=2  ";
		List<Object> valuesList = new ArrayList<Object>();
		if(ValueCheck.IsNotEmpty(pk_group)){
//			sql += " where b.pk_group = ?";
//			valuesList.add(pk_group);
		}
		sql += " order by a.vcode,a.vname";
		return jdbcTemplate.query(sql,valuesList.toArray(), new ListMapMapper());
	}
	/**
	 * 计算账单菜品折扣金额并保存
	 *  @author ZGL
	 * @throws Exception 
	 *  @date 2015-04-13 18:14:26
	 */
	@Override
	public int insertPubitemDiscAmt(Map<String, Object> map) throws Exception {
		int res = 0;
		try{
			//查询账单菜品明细
			String selectdetails = "select id,totalprice from net_orderdetail where ordersid = ?";
			List<Object>  valuesList = new ArrayList<Object>();
			valuesList.add(map.get("poserial"));
			List<Map<String,Object>> listOrderDetails = jdbcTemplate.query(selectdetails,valuesList.toArray(), new ListMapMapper());
			if(ValueCheck.IsNotEmptyForList(listOrderDetails)){
				valuesList = new ArrayList<Object>();//清空上次执行的参数
				Double discAmt = 0.00,comparedNum=0.00;
				String maxpkId = "";
				//均摊优惠金额，有余数加到菜品金额最大的菜上，有多个菜品金额一样随机选择一个
				for(Map<String,Object> orderDetails : listOrderDetails){
					//存储菜品金额最大的一条记录
					if(comparedNum<Double.parseDouble(orderDetails.get("totalprice")+"")){
						comparedNum = Double.parseDouble(orderDetails.get("totalprice")+"");
						maxpkId = orderDetails.get("id")+"";
					}
					String detaiDisc = WeChatUtil.multipliedNum(WeChatUtil.dividedNum(orderDetails.get("totalprice"),map.get("billAmt"),20),map.get("discAmt"),2);
					orderDetails.put("discAmt", detaiDisc);//将菜品均摊到的折扣金额暂存到listOrderDetails中
					 discAmt = WeChatUtil.stringPlusDouble(discAmt, detaiDisc);
				}
				//如果均摊下来的折扣总额不等于账单折扣金额，算出差值，放到菜品最高的菜上
				if(!map.get("discAmt").equals(discAmt)){
					comparedNum = WeChatUtil.subtractNum(map.get("discAmt"), discAmt);
				}
				String insetOrderPayment = "insert into net_orderpayment(id,resv,pk_orderdetail,pk_foliopayment,amt)";
				String insertColums = "";
				for(Map<String,Object> orderDetails2 : listOrderDetails){
					//查找金额最大的菜品将差值加到折扣金额中
					if(maxpkId.equals(orderDetails2.get("id"))){
						orderDetails2.put("discAmt", WeChatUtil.stringPlusDouble(orderDetails2.get("discAmt"), comparedNum));
					}
					insertColums += ";'"+CodeHelper.createUUID()+"','"+map.get("resv")+"','"+orderDetails2.get("id")+"','"+map.get("pk_foliopayment")+"','"+orderDetails2.get("discAmt")+"'";
				}
				//处理sql的关键字
				if("oracle".equals(Commons.databaseType)){
					insertColums = " select " + insertColums.substring(1).replace(";"," from dual union select ")+" from dual";
				}else{
					insertColums = " values (" + insertColums.substring(1).replace(";"," ),( ")+")";
				}
				 jdbcTemplate.update(insetOrderPayment+insertColums, valuesList.toArray());
				 res = 1;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		return res;
	}
	/**
	 * 查询门店信息
	 * @author ZGL
	 * @date 2015-04-14 10:55:43
	 */
	private final static String getFirmInfoSql = "SELECT * FROM　CBOH_STORE_3CH WHERE PK_STORE= ";
	@Override
	public Map<String,Object> getFirmInfo(String firmid) {
		return jdbcTemplate.queryForMap(getFirmInfoSql+"'"+firmid+"'");
	}
	/**
	 * 查询已下单未支付账单的支付金额
	 * @author ZGL
	 * @date 2015-05-20 13:41:19
	 */
	private final static String getFolioPaymoneySql = "select * from net_orders where ((isfeast='2' and payment='2') or( (isfeast!='2' or payment='0') and state='7')) and  id= ";
	@Override
	public Map<String,Object> getFolioPaymoney(Net_Orders orders) {
		return jdbcTemplate.queryForMap(getFolioPaymoneySql+"'"+orders.getId()+"'");
	}
	/**
	 * 查询账单支付信息
	 * @author ZGL
	 * @date 2015-05-19 13:10:09
	 * 
	 */
	private final static String queryFolioPaymentSql = "SELECT * FROM　NET_FOLIOPAYMENT WHERE RESV=? ";
	@Override
	public List<Map<String, Object>> queryFolioPayment(Net_Orders orders) {
		return jdbcTemplate.query(queryFolioPaymentSql,new Object[] { orders.getResv() }, new ListMapMapper());
		
	}
	/**
	 * 查询发票抬头记录
	 */
	private final static String queryVinvoicetitleSql = "SELECT * FROM　WX_INVOICETITLE WHERE  WECHATID = ? ";
	@Override
	public List<Map<String, Object>> queryVinvoicetitle(Map<String, Object> mapParam) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(queryVinvoicetitleSql);
		valuesList.add(mapParam.get("openid"));
		
		if(ValueCheck.IsNotEmpty(mapParam.get("vinvoicetitle"))){
			sb.append(" AND VINVOICETITLE = ? ");
			valuesList.add(mapParam.get("vinvoicetitle"));
		}
		sb.append(" ORDER BY TS DESC");
		return jdbcTemplate.query(sb.toString(),valuesList.toArray(), new ListMapMapper());
	}
	/**
	 * 插入发票抬头记录
	 */
	private final static String insertVinvoicetitleSql = "insert into wx_invoicetitle(pk_invoicetitle,vinvoicetitle,wechatid,ts) values(?,?,?,?)";
	@Override
	public void addVinvoicetitle(Map<String, Object> mapParam) {
		List<Object> valuesList = new ArrayList<Object>();
		valuesList.add(CodeHelper.createUUID());//主键
		valuesList.add(mapParam.get("vinvoicetitle"));//发票抬头
		valuesList.add(mapParam.get("openid"));//微信id
		valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//时间
		 jdbcTemplate.update(insertVinvoicetitleSql, valuesList.toArray());
	}
	/**
	 * 修改账单应付金额及存储门店返回的账单折扣信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateFolioDisc(JSONObject json) {
		//如果订单主键为空，直接返回
		if(ValueCheck.IsEmpty( json.get("id"))){
			return;
		}
		Double nmoney=0.00;
		String updateFolioSql = "update net_orders set paymoney=?, sumprice=?";
		String addFolioPaymentSql = "insert into net_foliopayment(PK_FOLIOPAYMENT,RESV,VSETTLEMENTCODE,VSETTLEMENTNAME,NPAYAMT,NREFUNDAMT,VPAYDATE,NSETTLEMENTCOUNT) values (?,?,?,?,?,?,?,?)";
		List<Object> valuesList = new ArrayList<Object>();
		 //每次更新前查询一次菜品数据，编码、主键、名称
		 List<Map<String, Object>> listPubitem = queryPubitemFromBOH("",json.get("id").toString(),DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));
		 Map<String,Object> mapPubitem = new HashMap<String,Object>();
		 if(ValueCheck.IsNotEmptyForList(listPubitem)){
			 for(Map<String,Object> map : listPubitem){
				 mapPubitem.put((String) map.get("vcode"), map.get("pk_pubitem")+"'"+map.get("vname"));
			 }
		 }else{
			 //记录日志
			LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("id")+",接收pos返回数据。查询菜谱方案菜品明细为空"+System.getProperty("line.separator"));
			 return;
		 }
		 //每次更新前查询一次附加项数据，编码、主键、名称
		 List<Map<String,Object>> listRedefine = selectRedefine();
		 Map<String,Object> mapRedefine = new HashMap<String,Object>();
		 for(Map<String,Object> map : listRedefine){
			 mapRedefine.put((String) map.get("vcode"), map.get("pk_redefine")+"'"+map.get("vname"));
		 }
		 /*List<Map<String,Object>> listProdAdd = selectProdAdd();
		 for(Map<String,Object> map : listProdAdd){
			 mapRedefine.put((String) map.get("vcode"), map.get("pk_redefine")+"'"+map.get("vname"));
		 }*/

		// 获取订单数据
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("id", json.get("id") + "");
		List<Net_Orders> tempOrderList = bookMealMapper.getOrderMenus(paraMap);
		if(null != tempOrderList && !tempOrderList.isEmpty()) {
			String firmid = tempOrderList.get(0).getFirmid();
			// 获取附加产品门店编码
			List<ProductAdditional> listProdAdd = this.bookMealMapper.getListProductAdditional(null, firmid);
			
			for(ProductAdditional item : listProdAdd) {
				mapRedefine.put(item.getVcode(), item.getPk_prodAdd() + "'" + item.getPubitemName());
			}
		}
		//更新账单明细数据
		List<Object> orderList = (List<Object>)json.get("menuList");
		if(orderList != null && orderList.size()>0){
			//删除订单明细
			String deleteDtl = "DELETE FROM NET_ORDERDETAIL WHERE ORDERSID=?";
			jdbcTemplate.update(deleteDtl, new Object[] { json.get("id") });
			//删除菜品附加项信息
			String deleteRedefine = "DELETE FROM NET_DISHADDITEM WHERE PK_ORDERSID=?";
			jdbcTemplate.update(deleteRedefine, new Object[] { json.get("id") });
			//删除菜品附加产品信息
			String deleteProd = "DELETE FROM NET_DISHPRODADD WHERE PK_ORDERSID=?";
			jdbcTemplate.update(deleteProd, new Object[] { json.get("id") });
			//删除套餐明细信息
			String deletePackageDtl = "DELETE FROM NET_ORDERPACKAGEDETAIL WHERE PK_ORDERID=?";
			jdbcTemplate.update(deletePackageDtl, new Object[] { json.get("id") });
			//组建订单明细参数list及菜品附加项list
			Net_Orders order = new Net_Orders();
			order.setId( json.get("id")+"");
			List<Net_OrderDtl> listOrderDtl =  new ArrayList<Net_OrderDtl>();
			Net_OrderDtl orderDtl = new Net_OrderDtl();
			NetDishAddItem dishAddItem = new NetDishAddItem();
			Map<String, Object> filterMap = new HashMap<String, Object>();
			List<NetDishAddItem> listNetDishAddItem =  new ArrayList<NetDishAddItem>();
			List<NetDishProdAdd> listNetDishProdAdd = new ArrayList<NetDishProdAdd>();
			//套餐列表
			List<Net_OrderPackageDetail> listOrderPackageDtl = new ArrayList<Net_OrderPackageDetail>();
			Net_OrderPackageDetail orderPackageDetail = new Net_OrderPackageDetail();
			for(Object obj : orderList){
				orderDtl = new Net_OrderDtl();
				Map<String,Object> map = (Map<String,Object>)obj;
				 valuesList = new ArrayList<Object>();
				 String orderDtlId=CodeHelper.createUUID();
				 orderDtl.setId(orderDtlId);
				 orderDtl.setFoodsid(mapPubitem.get(map.get("vcode")).toString().split("'")[0]+"");//菜品主键
				 orderDtl.setOrdersid(json.get("id")+"");
				 orderDtl.setRemark("");//备注
				 orderDtl.setFoodnum(map.get("cnt")+"");//点菜数量
				 orderDtl.setFoodzcnt(map.get("zcnt")+"");//赠送数量
				 orderDtl.setTotalprice(map.get("money")+"");//点菜金额
				 orderDtl.setFoodsname(map.get("vname")+"");//菜品名称
				 orderDtl.setIspackage((map.get("ispackage") == null ? "0" : map.get("ispackage")) + "");//是否套餐
				 List<Object> fujiaList = (List<Object>)map.get("fujiaList");
				 double fujiaPrice = 0.0;
				 if(fujiaList != null && fujiaList.size()>0){
					 for(Object fujia : fujiaList){
						Map<String,Object> mapFujia = (Map<String,Object>)fujia;
						String pk_pubItem = mapPubitem.get(map.get("vcode")).toString().split("'")[0] + "";
						if(!mapRedefine.containsKey(mapFujia.get("vcode"))) {
							orderDtl.setRemark(mapFujia.get("vname") + "");
							continue;
						}
						String pk_redefien = mapRedefine.get(mapFujia.get("vcode")).toString().split("'")[0] + "";
						//键值，用于判断是否已存在
						String key = orderDtl.getOrdersid() + "_" + orderDtl.getFoodsid() + "_" + pk_pubItem + "_" + pk_redefien + "_";
						/*if(filterMap.containsKey(key)) {
							continue;
						}*/
						if(mapFujia.get("visaddprod") != null && "1".equals(mapFujia.get("visaddprod").toString())) {
							NetDishProdAdd data = new NetDishProdAdd();
							data.setPk_group("");
							data.setPk_orderDtlId(orderDtlId);
							data.setPk_pubitem(pk_pubItem);
							data.setPk_prodAdd(pk_redefien);
							data.setPk_prodReqAdd("");
							data.setNcount(mapFujia.get("fcount")+"");
							Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
							data.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
							listNetDishProdAdd.add(data);
							filterMap.put(key, data);
							fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
						} else {
							 dishAddItem = new NetDishAddItem();
							 dishAddItem.setPk_group("");
							 dishAddItem.setPk_orderDtlId(orderDtlId);
							 dishAddItem.setPk_pubItem(pk_pubItem);
							 dishAddItem.setPk_redefine(pk_redefien);
							 dishAddItem.setPk_prodcutReqAttAc("");
							 dishAddItem.setNcount(mapFujia.get("fcount")+"");
							 Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
							 dishAddItem.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
							 listNetDishAddItem.add(dishAddItem);
							 filterMap.put(key, dishAddItem);
							 fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
						}
					 }
					 order.setListDishProdAdd(listNetDishProdAdd);
					 order.setListDishAddItem(listNetDishAddItem);
				 }
				 //如果是套餐
				 if("1".equals(map.get("ispackage"))){
					 listOrderPackageDtl = new ArrayList<Net_OrderPackageDetail>();
					 //套餐明细
					 List<Object> tcMenuList = (List<Object>)map.get("menutclist");
					 if(tcMenuList != null && tcMenuList.size()>0){
						 for(Object tcObj : tcMenuList){
							 Map<String,Object> tcmxMap = (Map<String,Object>)tcObj;
							 orderPackageDetail = new Net_OrderPackageDetail();
							 orderPackageDetail.setPk_pubitem(mapPubitem.get(tcmxMap.get("vcode")).toString().split("'")[0]+"");
							 orderPackageDetail.setNcnt(Double.parseDouble(tcmxMap.get("cnt")+""));
							 orderPackageDetail.setNzcnt(Double.parseDouble(tcmxMap.get("zcnt")+""));
							 orderPackageDetail.setNprice(Double.parseDouble(tcmxMap.get("price")+""));
							 orderPackageDetail.setVremark("");
							 //套餐明细附加项、附加产品
							 List<Object> txmxFujiaList = (List<Object>)tcmxMap.get("fujiaList");
							 if(txmxFujiaList != null && txmxFujiaList.size()>0){
								 listNetDishProdAdd = new ArrayList<NetDishProdAdd>();
								 listNetDishAddItem = new ArrayList<NetDishAddItem>();
								 for(Object fujia : txmxFujiaList){
									Map<String,Object> mapFujia = (Map<String,Object>)fujia;
									String pk_pubItem = mapPubitem.get(map.get("vcode")).toString().split("'")[0] + "";
									if(!mapRedefine.containsKey(mapFujia.get("vcode"))) {
										orderPackageDetail.setVremark(mapFujia.get("vname") + "");
										continue;
									}
									String pk_redefien = mapRedefine.get(mapFujia.get("vcode")).toString().split("'")[0] + "";
									//键值，用于判断是否已存在
									String key = orderDtl.getOrdersid() + "_" + orderDtl.getFoodsid() + "_" + pk_pubItem + "_" + pk_redefien + "_";
									if(mapFujia.get("visaddprod") != null && "1".equals(mapFujia.get("visaddprod").toString())) {
										NetDishProdAdd data = new NetDishProdAdd();
										data.setPk_group("");
										data.setPk_orderDtlId(orderDtlId);
										data.setPk_pubitem(pk_pubItem);
										data.setPk_prodAdd(pk_redefien);
										data.setPk_prodReqAdd("");
										data.setNcount(mapFujia.get("fcount")+"");
										Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
										data.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
										listNetDishProdAdd.add(data);
										fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
									} else {
										 dishAddItem = new NetDishAddItem();
										 dishAddItem.setPk_group("");
										 dishAddItem.setPk_orderDtlId(orderDtlId);
										 dishAddItem.setPk_pubItem(pk_pubItem);
										 dishAddItem.setPk_redefine(pk_redefien);
										 dishAddItem.setPk_prodcutReqAttAc("");
										 dishAddItem.setNcount(mapFujia.get("fcount")+"");
										 Double price = Double.parseDouble(mapFujia.get("nprice")+"") / Integer.parseInt(mapFujia.get("fcount")+"");
										 dishAddItem.setNprice(WeChatUtil.formatDoubleLength(Double.toString(price), 2));
										 listNetDishAddItem.add(dishAddItem);
										 fujiaPrice += Double.parseDouble(mapFujia.get("nprice")+"");
									}
								 }
								 orderPackageDetail.setListDishProdAdd(listNetDishProdAdd);
								 orderPackageDetail.setListDishAddItem(listNetDishAddItem);
							 }
							 listOrderPackageDtl.add(orderPackageDetail);
						 }
						 orderDtl.setOrderPackageDetailList(listOrderPackageDtl);
					 }
				 }
				 listOrderDtl.add(orderDtl);
				 //重新计算账单金额
				 nmoney = WeChatUtil.stringPlusDouble(nmoney, map.get("money")) + fujiaPrice;
			}
			order.setListNetOrderDtl(listOrderDtl);
			//插入pos上传的订单明细
			bookMealMapper.saveOrderDtl(order);
		}
		//插入退菜数据
		List<Object> delmenuList = (List<Object>)json.get("delmenuList");
		if(delmenuList != null && delmenuList.size()>0){
			//删除订单退菜信息
			String deleteDtl = "DELETE FROM net_backdishes WHERE orderid=?";
			jdbcTemplate.update(deleteDtl, new Object[] { json.get("id") });
			//组建退菜参数list
			String backdishesSql = "insert into net_backdishes(pk_backdishes,vpcode,vpname,nzcnt,nzmoney,pk_pubitem,orderid,vdesc) values (?,?,?,?,?,?,?,?)";
			for(Object obj : delmenuList){
				Map<String,Object> map = (Map<String,Object>)obj;
				 valuesList = new ArrayList<Object>();
				 valuesList.add(CodeHelper.createUUID());
				 valuesList.add(map.get("vcode"));//菜品编码
				 valuesList.add(map.get("vname"));//菜品名称
				 valuesList.add(map.get("ycnt"));//退菜数量
				 valuesList.add(map.get("ymoney"));//退菜金额
				 valuesList.add(mapPubitem.get(map.get("vcode")).toString().split("'")[0]);//菜品主键
				 valuesList.add(json.get("id"));//订单主键
				 valuesList.add(map.get("vdesc"));//退菜原因
				 jdbcTemplate.update(backdishesSql,valuesList.toArray());
			}
		}
		 //插入折扣信息
		List<Object> paymentList = (List<Object>)json.get("paymentList");
		if(paymentList != null && paymentList.size()>0){
			//删除订单折扣信息
			String deleteDtl = "DELETE FROM NET_FOLIOPAYMENT WHERE RESV=?";
			jdbcTemplate.update(deleteDtl, new Object[] { json.get("resv") });
			for(Object obj : paymentList){
				Map<String,Object> map = (Map<String,Object>)obj;
				 valuesList = new ArrayList<Object>();
				 valuesList.add(CodeHelper.createUUID());
				 valuesList.add(json.get("resv"));//账单号
				 valuesList.add(map.get("vsettlementcode"));//支付方式编码
				 valuesList.add(map.get("vsettlementname"));//支付方式名称
				 valuesList.add(0);//支付金额
				 valuesList.add(map.get("nrefundamt"));//优惠金额
				 valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				 valuesList.add(map.get("vsettlementcount"));//数量
				 //存储门店返回的账单折扣信息
				 jdbcTemplate.update(addFolioPaymentSql, valuesList.toArray());
			}
		}

		//-------------------------修改账单应付金额start----------------------------
		valuesList = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(updateFolioSql);
		valuesList.add(json.get("paymoney"));
		valuesList.add(WeChatUtil.formatDoubleLength(String.valueOf(nmoney),2));
		//add 20151024 为防止pos加减菜后由于价格改变，导致获取微信支付预支付id错误，增加一个随机码
		if("5".equals(json.get("type"))) {
			// 修改商户订单号生成规则
			//String outtradeno = CodeHelper.createUUID();
			String firmCode = "";
			if(null != tempOrderList && !tempOrderList.isEmpty()) {
				firmCode = tempOrderList.get(0).getVcode();
			}
			String outtradeno = firmCode + json.get("resv") + (new java.util.Random().nextInt(9000) + 1000);
			sb.append(", outtradeno=?");
			valuesList.add(outtradeno);
			LogUtil.writeToTxt(LogUtil.BUSINESS, "接受POS返回消息，更新订单【" + json.get("id") + "】的商户订单号为【" + outtradeno + "】" + System.getProperty("line.separator"));
		}
		if("6".equals(json.get("type"))){//如果type=6即是pos结账后发送的消息，此时需要将订单微信中的订单状态改为已结账
			sb.append(",state = '6' ");
		}
		if(ValueCheck.IsNotEmpty(json.get("disc"))){//折扣金额
		}
		sb.append(" where id = '" +json.get("id")+"' and resv = '"+json.get("resv")+"' and state = 7");
		 jdbcTemplate.update(sb.toString(), valuesList.toArray());
		//-------------------------修改账单应付金额end----------------------------
		 
		//记录日志
		LogUtil.writeToTxt(LogUtil.BUSINESS, "mq流水号："+json.get("id")+",接收pos返回数据。数据内容："+json+System.getProperty("line.separator"));
	
	}

	/**
	 * 查询菜品主要信息
	 * @author ZGL
	 * @date 2015-04-10 10:30:05
	 */
	@Override
	public List<Map<String, Object>> selectPubitem() {
		String sql = " select a.pk_pubitem,a.vcode,a.vname from cboh_pubitem_3ch a where a.enablestate=2  order by a.vcode,a.vname";
		return jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
	}
	/**
	 * 查询附加项信息
	 * @return
	 */
	public List<Map<String, Object>> selectRedefine() {
		String sql = " select a.pk_redefine,a.vcode,a.vname from cboh_redefine_3ch a where a.enablestate=2  order by a.vcode,a.vname";
		return jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
	}
	/**
	 * 查询附加产品信息
	 * @return
	 */
	public List<Map<String, Object>> selectProdAdd() {
		String sql = " select a.pk_prodadd as pk_redefine, p.vname as vname, p.vcode as vcode from cboh_pubitem_3ch p, cboh_productadditional_3ch a "
				+ "where a.pk_prodreqadd = p.pk_pubitem and p.enablestate = 2 ";
		return jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
	}
	/**
	 * 查询优惠信息（赠送）
	 * @param orders
	 * @return
	 */
	@Override
	public List<Map<String,Object>> listPreferentialZs(Net_Orders orders){
		String sql = " select a.foodsname,a.foodzcnt, round(a.foodzcnt*(a.TOTALPRICE/a.FOODNUM),2) as nzmoney from net_orderdetail a where ordersid = ?  and a.foodzcnt>0 order by a.foodsname";
		return jdbcTemplate.query(sql,new Object[]{orders.getId()}, new ListMapMapper());
	}
	/**
	 * 查询优惠信息（退菜）
	 * @param orders
	 * @return
	 */
	@Override
	public List<Map<String,Object>> listPreferentialTc(Net_Orders orders){
		String sql = " select a.vpcode,a.vpname,a.nzcnt,a.nzmoney,to_char((a.nzmoney/a.nzcnt),'FM999990.00') as price from net_backdishes a where orderid = ?  order by a.vpcode,a.vpname";
		return jdbcTemplate.query(sql,new Object[]{orders.getId()}, new ListMapMapper());
	}
	/**
	 * 查询优惠信息（折扣）
	 * @param orders
	 * @return
	 */
	@Override
	public List<Map<String,Object>> listPreferentialZk(Net_Orders orders){
		String sql = "SELECT * FROM NET_FOLIOPAYMENT WHERE resv= (select resv from net_orders where id= ?) ORDER BY VSETTLEMENTCODE ";
		return jdbcTemplate.query(sql,new Object[]{orders.getId()}, new ListMapMapper());
	}
	/**
	 * 查询该门店所属方案的菜品编码
	 */
	private final static String queryPubitemFromBOH = "SELECT M.PK_ITEMPRGM FROM CBOH_ITEMPRGM_3CH M,CBOH_ITEMPRGFIRM_3CH F"+
				" WHERE M.PK_ITEMPRGM = F.PK_ITEMPRGM AND M.ENABLESTATE=2 "+
				" AND M.DEDAT >=? AND M.DBDAT <= ? AND F.PK_STORE= ? ORDER BY M.VLEV DESC ";
	public List<Map<String, Object>> queryPubitemFromBOH(String pk_group,String orderid, String dat) {
		String selectFirmid = "select firmid from net_orders where id = '"+orderid+"'";
		List<Map<String, Object>> listOrder = jdbcTemplate.query(selectFirmid,new Object[]{}, new ListMapMapper());
		if(null == listOrder || listOrder.isEmpty()){
			LogUtil.writeToTxt(LogUtil.BUSINESS, "获取门店信息失败，查询sql【" + selectFirmid + "】");
			return null;
		}
		String firmid = listOrder.get(0).get("firmid")+"";
		if(ValueCheck.IsEmpty(firmid)){
			return null;
		}
		StringBuilder sb = new StringBuilder(queryPubitemFromBOH);
		List<Object> valuesList = new ArrayList<Object>();
		
		valuesList.add(dat);
		valuesList.add(dat);
		valuesList.add(firmid);
		//查询菜谱方案按优先级倒序排序
		List<Map<String, Object>> listRes = jdbcTemplate.query(sb.toString(),valuesList.toArray(), new ListMapMapper());
		if(ValueCheck.IsNotEmptyForList(listRes)){
			//取第一个
			Map<String,Object> resMap = listRes.get(0);
//			String sql = "select vitcode as vcode,vname,vpubitem,pk_pubitem from cboh_itemprgd_3ch where pk_itemprgm= '"+resMap.get("pk_itemprgm")+"'"+
//					"union all select  vcode,vname,'' as vpubitem,pk_pubpack as pk_pubitem from cboh_itemprgpackage_3ch where pk_itemprgm= '"+resMap.get("pk_itemprgm")+"'";
			String sql = " select a.vitcode as vcode,a.vname,a.vpubitem,a.pk_pubitem "+
					" from cboh_itemprgd_3ch a "+
					" left join cboh_itemprgfirm_3ch b on a.pk_itemprgm = b.pk_itemprgm "+
					" left join cboh_store_3ch s on b.pk_store = s.pk_store "+
					" where s.pk_store='"+firmid+"' and a.pk_itemprgm='"+resMap.get("pk_itemprgm")+"' "+
					" union all "+
					" select  b.vcode,a.vname,a.npubid as vpubitem,a.pk_pubpack as pk_pubitem  "+
					" from cboh_itemprgpackage_3ch a "+ 
					" left join CBOH_PUBPACKCODE_3CH b on a.pk_pubpack = b.pk_pubpack "+
					" left join cboh_store_3ch s on  b.pk_brand = s.pk_brandid "+
					" where s.pk_store='"+firmid+"' and a.pk_itemprgm='"+resMap.get("pk_itemprgm")+"'";
			LogUtil.writeToTxt(LogUtil.BUSINESS, "查询门店编码sql：" + sql);
			List<Map<String, Object>> result = jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
			
			// 查询门店个性菜谱方案
			sql = "select vitcode as vcode, vname, vpubitem, pk_pubitem from cboh_store_itemprgd_3ch where pk_store = '"
					+ firmid + "'" + " and enablestate = 2";
			result.addAll(jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper()));
			
			return result;
		}
		return null;
	}
	public static void main(String[] args) {
		String str = "{\"id\":\"86859cfa62ba4098ae89cbf30f06535b\",\"resv\":\"15030616321511\",\"paymoney\":\"120\",\"type\":\"5\",\"paymentList\":[{\"vsettlementcode\":\"1001\",\"vsettlementname\":\"88折\",\"nrefundamt\":\"30\"},{\"vsettlementcode\":\"1002\",vsettlementname:\"买二送一\",\"nrefundamt\":\"20\"}]}";
		JSONObject json =JSONObject.fromObject(str);
		System.out.println(json);
	}
	@Override
	public List<Map<String, Object>> listPreferentialOtherInfo(Net_Orders orders) {
		String sql = "select pk_CouponErrMsg,id,vcouponcode,vcouponname,vactcode,verrmsg from wx_CouponErrMsg where id= '"+orders.getId()+"' ";
		return jdbcTemplate.query(sql,new Object[]{}, new ListMapMapper());
	}
	@Override
	public void addCouponErrMsg(String str,String id) {
		String insertCouponErrSql = "insert into wx_CouponErrMsg(pk_CouponErrMsg,id,vcouponcode,vcouponname,vactcode,verrmsg) values(?,?,?,?,?,?)";
		List<Object> valuesList = new ArrayList<Object>();
		if(ValueCheck.IsNotEmpty(str) && ValueCheck.IsNotEmpty(id)){
			//删除原先的错误信息
			String delSql = "delete from wx_CouponErrMsg where id= ? ";
			jdbcTemplate.update(delSql, new Object[] { id});
			//str字符转格式：活动编码,券唯一编码,失败原因;活动编码,券唯一编码,失败原因;...
			String[] errStrs = str.split(";");
			String couponId = "";
			//组建券唯一编码
			for(String str2 : errStrs){
				//str2字符转格式：活动编码,券唯一编码,失败原因
				if(ValueCheck.IsNotEmpty(str2)){
					String[] errStr2 = str2.split(",");
					couponId += ",'"+errStr2[1]+"'";
				}
			}
			//查询电子券信息
			if(ValueCheck.IsNotEmpty(couponId)){
				Map<String,Object> map = new HashMap<String,Object>();
				couponId = couponId.substring(1);
				String selectCoupon = "select code,typdes from voucher where code in ("+couponId+")";
				List<Map<String, Object>> listRes = jdbcTemplate.query(selectCoupon,new Object[]{}, new ListMapMapper());
				if(ValueCheck.IsNotEmptyForList(listRes)){
					for(Map<String,Object> map2 : listRes){
						map.put(map2.get("code")+"", map2.get("typdes"));
					}
					//循环插入表中
					for(String str2 : errStrs){
						//str2字符转格式：活动编码,券唯一编码,失败原因
						if(ValueCheck.IsNotEmpty(str2)){
							String[] errStr2 = str2.split(",");
							valuesList = new ArrayList<Object>();
							valuesList.add(CodeHelper.createUUID());
							valuesList.add(id);
							valuesList.add(errStr2[1]);
							valuesList.add(map.get(errStr2[1]));
							valuesList.add(errStr2[0]);
							valuesList.add(errStr2[2]);
							jdbcTemplate.update(insertCouponErrSql, valuesList.toArray());
						}
					}
				}
			}
		}
		
	}
	//查询菜谱方案优先级最高的那个
	@Override
	public String queryItemprgmFromBOH(String pk_group,String orderid,String firmid, String dat) {
		if(ValueCheck.IsNotEmpty(orderid) && ValueCheck.IsEmpty(firmid)){
			String selectFirmid = "select firmid from net_orders where id = '"+orderid+"'";
			List<Map<String, Object>> listOrder = jdbcTemplate.query(selectFirmid,new Object[]{}, new ListMapMapper());
			if(ValueCheck.IsEmpty(listOrder)){
				return null;
			}
			firmid = listOrder.get(0).get("firmid")+"";
		}
		if(ValueCheck.IsEmpty(firmid)){
			return null;
		}
		StringBuilder sb = new StringBuilder(queryPubitemFromBOH);
		List<Object> valuesList = new ArrayList<Object>();
		
		valuesList.add(dat);
		valuesList.add(dat);
		valuesList.add(firmid);
		//查询菜谱方案按优先级倒序排序
		List<Map<String, Object>> listRes = jdbcTemplate.query(sb.toString(),valuesList.toArray(), new ListMapMapper());
		if(ValueCheck.IsNotEmptyForList(listRes)){
			//取第一个
			Map<String,Object> resMap = listRes.get(0);
			return  resMap.get("pk_itemprgm")+"";
		}
		return null;
	}
	

	/**
	 * 插入支付记录，供对账使用
	 */
	private final static String insertTransactionRecSql = "insert into CBOH_TRANSCATIONREC_3CH(pk_transcationrec,"
			+ "vtranscateid,vbcode,ntotalmoney,voperate_code,dworkdate,vscode,vecode,vename,dctime,vposid,ityp) "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
	@Override
	public void addTransactionRec(Map<String, Object> mapParam) {
		List<Object> valuesList = new ArrayList<Object>();
		valuesList.add(CodeHelper.createUUID());//主键
		valuesList.add(mapParam.get("vtransactionid"));//流水号
		valuesList.add(mapParam.get("poserial"));//账单号
		valuesList.add(mapParam.get("cardAmt"));//金额
		valuesList.add(mapParam.get("1"));//操作类型    0 退款 1 支付成功 2 交易中撤销 3 撤销失败 4 撤销成功 5是授权支付成功 6 微信扫码支付 7 支付宝扫码支付 8 提交支付申请 9 超时人工处理 10 交易失败
		valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"));//营业日
		valuesList.add(mapParam.get("firmid"));//店铺编码
		valuesList.add(mapParam.get(""));//收银员编码
		valuesList.add(mapParam.get(""));//收银员姓名
		valuesList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//操作时间
		valuesList.add(mapParam.get(""));//POS编号
		valuesList.add(mapParam.get("orderType"));//1 支付宝 2 微信
		 jdbcTemplate.update(insertTransactionRecSql, valuesList.toArray());
	}
	
	private final static String deleteGroupRecord = "DELETE FROM WX_GROUP_RECORD WHERE ORDERID=?";
	private final static String addGroupRecord = "INSERT INTO WX_GROUP_RECORD(PK_RECORD,ORDERID,GROUPCODE,COUPONCODE,NDERATENUM,STATE,UPDATETIME) VALUES(?,?,?,?,?,?,SYSDATE)";
	public void addGroupRecord(String orderid,List<Map<String, String>> list) {
		if(StringUtils.hasText(orderid)){
			jdbcTemplate.update(deleteGroupRecord,new Object[]{orderid});
			if(list!=null && !list.isEmpty()){
				//格式化时间
				String sql = addGroupRecord;
				if("mysql".equals(Commons.databaseType)){
					sql = sql.replace("SYSDATE","now()");
				} else if("sqlserver".equals(Commons.databaseType)){
					sql = sql.replace("SYSDATE","getdate()");
				}
				for(Map<String,String> map : list){
					try{
						jdbcTemplate.update(sql,new Object[]{CodeHelper.createUUID(),
								orderid,
								map.get("groupcode"),
								map.get("couponcode"),
								Double.parseDouble(map.get("nDerateNum")),
								map.get("state")});
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private final static String getGroupRecordSql = "SELECT GROUPCODE,COUPONCODE,VNAME AS COUPONNAME,NDERATENUM,STATE FROM WX_GROUP_RECORD,CBOH_COUPON_3CH WHERE WX_GROUP_RECORD.COUPONCODE=CBOH_COUPON_3CH.VCODE AND ISLAST='1' AND ORDERID=?";
	public List<Map<String,Object>> getGroupRecord(String orderid){
		if(StringUtils.hasText(orderid)){
			return jdbcTemplate.query(getGroupRecordSql, new Object[]{orderid},new ListMapMapper());
		}
		return null;
	}
	
	private final static String updateGroupRecordSql = "UPDATE WX_GROUP_RECORD SET ISLAST='2' WHERE ORDERID=?";
	public void updateGroupRecordSql(String orderid){
		if(StringUtils.hasText(orderid)){
			try {
				jdbcTemplate.update(updateGroupRecordSql, new Object[]{orderid});
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据订单id查询使用的活动
	 * @param orderid
	 * @return
	 */
	public List<WxOrderActm> getListOrderActm(String orderid) {
		List<Object> valuesList = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder("");
		sb.append("select pk_orderactm, vordersid, vactmcode, ")
		.append("vvouchercode, ndiscamt, ncnt, vtype ")
		.append("from wx_order_actm ")
		.append("where vordersid=?");
		
		valuesList.add(orderid);
		
		return jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<WxOrderActm>(){
			public WxOrderActm mapRow(ResultSet rs, int i) throws SQLException {
				WxOrderActm data = new WxOrderActm();
				data.setPk_orderactm(rs.getString("pk_orderactm"));
				data.setVordersid(rs.getString("vordersid"));
				data.setVactmcode(rs.getString("vactmcode"));
				data.setVvouchercode(rs.getString("vvouchercode"));
				data.setNdiscamt(rs.getDouble("ndiscamt"));
				data.setNcnt(rs.getInt("ncnt"));
				data.setVtype(rs.getString("vtype"));
				return data;
			}
		});
	}
	
	/**
	 * 保存订单使用的活动
	 * @param orderActm
	 * @return
	 */
	public int saveOrderActm(WxOrderActm orderActm) {
		int res = 0;
		List<Object> valuesList = new ArrayList<Object>();
		try{
			//保存支付信息
			String insertSql = "insert into wx_order_actm(pk_orderactm,vordersid,vactmcode,vvouchercode,ndiscamt,ncnt,vtype)" +
					" values(?,?,?,?,?,?,?)";
			valuesList.add(orderActm.getPk_orderactm());//主键
			valuesList.add(orderActm.getVordersid());//账单号
			valuesList.add(orderActm.getVactmcode());//活动编码
			valuesList.add(orderActm.getVvouchercode());//优惠券编码
			valuesList.add(orderActm.getNdiscamt());//折扣金额
			valuesList.add(orderActm.getNcnt());//数量
			valuesList.add(orderActm.getVtype());//支付类型
			res = jdbcTemplate.update(insertSql, valuesList.toArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 删除订单使用的活动
	 * @param orderActm
	 * @return
	 */
	public int deleteOrderActm(WxOrderActm orderActm) {
		int res = 0;
		String deleteSql = "delete from wx_order_actm where vordersid=?";
		try {
			res = jdbcTemplate.update(deleteSql, new Object[] { orderActm.getVordersid() });
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 保存购买电子券信息
	 * outTradeNo   cardNo会员卡号     state  date  couponcodes所有电子券的id以“,”隔开   couponnums每种电子券的数量以“,”隔开      sumpoint积分   vnum积分卡数量
	 * */
	public void addCouponItem(String outTradeNo, String cardNo,String openid, String pk_group,
			String couponids, String couponnums, String sumpoint,String sumprice, String vnum) {
		String sql = "insert into wx_couponitem(outtradeno,cardno,state,openid,pk_group,cdate,couponids,couponnums,sumpoint,sumprice,vnum)"
			+" values(?,?,?,?,?,?,?,?,?,?,?)";
		List<Object> valueList = new ArrayList<Object>();
		valueList.add(outTradeNo);
		valueList.add(cardNo);
		valueList.add("0");
		valueList.add(openid);
		valueList.add(pk_group);
		valueList.add(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		valueList.add(couponids);
		valueList.add(couponnums);
		valueList.add(sumpoint);
		valueList.add(sumprice);
		valueList.add(vnum);
		jdbcTemplate.update(sql, valueList.toArray());
	}
	/**
	 * 购买电子券成功 修改状态
	 * 
	 * */
	public void updateCouponItem(String outTradeNo) {
		String sql = "update wx_couponitem set state='1' where outtradeno=?";
		jdbcTemplate.update(sql, new Object[]{outTradeNo});
	}
	/**
	 * 查询购买电子券信息
	 * */
	public List<Map<String, Object>> queryWxCouponItem(String outTradeNo) {
		String sql = "select * from wx_couponitem where outtradeno=? order by cdate desc";
		return jdbcTemplate.query(sql, new Object[]{outTradeNo},new ListMapMapper());
	}
}
