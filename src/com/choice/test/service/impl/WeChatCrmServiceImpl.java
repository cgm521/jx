package com.choice.test.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.choice.test.domain.Card;
import com.choice.test.domain.CardRules;
import com.choice.test.domain.CardTyp;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.Voucher;
import com.choice.test.persistence.WeChatCrmMapper;
import com.choice.test.service.CardBarCode;
import com.choice.test.service.CardQrCode;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.GenerateRandom;
import com.choice.test.utils.JdbcConnection;
import com.choice.test.utils.SendSMSByWxt;
import com.choice.test.utils.SendSMSByXieHe;
import com.choice.test.utils.ShareCodeUtil;
import com.choice.test.utils.TestSDKClient;
import com.choice.webService.CRMWebservice;
import com.choice.wechat.domain.myCard.LabelFavorite;
import com.choice.wechat.util.LogUtil;
import com.choice.wechat.util.WeChatUtil;
import com.jianzhou.sdk.BusinessService;

/**
 * 微信CRM
 * @author 孙胜彬
 */
public class WeChatCrmServiceImpl implements WeChatCrmMapper {
	
	private final String FIRST_WECHATNO = "000001";
	private final int REST =Integer.parseInt(Commons.firmId) ;
	private CRMWebservice crmService;
	/**
	 * 获取会员信息
	 * @param queryCardNo 卡号
	 * @param queryName 姓名
	 * @param queryTele 手机号
	 * @return
	 * @throws Exception
	 */
	public List<Card> queryCardWebService(Card condition) throws Exception {
		if((null==condition.getCardNo()||condition.getCardNo().equals(""))
				&&(null==condition.getPasswd()||condition.getPasswd().equals(""))
				&&(null==condition.getName()||condition.getName().equals(""))
				&&(null==condition.getTele()||condition.getTele().equals(""))
				&&(null==condition.getOpenid()||condition.getOpenid().equals(""))
				&&(null==condition.getCardId()||condition.getCardId().equals(""))
				&&(null==condition.getTyp()||condition.getTyp().equals(""))
				&&(null==condition.getMyselfShareCode()||condition.getMyselfShareCode().equals(""))
				&&(null==condition.getOthersShareCode()||condition.getOthersShareCode().equals(""))
				){
			// 必须选择一个查询条件
			return null;
		}
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Card> list = new ArrayList<Card>();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT C.WECHATID,C.CARDID,C.CARDNO,C.NAME,C.TYPDES,C.TELE,C.ZAMT,C.CREDIT,C.TTLFEN, C.CITY, S.VNAME AS FIRMNAME, "
					+ "C.RANNUM,C.PASSWD,C.TYP,C.BAMT,C.ZFEN,C.STATUS,C.GRPNO,C.FRIM,C.BINDATE,C.ENDDATE," +
					"C.BBCNT,C.TRAN,C.YINVOICE,C.INVOICEAMT,C.DISCSTA,C.CHGAMT,CT.DISC,CT.CREDIT AS STA,"
					+ "CT.FENSTA,CT.NAM AS NAM,C.BRIDATE,C.ROOMID AS MAIL,C.SEX,C.ADDR,CT.POS AS RECHARGEONLINE, " +
					"C.MYSELFSHARECODE,C.OTHERSSHARECODE,C.LABEL,C.FAVORITE  "+
					"FROM CARD C,CARDTYP CT,CBOH_STORE_3CH S WHERE C.TYP=CT.ID AND C.REST=S.VCODE(+) ";
			if(StringUtils.hasText(condition.getCardNo())){
				sql += " AND CARDNO = '" + condition.getCardNo() + "'";
			}
			if(StringUtils.hasText(condition.getPasswd())){
				sql += " AND C.PASSWD = '" + condition.getPasswd() + "'";
			}
			if(StringUtils.hasText(condition.getName())){
				sql += " AND NAME LIKE '%" + condition.getName() + "%'";
			}
			if(StringUtils.hasText(condition.getTele())){
				sql += " AND (C.TELE = '" + condition.getTele() + "'"
					+ " OR C.MOBTEL = '" + condition.getTele() + "')";
			}
			if(StringUtils.hasText(condition.getOpenid())){
				sql += " AND WECHATID = '" + condition.getOpenid() + "'";
			}
			if(StringUtils.hasText(condition.getCardId())){
				sql += " AND CARDID = " + condition.getCardId();
			}
			if(StringUtils.hasText(condition.getTyp())) {
				sql += " AND C.TYP ='" + condition.getTyp() + "'";
			}
			if(StringUtils.hasText(condition.getMyselfShareCode())) {
				sql += " AND C.MYSELFSHARECODE ='" + condition.getMyselfShareCode() + "'";
			}
			if(StringUtils.hasText(condition.getOthersShareCode() )) {
				sql += " AND C.OTHERSSHARECODE ='" + condition.getOthersShareCode() + "'";
			}
			LogUtil.writeToTxt("mycard", "查询会员的sql"+sql);
			rs = st.executeQuery(sql);
			while(rs.next()){
				Card card = new Card();
				String cardId = "";
				try {
					cardId = rs.getString("CARDID");
				} catch (Exception e) {
					cardId = "";
				}
				String cardNo = "";
				try {
					cardNo = rs.getString("CARDNO");
				} catch (Exception e) {
					cardNo = "";
				}
				String name = "";
				try {
					name = rs.getString("NAME");
				} catch (Exception e) {
					name = "";
				}
				String typDes = "";
				try {
					typDes = rs.getString("NAM");
				} catch (Exception e) {
					typDes = "";
				}
				String tele = "";
				try {
					tele = rs.getString("TELE");
				} catch (Exception e) {
					tele = "";
				}
				double zAmt = 0.0;
				try {
					zAmt = Double.valueOf(rs.getString("ZAMT"));
				} catch (Exception e) {
					zAmt = 0.0;
				}
				double credit = 0.0;
				try {
					credit = Double.valueOf(rs.getString("CREDIT"));
				} catch (Exception e) {
					credit = 0.0;
				}
				double ttlFen = 0.0;
				try {
					ttlFen = Double.valueOf(rs.getString("TTLFEN"));
				} catch (Exception e) {
					ttlFen = 0.0;
				}
				String rannum = "";
				try {
					rannum = rs.getString("RANNUM");
				} catch (Exception e) {
					rannum = "";
				}
				String frim = "";
				try {
					frim = rs.getString("FRIM");
				} catch (Exception e) {
					frim = "";
				}
				double invoiceamt = 0.0;
				try {
					invoiceamt = rs.getDouble("INVOICEAMT");
				} catch (Exception e) {
					invoiceamt = 0.0;
				}
				String bindate = "";
				try {
					bindate = DateFormat.getStringByDate(rs.getDate("BINDATE"),"yyyy-MM-dd");
				} catch (Exception e) {
					bindate = "";
				}
				String enddate = "";
				try {
					//enddate = rs.getString("ENDDATE");
					enddate = DateFormat.getStringByDate(rs.getDate("ENDDATE"),"yyyy-MM-dd");
				} catch (Exception e) {
					enddate = "";
				}
				String chgAmt = "";
				try {
					chgAmt = rs.getString("CHGAMT");
				} catch (Exception e) {
					chgAmt = "";
				}
				String typ = "";
				try {
					typ = rs.getString("TYP");
				} catch (Exception e) {
					typ = "";
				}
				String bridate = "";
				try {
					Date date = rs.getDate("BRIDATE");
					if(date!=null){
						bridate = DateFormat.getStringByDate(date,"yyyy-MM-dd");
					}
				} catch (Exception e) {
					bridate = "";
				}
				String mail = "";
				try {
					mail = rs.getString("MAIL");
				} catch (Exception e) {
					mail = "";
				}
				String passwd = "";
				try {
					passwd = rs.getString("PASSWD");
				} catch (Exception e) {
					passwd = "";
				}
				String myselfShareCode="";
				try {
					myselfShareCode = rs.getString("MYSELFSHARECODE");
				} catch (Exception e) {
					myselfShareCode = "";
				}
				String othersShareCode="";
				try {
					othersShareCode = rs.getString("OTHERSSHARECODE");
				} catch (Exception e) {
					othersShareCode = "";
				}
				String wechatId="";
				try {
					wechatId = rs.getString("WECHATID");
				} catch (Exception e) {
					wechatId = "";
				}
				String label="";
				try {
					label = rs.getString("label");
				} catch (Exception e) {
					 label="";
				}
				String favorite="";
				try {
					favorite = rs.getString("favorite");
				} catch (Exception e) {
					 favorite="";
				}
				card.setMail(mail);
				card.setBridate(bridate);
				card.setSex(rs.getString("SEX")==null?"男":rs.getString("SEX"));
				card.setAddr(rs.getString("ADDR")==null?"":rs.getString("ADDR"));
				card.setCardId(cardId);
				card.setCardNo(cardNo);
				card.setName(name);
				card.setTyp(typ);
				card.setTypDes(typDes);
				card.setTele(tele);
				card.setzAmt(String.valueOf(zAmt));
				card.setCredit(String.valueOf(credit));
				card.setTtlFen(String.valueOf(ttlFen));
				card.setRunNum(rannum);
				card.setFirmId(frim);
				card.setInvoiceAmt(String.valueOf(invoiceamt));
				card.setQrordr(cardNo+rannum);
				card.setBindate(bindate);
				card.setEnddate(enddate);
				card.setChgAmt(chgAmt);
				card.setPasswd(passwd);
				card.setRechargeOnline(rs.getString("RECHARGEONLINE"));
				card.setCity(rs.getString("CITY"));
				card.setFirmName(rs.getString("FIRMNAME"));
				card.setMyselfShareCode(myselfShareCode);
				card.setOthersShareCode(othersShareCode);
				card.setOpenid(wechatId);
				card.setWechatId(wechatId);
				card.setLabel(label);
				card.setFavorite(favorite);
				//将获得新值的对象放到集合中
				list.add(card);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	/**
	 * 根据卡号查询卡相关信息
	 */
	public Map<String,Object> findInAmtInfo(String cardNo){
		Connection conn = null;
		Map<String,Object> resultMap = new  HashMap<String,Object>();
		
		Statement st = null;
		ResultSet rs = null;

		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT T.*,C.CHGAMT,C.ZAMT,C.ENDDATE,C.SAILEMP,C.CARDID FROM CARDTYP T, CARD C WHERE T.ID = C.TYP ";
			if(null!=cardNo && !cardNo.equals("")){
				sql += "AND C.CARDNO = '"+cardNo+"'";
			}
			rs = st.executeQuery(sql);
			while(rs.next()){
				double maxAmt = 0.0;
				try {
					maxAmt = Double.valueOf(rs.getString("MAXAMT"));
				} catch (Exception e) {
					maxAmt = 0.0;
				}
				int rountNum = 0;
				try {
					rountNum = Integer.valueOf(rs.getString("ROUNTNUM"));
				} catch (Exception e) {
					rountNum = 0;
				}
				double maxcz = 0.0;
				try {
					maxcz = Double.valueOf(rs.getString("MAXCZ"));
				} catch (Exception e) {
					maxcz = 0.0;
				}
				double chgNumAmt = 0.0;
				try {
					chgNumAmt = Double.valueOf(rs.getString("CHGNUMAMT"));
				} catch (Exception e) {
					chgNumAmt = 0.0;
				}
				int chgComId = 0;
				try {
					chgComId = Integer.valueOf(rs.getString("CHGCOMID"));
				} catch (Exception e) {
					chgComId = 0;
				}
				double firstChgAmt = 0.0;
				try {
					firstChgAmt = Double.valueOf(rs.getString("FIRSTCHGAMT"));
				} catch (Exception e) {
					firstChgAmt = 0.0;
				}
				double chgAmt = 0.0;
				try {
					chgAmt = Double.valueOf(rs.getString("CHGAMT"));
				} catch (Exception e) {
					chgAmt = 0.0;
				}
				double chgDisc = 0.0;
				try {
					chgDisc = Double.valueOf(rs.getString("CHGDISC"));
				} catch (Exception e) {
					chgDisc = 0.0;
				}
				double zAmt = 0.0;
				try {
					zAmt = Double.valueOf(rs.getString("ZAMT"));
				} catch (Exception e) {
					zAmt = 0.0;
				}
				Date endDate = new Date();
				try {
					endDate = rs.getDate("ENDDATE");
				} catch (Exception e) {
					endDate = new Date();
				}
				String sailEmp = "";
				try {
					sailEmp = rs.getString("SAILEMP");
				} catch (Exception e) {
					sailEmp = "";
				}
				int cardId = 0;
				try {
					cardId = rs.getInt("CARDID");
				} catch (Exception e) {
					cardId = 0;
				}
				
				resultMap.put("maxAmt", maxAmt);//单次充值最大金额
				resultMap.put("rountNum", rountNum);//小数处理方式0全部舍掉1全部进位2四舍五入3保持原数
				resultMap.put("maxcz", maxcz);//卡允许最大余额
				resultMap.put("chgNumAmt", chgNumAmt);//充值倍数
				resultMap.put("chgComId", chgComId);//充值赠送比例规则ID
				resultMap.put("firstChgAmt", firstChgAmt);//首次充值最低限额
				resultMap.put("chgAmt", chgAmt);//会员卡累计充值金额（用来判断是否是第一次充值）
				resultMap.put("chgDisc", chgDisc);//赠送比例
				resultMap.put("zAmt", zAmt);//卡余额
				resultMap.put("endDate", DateFormat.getStringByDate(endDate, "yyyy-MM-dd HH:mm:ss"));//卡有效期
				resultMap.put("sailEmp", sailEmp);//业务员
				resultMap.put("cardId", cardId);//卡ID
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}
	
	/**
	 * 根据赠送规则查询赠送比例
	 */
	public double findGiftRate(int chgComId,double inAmt,Date date,int firmId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		double cent = 0;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT * FROM COMMAIN WHERE MODID = "+chgComId +" AND "+
					" BAMT<="+inAmt+" AND EAMT>="+inAmt+" AND "+
					" TO_DATE('"+DateFormat.getStringByDate(date, "yyyy-MM-dd")+"','YYYY-MM-DD')>=BDAT  AND TO_DATE('"+DateFormat.getStringByDate(date, "yyyy-MM-dd")+"','YYYY-MM-DD')<=EDAT";
			rs = st.executeQuery(sql);
			if(rs.next()){
				cent = Double.valueOf(rs.getString("CENT"));
				sql = "SELECT * FROM COMINT WHERE MODID="+chgComId+" AND RESTID="+firmId+" AND MID="+rs.getString("ID");
				rs = st.executeQuery(sql);
				if(!rs.next()){
					cent = -2;//该分店没有与充值金额对应的赠送规则
				}
			}else{
				cent = -1;//没有与当前充值金额对应的赠送规则或者与当前充值金额对应的赠送规则已过期！
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cent;
	}
	
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
	public int changeAmt(int cardId,double totalAmt,double zAmt,int empNo,int firmId,String isCredit,String invoiceNo,String isGrpCard,
			double invoiceAmt,double inAmt,double giftAmt,int payMentCode,String payMent,Date dateTime,Date endDate,String empName,String oEmpName){
		Connection conn = null;
		Statement st = null;
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			//存储过程
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call UPDATE_CARD_AMT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			proc.setInt(1, cardId);
			proc.setDouble(2, totalAmt);
			proc.setDouble(3, zAmt);
			proc.setInt(4, empNo);
			proc.setInt(5, firmId);
			proc.setString(6, isCredit);
			proc.setString(7, invoiceNo);
			proc.setString(8, isGrpCard);
			proc.setInt(9, 0);//反充原因
			proc.setDouble(10, invoiceAmt);
			proc.setDouble(11, inAmt);
			proc.setDouble(12, giftAmt);
			proc.setInt(13, payMentCode);
			proc.setString(14, payMent);
			proc.setDate(15, new java.sql.Date(dateTime.getTime()));
			proc.setString(16, "N");
			proc.setDate(17, new java.sql.Date(endDate.getTime()));
			proc.setInt(18, 0);
			proc.setString(19, empName);
			proc.setString(20, oEmpName);
			proc.registerOutParameter(21, Types.NUMERIC);
			proc.execute();
			int pr = proc.getInt(21);
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * 根据卡编码查询需要的信息
	 */
	public Map<String,Object> findCardInfo(String cardNo,int firmId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT * FROM CARD WHERE CARDNO='"+cardNo+"'";
			rs = st.executeQuery(sql);
			if(rs.next()){
				int cardId = 0;
				try{
					cardId = Integer.valueOf(rs.getString("CARDID"));
				}catch(Exception e){
					cardId = 0;
				}
				resultMap.put("cardId", cardId);
			}
			sql = "SELECT * FROM FIRM WHERE FIRMID="+firmId;
			rs = st.executeQuery(sql);
			if(rs.next()){
				String firmDes = "";
				try{
					firmDes = rs.getString("FIRMDES");
				}catch(Exception e){
					firmDes = "";
				}
				resultMap.put("firmDes", firmDes);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}
	
	
	
	/**
	 * 会员卡扣款
	 * @param cardId 卡编号
	 * @param firmId 分店编号
	 * @param amt 扣款金额
	 * @param date 扣款日期
	 * @param invoiceAmt 发票额
	 * @param empName 收银员姓名
	 * @return
	 */
	public int outAmt(int cardId,int firmId,String firmName,double amt,Date date,double invoiceAmt,String empName){
		Connection conn = null;
		Statement st = null;
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			//存储过程
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call INSERT_CARDORDR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			proc.setDouble(1, amt);
			proc.setInt(2, cardId);
			proc.setDate(3, new java.sql.Date(date.getTime()));
			proc.setDouble(4, 0);
			proc.setString(5, firmName);
			proc.setInt(6, 0);
			proc.setString(7, "");
			proc.setInt(8, firmId);
			proc.setDouble(9, 0);
			proc.setInt(10, 0);
			proc.setString(11, "N");
			proc.setDouble(12, 0);
			proc.setDouble(13, invoiceAmt);
			proc.setInt(14, 0);
			proc.setInt(15, 0);
			proc.setString(16, "");
			proc.setString(17, "");
			proc.setDouble(18, 0);
			proc.setString(19, empName);
			proc.registerOutParameter(20, Types.NUMERIC);
			proc.registerOutParameter(21, Types.NUMERIC);
			proc.registerOutParameter(22, Types.NUMERIC);
			proc.registerOutParameter(23, Types.NUMERIC);
			proc.registerOutParameter(24, Types.NUMERIC);
			proc.registerOutParameter(25, Types.NUMERIC);
			proc.registerOutParameter(26, Types.NUMERIC);
			proc.registerOutParameter(27, Types.NUMERIC);
			proc.registerOutParameter(28, Types.NUMERIC);
			proc.registerOutParameter(29, Types.NUMERIC);
			proc.execute();
			int pr = proc.getInt(20);
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 微信新增会员信息
	 * @param name 用户名
	 * @param tele 手机号
	 * @param wechatId 微信号
	 * @return
	 */
	public String addCardWebService(String name,String tele,String wechatId,String typ, String typDes, String chlb, String cardRest, String passwd,String othersShareCode){
		if((null==name||name.equals(""))||(null==tele||tele.equals(""))||(null==wechatId||wechatId.equals(""))){
			// 必须选择一个条件
//				return "-1";
		}else{
			boolean flag = Pattern.matches("^1[3|4|5|7|8]\\d{9}$", tele);
			if(!flag){
				return "-2";
			}
		}
		
		// 密码默认值
		if(!StringUtils.hasText(passwd)) {
			passwd = "123456";
		}
		
		int cardNoLen = 0;
		String beginNo = "";
		String sql = "";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String rest = "";
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			//获取卡类别 是否为微信卡（WECHATSTATE = 'Y'） 的数据，取第一条数据
			sql = "SELECT ID,NAM,BYTE,BEGINNO FROM CARDTYP WHERE WECHATSTATE = 'Y' AND LOCKED='N'";
			if(null != typ && !"".equals(typ)) {
				sql += " AND ID = '" + typ + "'";
			}
			rs = st.executeQuery(sql);
			while(rs.next()){
				typ = rs.getString("ID");
				typDes = rs.getString("NAM");
				cardNoLen = rs.getInt("BYTE");
				beginNo = rs.getString("BEGINNO");
				break;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if((null==typ||typ.equals(""))||(null==typDes||typDes.equals(""))){
			return "-3";//没有微信卡类别，请设置卡类别数据中是否微信卡标志设置为是。
		}
		if(null == beginNo) {
			beginNo = "";
		}
		String cardId = "";
		String cardNo = "";
		List<Card> listCard=new ArrayList<Card>();
		try {
			Card condition = new Card();
			condition.setOpenid(wechatId);
			listCard = queryCardWebService(condition);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(null!=listCard && listCard.size()!=0){
			return listCard.get(0).getCardNo();
		}else{
			try {
				//连接数据库
				conn = new JdbcConnection().getCRMConnection();
				st = conn.createStatement();
				sql = "SELECT CARDNO FROM CARD WHERE 1=1 " ;
				//九毛九暂时不用手机号验证
//				if(null!=name && !name.equals("")){
//					sql += " AND NAME = '"+name+"' ";
//				}
				if(null!=wechatId && !wechatId.equals("")){
					sql += " AND WECHATID = '"+wechatId+"' ";
				}
				if(null!=tele && !tele.equals("")){
					sql += " AND (TELE = '" + tele + "'"
							+ " OR MOBTEL = '" + tele + "')";
				}
				
				rs = st.executeQuery(sql);//先查询当前手机号用户是否已经存在
				
				if(rs.next()){
					cardNo = rs.getString("CARDNO");
					return cardNo;
				}else{
				/*	sql = "SELECT MAX(TO_NUMBER(CARDNO)) AS CARDNO FROM CARD WHERE CARDNO LIKE '" + beginNo + "%'";//查找是微信卡类型的最大的卡号
					if(cardNoLen > 0) {
						sql += " AND LENGTH(CARDNO) = " + cardNoLen;
					}
					try {
						rs = st.executeQuery(sql);
					} catch(Exception ex) {
						sql = "SELECT MAX(CARDNO) AS CARDNO FROM CARD WHERE CARDNO LIKE '" + beginNo + "%'";//查找是微信卡类型的最大的卡号
						if(cardNoLen > 0) {
							sql += " AND LENGTH(CARDNO) = " + cardNoLen;
						}
						rs = st.executeQuery(sql);
					}
					
					String newCardNo = "";//新卡卡号
					if(rs.next()){
						String maxCardNo = rs.getString("CARDNO");
						newCardNo = getMaxCardNo((null==maxCardNo?"0":maxCardNo),cardNoLen,beginNo);
					}*/
					String newCardNo = tele;//新卡卡号.吉祥要求顾客手机号做为卡号
					int newCardId = 0 ;//新卡ID
					sql = "SELECT CARD_ID.NEXTVAL AS CARDID FROM DUAL";
					rs = st.executeQuery(sql);
					if(rs.next()){
						newCardId = rs.getInt("CARDID");
						cardId = Integer.toString(newCardId);
					}
					String myselfShareCode=ShareCodeUtil.toSerialCode(newCardId);
					String yearYx="";//有效期
					sql = "SELECT VL FROM SYSCONFIG WHERE DES = 'YEAR'";
					rs = st.executeQuery(sql);
					while(rs.next()){
						yearYx = rs.getString("VL");
						break;
					}
					yearYx = (yearYx==null||"".equals(yearYx))?"0":yearYx;
					
					String cond = DateFormat.getStringByDate(new Date(), "yyyy/MM/dd");
					String bindate = DateFormat.getStringByDate(new Date(), "yyyy/MM/dd");
					String enddate = DateFormat.getStringByDate(DateFormat.getDateBefore(new Date(), "year", 1, Integer.parseInt(yearYx)), "yyyy/MM/dd");
					
					rest = StringUtils.hasText(cardRest) ? cardRest : String.valueOf(REST);
					sql = "INSERT INTO CARD(CARDID,CARDNO,NAME,REST,TYP,PASSWD,TELE,MOBTEL,WECHATID,TYPDES,CHLB,COND,BINDATE,ENDDATE,STATUS,othersShareCode,myselfShareCode) " +
							"VALUES ("+newCardId+",'"+newCardNo+"','"+name+"',"+ rest
							+","+typ+",'" + passwd + "','"+tele+"','"+tele+"','"+wechatId+"','"+typDes+"','"+chlb
							+"',to_date('"+cond+"','yyyy/MM/dd'),to_date('"+bindate+"','yyyy/MM/dd'),to_date('"+enddate+"','yyyy/MM/dd'),'冻结','"+othersShareCode+"','"+myselfShareCode+"')";
					LogUtil.writeToTxt("mycard", "注册会员是的sql【" + sql + "】");
					st.execute(sql);
					cardNo = newCardNo;
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != st){
						st.close();
					}
					if(null !=conn){
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// 生成二维码和条形码
			generateQRCodeAndBarCode(cardNo, cardId);
			
			// 调用开卡赠送存储过程
			addCardGift(Integer.parseInt(cardId), rest);			 
			return cardNo;
		}
	}
	private void addCardGift(int cardId, String firmCode) {
		Connection conn = null;
		Statement st = null;
		
		LogUtil.writeToTxt(LogUtil.BUSINESS, "调用开卡赠送存储过程，参数：卡ID【" + cardId + "】，门店编码【" + firmCode + "】");
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			//存储过程
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call SP_ADDVOUCHER_WX(?,?,?,?) }");
			proc.setInt(1, cardId);
			proc.setString(2, firmCode);
			proc.setDate(3, new java.sql.Date(DateFormat.getDateByString(DateFormat.getStringByDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime()));
			proc.registerOutParameter(4, Types.NUMERIC);
			proc.execute();
			int pr = proc.getInt(4);
			
			LogUtil.writeToTxt(LogUtil.BUSINESS, "开卡赠送存储过程完成，卡ID【" + cardId + "】，返回结果：【" + pr + "】");
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.writeToTxt(LogUtil.BUSINESS, "开卡赠送存储过程失败，卡ID【" + cardId + "】，失败原因：【" + e.getMessage() + "】");
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 生成会员卡二维码
	 * @param cardNo
	 * @param cardId
	 */
	public void generateQRCodeAndBarCode(String cardNo, String cardId) {
		// 修改会员卡二维码生成规则
		System.out.println("900-cardNo="+cardNo+"--cardId="+cardId);
		String rannum = GenerateRandom.getRanNum(6);
		String cardnum = addRanNum(cardNo, rannum);
		System.out.println("903-rannum="+rannum+"--cardnum="+cardnum);
		String content = encryptCardId(cardId);
		
		String path_qrcode = CodeHelper.QrcodePic + cardnum + ".png";
		System.out.println("907-path_qrcode="+path_qrcode);
		String path_barcode=path_qrcode.replace("qrcode", "barcode");
		CardQrCode handler = new CardQrCode();
		handler.encoderQRCode(content, path_qrcode, "png");
		CardBarCode barCode_handler= new CardBarCode();
		barCode_handler.generateFile(content, path_barcode);//条形码的图片格式，默认为png格式
	}
	
	/**
	 * 对会员卡ID进行加密，用于生成会员二维码
	 * 加密规则：按位转为字符型，左移两位，然后将结果转为16进制。对所有结果拼接
	 * @param cardId
	 * @return
	 */
	private String encryptCardId(String cardId) {
		int length = cardId.length();
		String result = "";
		for(int i = 0; i < length; i ++) {
			char temp = cardId.charAt(i);
			String sr = Integer.toHexString(temp ^ 5);
			result += sr;
		}
		
		return result;
	}
	
	/**
	 * 微信绑定老会员
	 * @param cardNo 卡号
	 * @param wechatId 微信号
	 * @return
	 */
	public String addWXCardWebService(String cardno,String wechatId, String passwd){
		if(((null==wechatId||wechatId.equals(""))||(null==cardno||cardno.equals("")))){
			// 必须选择一个条件
			return "卡号、微信号 不能为空！";
		}
		
		// 密码默认值
		if(!StringUtils.hasText(passwd)) {
			passwd = "123456";
		}
		
		String cardNo = "";
		Connection conn = null;
		Statement st = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "UPDATE CARD SET WECHATID='"+wechatId+"', PASSWD='" + passwd + "' WHERE CARDNO='"+cardno+"'";
			st.execute(sql);
			cardNo = cardno;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 获取卡ID
		String cardId = "";
		ResultSet rs = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT CARDID FROM CARD WHERE CARDNO='"+cardno+"'";
			rs = st.executeQuery(sql);
			if(rs.next()){
				cardId = rs.getString("CARDID");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 生成二维码
		this.generateQRCodeAndBarCode(cardno, cardId);
		
		return cardNo;
	}

	/**
	 * 获取电子卷
	 * @param wechatId 微信号
	 * @return
	 */
	public List<Voucher> getVoucher(String wechatId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Voucher> listVoucher=new ArrayList<Voucher>();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT V.ID AS ID,V.CODE AS CODE,V.TYPDES AS TYPDES,V.REST AS REST,F.FIRMDES AS FIRMDES,V.BDATE AS BDATE," + 
						 "V.EDATE AS EDATE,V.STA AS STA,V.AMT AS AMT,V.ACTCODE AS ACTCODE, P.MEMO AS MEMO, P.ISTYLE AS ISTYLE, " + 
						 "P.DISCRATE AS DISCRATE FROM VOUCHER V "+
						 "LEFT JOIN FIRM F ON F.FIRMID = V.REST "+
						 "LEFT JOIN CARD C ON C.CARDID = V.CARDID "+
						 "LEFT JOIN COUPON P ON P.VCODE = V.TYPID "+
						 "WHERE 1=1 ";
			if(null!=wechatId && !wechatId.equals("")){
				sql += " AND WECHATID = '"+wechatId+"' ";
			}
			sql += " ORDER BY V.EDATE, V.TYPID ";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Voucher voucher=new Voucher();
				voucher.setId(String.valueOf(rs.getInt("ID")));
				voucher.setCode(rs.getString("CODE"));
				voucher.setTypdes(rs.getString("TYPDES"));
				voucher.setRest(rs.getString("REST"));
				voucher.setFirmname(rs.getString("FIRMDES"));
				voucher.setBdate(DateFormat.getStringByDate(rs.getDate("BDATE"), "yyyy-MM-dd"));
				voucher.setEdate(DateFormat.getStringByDate(rs.getDate("EDATE"), "yyyy-MM-dd"));
				voucher.setSta(rs.getString("STA"));
				voucher.setAmt(rs.getString("AMT"));
				voucher.setActmCode(rs.getString("ACTCODE"));
				voucher.setMemo(rs.getString("MEMO"));
				voucher.setIstyle(rs.getInt("ISTYLE"));
				String discrate = "";
				if(StringUtils.hasText(rs.getString("DISCRATE"))) {
					discrate = WeChatUtil.dividedNum(rs.getString("DISCRATE"), 10, 1);
					if(discrate.endsWith("0")) {
						discrate = discrate.substring(0, discrate.length() - 2);
					}
				}
				voucher.setDiscrate(discrate);
				listVoucher.add(voucher);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listVoucher;
	}

	
	/**
	 * 获得做大卡号加1
	 * @param maxCardNo
	 * @param cardNoLen 
	 * @param beginNo 
	 * @return
	 */
	public String getMaxCardNo(String maxCardNo, int cardNoLen, String beginNo){
//		int cardLength = maxCardNo.length();
		//int iMaxCardNo = Integer.valueOf(maxCardNo);
		if(!"0".equals(maxCardNo)) {
			maxCardNo = maxCardNo.substring(beginNo.length());
		}
		if(!StringUtils.hasText(maxCardNo)) {
			maxCardNo = "0";
		}
		long iMaxCardNo = Long.valueOf(maxCardNo);
		String cardNo = (iMaxCardNo+1)+"";
		int preLength = cardNo.length();
//		if(preLength < cardLength){
		cardNoLen = cardNoLen - beginNo.length();
		if(preLength < cardNoLen){//默认卡号长度6位
			for(int i=0;i<cardNoLen-preLength;i++){
//				for(int i=0;i<cardLength-preLength;i++){
				cardNo = "0"+cardNo;
			}
		}
		
		return beginNo + cardNo;
	}
	/**
	 * 会员卡规则注释表
	 * @param wechatId 微信号
	 * @return
	 */
	public List<CardRules> getCardRules(String wechatId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<CardRules> listCardRules=new ArrayList<CardRules>();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT ID, CHGRULES, JIFENRULES, EXCLUSPRIVLE, CARDEXPLAN, CARDTELE, STORE FROM CARDRULES";
			rs = st.executeQuery(sql);
			while(rs.next()){
				CardRules cardRules=new CardRules();
				cardRules.setID(String.valueOf(rs.getInt("ID")));
				cardRules.setChgrules(rs.getString("CHGRULES"));
				cardRules.setJifenrules(rs.getString("JIFENRULES"));
				cardRules.setExclusprivle(rs.getString("EXCLUSPRIVLE"));
				cardRules.setCardexplan(rs.getString("CARDEXPLAN"));
				cardRules.setCardtele(rs.getString("CARDTELE"));
				cardRules.setSTORE(rs.getString("STORE"));
				listCardRules.add(cardRules);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listCardRules;
	}
	/**
	 * 随机码
	 * @param cardNo 卡号
	 * @return
	 */
	public String addRanNum(String cardno,String rannum){
		Connection conn = null;
		Statement st = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "UPDATE CARD SET RANNUMDATE=SYSDATE,RANNUM='"+rannum+"' WHERE CARDNO='"+cardno+"'";
			st.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cardno+rannum;
	}
	/**
	 * 修改手机号
	 * @param cardNo 卡号
	 * @return
	 */
	public String updateTele(String openid, String tele){
		Connection conn = null;
		Statement st = null;
		int i=0;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "UPDATE CARD SET TELE = '"+tele+"'  WHERE WECHATID = '"+openid+"'";
			i=st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i+"";
	}
	
	/**
	 * 充值记录
	 * @param queryCardNo
	 * @param wechatId
	 * @return
	 */
	public List<ChargeRecord> chargeRecordWebService(String queryCardNo, String wechatId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ChargeRecord> listChargeRecord=new ArrayList<ChargeRecord>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			if(null!=wechatId && !wechatId.equals("")){
				sbf.append(" AND C.WECHATID = '"+wechatId+"'");
			}
			String sql = "SELECT C.CARDNO AS CARDNO,"+
					     " CA.TIM      AS TIM,"+
					     " CA.RMBAMT   AS RMBAMT,"+
					     " CA.GIFTAMT  AS GIFTAMT,"+
					     " CA.OPERATER AS OPERATER,"+
					     " CA.PAYMENT  AS PAYMENT"+
					  " FROM CHANGEAMT CA, CARD C"+
					 " WHERE CA.CARDID = C.CARDID AND C.CARDNO = '"+queryCardNo+"'"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				ChargeRecord chargeRecord=new ChargeRecord();
				chargeRecord.setCardno(rs.getString("CARDNO"));
				chargeRecord.setTim(DateFormat.getStringByDate(rs.getTimestamp("TIM"), "yyyy-MM-dd hh:mm:ss"));
				chargeRecord.setRmbamt(rs.getString("RMBAMT"));
				chargeRecord.setGiftamt(rs.getString("GIFTAMT"));
				chargeRecord.setOperater(rs.getString("OPERATER"));
				chargeRecord.setPayment(rs.getString("PAYMENT"));
				listChargeRecord.add(chargeRecord);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listChargeRecord;
	}
	/**
	 * 消费记录
	 * @param queryCardNo
	 * @param wechatId
	 * @return
	 */
	public List<ConsumeRecord> consumeRecordWebService(String queryCardNo, String wechatId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ConsumeRecord> ListConsumeRecord=new ArrayList<ConsumeRecord>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			if(null!=wechatId && !wechatId.equals("")){
				sbf.append(" AND C.WECHATID = '"+wechatId+"'");
			}
			String sql = "SELECT C.CARDNO   AS CARDNO,"+
					     "  CO.TIM     AS TIM,"+
					     "  CO.AMT     AS AMT,"+
					     "  CO.BALAAMT AS BALAAMT,"+
					     "  F.FIRMDES  AS FIRMDES,"+
					     "  CO.QCZAMT  AS QCZAMT,"+
					     "  CO.QMZAMT  AS QMZAMT"+
					 " FROM CARDORDRS CO, CARD C, FIRM F"+
					" WHERE CO.CARDID = C.CARDID"+
					 "  AND CO.REST = F.FIRMID  AND C.CARDNO = '"+queryCardNo+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				ConsumeRecord consumeRecord=new ConsumeRecord();
				consumeRecord.setCardno(rs.getString("CARDNO"));
				consumeRecord.setTim(DateFormat.getStringByDate(rs.getTimestamp("TIM"), "yyyy-MM-dd hh:mm:ss"));
				consumeRecord.setAmt(rs.getString("AMT"));
				consumeRecord.setBalaamt(rs.getString("BALAAMT"));
				consumeRecord.setFirmdes(rs.getString("FIRMDES"));
				consumeRecord.setQczamt(rs.getString("QCZAMT"));
				consumeRecord.setQmzamt(rs.getString("QMZAMT"));
				ListConsumeRecord.add(consumeRecord);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ListConsumeRecord;
	}
	/**
	 * 会员卡类型
	 * @param queryCardNo
	 * @param wechatId
	 * @return
	 */
	public List<CardTyp> getCardTyp(String id){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<CardTyp> listCardTyp=new ArrayList<CardTyp>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			if(null!=id && !id.equals("")){
				sbf.append(" AND ID = "+id+"");
			}
			String sql = "SELECT ID,NAM FROM CARDTYP WHERE WECHATSTATE='Y'"+sbf;
			rs = st.executeQuery(sql);
			while(rs.next()){
				CardTyp cardTyp=new CardTyp();
				cardTyp.setId(rs.getString("ID"));
				cardTyp.setNam(rs.getString("NAM"));
				listCardTyp.add(cardTyp);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listCardTyp;
	}
	/**
	 * 获取短信验证码
	 */
	public String generateRandom(String tele) throws Exception {
		String ranNum = GenerateRandom.getRanNum(6);
		String content = "【" + Commons.vcompanyname + "】" + "尊敬的用户,您本次的验证码为:" + ranNum;
		
		String smsType = Commons.getConfig().getProperty("smsChannelType");
		if("2".equals(smsType)) {
			// 使用谐和平台发送
			SendSMSByXieHe.sendSMS(tele, content);
		} else if("3".equals(smsType)) {
			// 使用网信通平台发送
			SendSMSByWxt.sendSMS(tele, content);
		} else if("9".equals(smsType)) {
			// 使用辰森短信平台发送
			sendSMSbyPlatform(tele, content);
		}else if("11".equals(smsType)){
			String jzname = Commons.getConfig().getProperty("jzusername");;//登录建周的用户名
			String jzpwd = Commons.getConfig().getProperty("jzpwd");;//登录建周的密码
			String jznote = Commons.getConfig().getProperty("jznote");;//建周的签名
			BusinessService bs = new BusinessService();
			bs.setWebService("http://www.jianzhou.sh.cn/JianzhouSMSWSServer/services/BusinessService");
			String ss = "尊敬的用户,您本次的验证码为:" + ranNum+jznote;
			int i = bs.sendBatchMessage(jzname,jzpwd ,tele, ss);
			System.out.println("建周短信平台返回码："+i+"电话："+tele+",ss:"+ss+"---"+jzname+","+jzpwd+","+jznote);
		} else {
			// 使用亿美平台发送
			TestSDKClient.sendSMS(tele, content);
		}
		return ranNum;
	}
	
	/**
	 * 使用辰森短信平台发送验证码
	 * @param tele
	 * @param content
	 */
	public void sendSMSbyPlatform(String tele, String content) {
		Connection conn = null;
		Statement st = null;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "INSERT INTO SENDSMS (SMSID,TELNO,TELMEMO,FLAG) VALUES (SEQ_SENDSMS.nextval, '" + tele + "', '" + content + "', 0)";
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
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
			double invoiceAmt,double inAmt,double giftAmt,String payMentCode,String payMent,String dateTime,String endDate,String empName,String oEmpName){
		Connection conn = null;
		Statement st = null;
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			//存储过程
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call UPDATE_CARD_AMT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			proc.setInt(1, cardId);
			proc.setDouble(2, totalAmt);
			proc.setDouble(3, zAmt);
			proc.setInt(4, Integer.valueOf(empNo));
			proc.setInt(5, Integer.valueOf(firmId));
			proc.setString(6, isCredit);
			proc.setString(7, invoiceNo);
			proc.setString(8, isGrpCard);
			proc.setInt(9, 0);//反充原因
			proc.setDouble(10, invoiceAmt);
			proc.setDouble(11, inAmt);
			proc.setDouble(12, giftAmt);
			proc.setInt(13, Integer.valueOf(payMentCode));
			proc.setString(14, payMent);
			proc.setDate(15, new java.sql.Date(DateFormat.getDateByString(dateTime, "yyyy-MM-dd").getTime()));
			proc.setString(16, "N");
			proc.setDate(17, new java.sql.Date(DateFormat.getDateByString(endDate, "yyyy-MM-dd").getTime()));
			proc.setInt(18, 0);
			proc.setString(19, empName);
			proc.setString(20, oEmpName);
			proc.registerOutParameter(21, Types.NUMERIC);
			proc.execute();
			int pr = proc.getInt(21);
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
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
			String tim, String sft, String posId, int flag, String poserial) {
		Connection conn = null;
		Statement st = null;
		
		LogUtil.writeToTxt("chargeOnLine", "在线充值开始，参数：卡ID【" + cardId + "】，充值金额【" + rmbAmt + "】，赠送金额【" + giftAmt 
				+ "】，流水号【" + poserial + "】，门店编码【" + firmId + "】，支付编码【" + payMentCode + "】，支付方式【" + payMent + "】,推荐人员工号【"+empNo+"】");
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			//存储过程
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call UPDATE_POS_CARD_AMT_NEW(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			proc.setInt(1, cardId);
			proc.setDouble(2, totalAmt);
			proc.setDouble(3, zAmt);
			proc.setInt(4, Integer.parseInt(empNo));
			proc.setString(5, firmId);
			proc.setString(6, invoiceNo);
			proc.setInt(7, 0);//反充原因
			proc.setDouble(8, invoiceAmt);
			proc.setDouble(9, rmbAmt);
			proc.setDouble(10, giftAmt);
			proc.setInt(11, Integer.parseInt(payMentCode));
			proc.setString(12, payMent);
			proc.setDate(13, new java.sql.Date(DateFormat.getDateByString(dateTime, "yyyy-MM-dd").getTime()));
			proc.setString(14, empName);
			proc.setString(15, oEmpName);
			proc.setString(16, tim);
			proc.setString(17, sft);
			proc.setString(18, posId);
			proc.setInt(19, flag);
			proc.setString(20, poserial);
			proc.registerOutParameter(21, Types.NUMERIC);
			proc.execute();
			int pr = proc.getInt(21);
			
			LogUtil.writeToTxt("chargeOnLine", "在线充值完成，卡ID【" + cardId + "】，返回结果：【" + pr + "】");
			
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.writeToTxt("chargeOnLine", "在线充值失败，卡ID【" + cardId + "】，失败原因：【" + e.getMessage() + "】");
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * 查询剩余、获得、支出积分
	 */
	@Override
	public Map<String, Object> getIntegrationRecodeSum(String cardNo,String cardId, String wechatId,String pk_group) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String, Object> integrationRecodeSum=new HashMap<String, Object>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			if(null!=cardNo && !cardNo.equals("")){
				sbf.append(" AND c.cardNo = '"+cardNo+"'");
			}
			if(null!=cardId && !cardId.equals("")){
				sbf.append(" AND c.cardId = "+cardId+"");
			}
			if(null!=wechatId && !wechatId.equals("")){
				sbf.append(" AND c.wechatId = '"+wechatId+"'");
			}
			if(null!=pk_group && !pk_group.equals("")){
//				sbf.append(" AND c.pk_group = '"+pk_group+"'");
			}
			String sql = "select sum(case when a.flag=2 then -a.payfen else 0 end) as outfen,"+
									       " sum(case when a.flag =1 then a.payfen else 0 end) as infen,"+
									       " sum(a.payfen) as syfen ";

			String sqlIn = "select a.operdate,f.vname,'1' as flag,case when a.flag=1 then '消费积分' when a.flag = 0 then '充值积分'  when a.flag = 2 then '充积分' else '' end as rsn,"+
								       " sum(a.Chgfen) as payfen"+
								" from spcardfen a,card c, cboh_store_3ch f "+
						        " where a.cardid = c.cardid and a.rest = f.vcode(+) "+sbf +"group by a.operdate,f.vname,a.flag";
			String sqlOut = "select t.dat as operdate,f.vname,'2' as flag,s.vname as rsn,sum(-t.xffen) as payfen"+
										" from cardordrs t,card c,cboh_shiftsft_3ch s,cboh_store_3ch f"+
										" where t.cardid = c.cardid and t.sft = s.vcode and t.rest = f.vname and t.xffen!=0"+sbf +
										" group by t.dat,f.vname,s.vname"+
										" union "+
										" select p.paydat as operdate,f.vname,'2' as flag,p.invoice as rsn,sum(-p.payfen) as payfen"+
										" from paygift p,card c,cboh_store_3ch f,gift g"+
										" where p.cardid =c.cardid and p.giftid = g.giftid(+) and p.rest = f.vcode(+) "+sbf +
										" group by p.paydat,p.invoice,f.vname";
			rs = st.executeQuery(sql +" from ("+sqlIn +" union " + sqlOut+") a");
			while(rs.next()){
				integrationRecodeSum.put("sy",rs.getString("syfen"));
				integrationRecodeSum.put("in",rs.getString("infen"));
				integrationRecodeSum.put("out",rs.getString("outfen"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return integrationRecodeSum;
	}
	/**
	 * 查询积分明细
	 */
	@Override
	public List<Map<String, Object>> getIntegrationRecodes(String cardNo,
			String cardId, String openid, String pk_group, String type) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Map<String, Object>> listIntegrationRecodes=new ArrayList<Map<String, Object>>();
		StringBuffer sbf=new StringBuffer();
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			if(null!=cardNo && !cardNo.equals("")){
				sbf.append(" AND c.cardNo = '"+cardNo+"'");
			}
			if(null!=cardId && !cardId.equals("")){
				sbf.append(" AND c.cardId = "+cardId+"");
			}
			if(null!=openid && !openid.equals("")){
				sbf.append(" AND c.wechatId = '"+openid+"'");
			}
			if(null!=pk_group && !pk_group.equals("")){
//				sbf.append(" AND c.pk_group = '"+pk_group+"'");
			}
			String sql = "";
			String sqlIn = "select a.operdate,f.vname,case when a.flag=1 then '消费积分' when a.flag = 0 then '充值积分'  when a.flag = 2 then '充积分' else '' end as rsn,"+
								       " sum(a.Chgfen) as payfen"+
								" from spcardfen a,card c,cboh_store_3ch f "+
						        " where a.cardid = c.cardid and a.rest = f.vcode(+) "+sbf +"group by a.operdate,f.vname,a.flag";
			String sqlOut = "select t.dat as operdate,f.vname,s.vname as rsn,sum(-t.xffen) as payfen"+
										" from cardordrs t,card c,cboh_shiftsft_3ch s,cboh_store_3ch f"+
										" where t.cardid = c.cardid and t.sft = s.vcode and t.rest = f.vcode(+) and t.xffen!=0"+sbf +
										" group by t.dat,f.vname,s.vname"+
										" union "+
										" select p.paydat as operdate,f.vname,p.invoice as rsn,sum(-p.payfen) as payfen"+
										" from paygift p,card c,cboh_store_3ch f,gift g"+
										" where p.cardid =c.cardid and p.giftid = g.giftid(+) and p.rest = f.vcode(+) "+sbf +
										" group by p.paydat,p.invoice,f.vname";
			if("in".equals(type)){
				sql = sqlIn;
			}else if("out".equals(type)){
				sql = sqlOut;
			}else{
				sql = sqlIn +" union " + sqlOut;
			}
			sql += " order by operdate desc,vname,rsn";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String, Object> integrationRecode=new HashMap<String, Object>();
				Date dat = DateFormat.getDateByString(rs.getString("operdate"), "yyyy-MM-dd");
				integrationRecode.put("operdate",DateFormat.getStringByDate(dat,  "yyyy-MM-dd"));
				integrationRecode.put("firmdes",rs.getString("vname"));
				integrationRecode.put("rsn",rs.getString("rsn"));
				integrationRecode.put("payfen",rs.getString("payfen"));
				listIntegrationRecodes.add(integrationRecode);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listIntegrationRecodes;
	}
	

	
	/**
	 * 根据会员卡类型及门店编码查询数据是否存在
	 * @param typ
	 * @param rest
	 * @return
	 * @throws Exception
	 */
	public boolean isCardTypRestExist(String typ, String rest) throws Exception {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		int cnt = 0;
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			String sql = "SELECT COUNT(1) AS CNT FROM CARDRTYPEST WHERE TYP='" + typ + "' AND REST='" + rest + "'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				cnt = rs.getInt("CNT");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(cnt > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 查询会员可以选择的所有标签和兴趣爱好
	 * @return
	 */
	@Override
	public List<LabelFavorite> getAllLabelFavorite() {
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		List<LabelFavorite> listLabelFavorite=new ArrayList<LabelFavorite>();
		try {
			conn=new JdbcConnection().getCRMConnection();
			st=conn.createStatement();
			String sql="SELECT SNO,CODE,DES FROM CODEDESC WHERE LOCKED='N' AND CODE IN ('BQ','XQ') ";
			rs=st.executeQuery(sql);
			while(rs.next()){
				LabelFavorite labelFavorite=new LabelFavorite();
				labelFavorite.setSno(rs.getString("SNO"));
				labelFavorite.setCode(rs.getString("CODE"));
				labelFavorite.setDes(rs.getString("DES"));
				listLabelFavorite.add(labelFavorite);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != st){
					st.close();
				}
				if(null !=conn){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listLabelFavorite;
	}
}
