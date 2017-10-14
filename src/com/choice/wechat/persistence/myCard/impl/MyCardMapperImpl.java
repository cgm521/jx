package com.choice.wechat.persistence.myCard.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.test.domain.Card;
import com.choice.test.domain.CardTyp;
import com.choice.test.domain.ChargeRecord;
import com.choice.test.domain.ConsumeRecord;
import com.choice.test.domain.Voucher;
import com.choice.test.utils.CodeHelper;
import com.choice.test.utils.Commons;
import com.choice.test.utils.DateFormat;
import com.choice.test.utils.JdbcConnection;
import com.choice.wechat.domain.bookDesk.Firm;
import com.choice.wechat.domain.myCard.Coupon;
import com.choice.wechat.domain.myCard.MemberRight;
import com.choice.wechat.domain.myCard.NetGoodsOrders;
import com.choice.wechat.persistence.myCard.MyCardMapper;
import com.choice.wechat.domain.myCard.NetGoodsOrderDtl;

@Repository
public class MyCardMapperImpl implements MyCardMapper {

	@Autowired
	private JdbcTemplate jdbcTemplateCrm;

	public List<ChargeRecord> listChargeRecord(ChargeRecord record) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ChargeRecord> listChargeRecord = new ArrayList<ChargeRecord>();
		StringBuffer sbf = new StringBuffer();
		try {
			// 连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			if (null != record.getOpenid() && !"".equals(record.getOpenid())) {
				sbf.append(" AND C.WECHATID = '" + record.getOpenid() + "'");
			}
			if (null != record.getTim() && !"".equals(record.getTim())) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR,
						Integer.parseInt(record.getTim().substring(0, 4)));
				cal.set(Calendar.MONTH,
						Integer.parseInt(record.getTim().substring(5, 7)));
				cal.set(Calendar.DATE, 1);
				cal.add(Calendar.DATE, -1);

				sbf.append(" AND CA.TIM >= TO_DATE('")
						.append(record.getTim() + "-01")
						.append("', 'yyyy-MM-dd')")
						.append(" AND CA.TIM <= TO_DATE('")
						.append(DateFormat.getStringByDate(cal.getTime(),
								"yyyy-MM-dd 23:59:59")).append("', 'yyyy-MM-dd hh24:mi:ss')");
			}
			String sql = "SELECT C.CARDNO AS CARDNO," + " CA.TIM      AS TIM,"
					+ " TO_CHAR(CA.RMBAMT,'FM999990.00')   AS RMBAMT," + " TO_CHAR(CA.GIFTAMT,'FM999990.00')  AS GIFTAMT,"
					+ " CA.OPERATER AS OPERATER," + " CA.PAYMENT  AS PAYMENT,"
					+ " F.FIRMDES   AS FIRMDES" + " FROM CHANGEAMT CA"
					+ " JOIN CARD C ON CA.CARDID = C.CARDID"
					+ " LEFT JOIN FIRM F ON CA.REST = F.FIRMID"
					+ " WHERE C.CARDNO = '" + record.getCardno() + "'" + sbf
					+ " ORDER BY CA.TIM DESC";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ChargeRecord chargeRecord = new ChargeRecord();
				chargeRecord.setCardno(rs.getString("CARDNO"));
				// chargeRecord.setTim(DateFormat.getStringByDate(rs.getTimestamp("TIM"),
				// "yyyy-MM-dd hh:mm:ss"));
				chargeRecord.setTim(DateFormat.getStringByDate(
						rs.getTimestamp("TIM"), "yyyy-MM-dd HH:mm:ss"));
				chargeRecord.setRmbamt(rs.getString("RMBAMT"));
				chargeRecord.setGiftamt(rs.getString("GIFTAMT"));
				chargeRecord.setOperater(rs.getString("OPERATER"));
				chargeRecord.setPayment(rs.getString("PAYMENT"));
				chargeRecord.setFirmdes(rs.getString("FIRMDES"));
				listChargeRecord.add(chargeRecord);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != st) {
					st.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listChargeRecord;
	}

	/**
	 * 查询消费记录
	 * 
	 * @param record
	 * @return
	 */
	public List<ConsumeRecord> listConsumeRecord(ConsumeRecord record) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<ConsumeRecord> ListConsumeRecord = new ArrayList<ConsumeRecord>();
		StringBuffer sbf = new StringBuffer();
		try {
			// 连接数据库
			conn = new JdbcConnection().getCRMConnection();
			st = conn.createStatement();
			if (null != record.getOpenid() && !"".equals(record.getOpenid())) {
				sbf.append(" AND C.WECHATID = '" + record.getOpenid() + "'");
			}
			if (null != record.getTim() && !"".equals(record.getTim())) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR,
						Integer.parseInt(record.getTim().substring(0, 4)));
				cal.set(Calendar.MONTH,
						Integer.parseInt(record.getTim().substring(5, 7)));
				cal.set(Calendar.DATE, 1);
				cal.add(Calendar.DATE, -1);

				sbf.append(" AND CO.TIM >= TO_DATE('")
						.append(record.getTim() + "-01")
						.append("', 'yyyy-MM-dd')")
						.append(" AND CO.TIM <= TO_DATE('")
						.append(DateFormat.getStringByDate(cal.getTime(),
								"yyyy-MM-dd 23:59:59")).append("', 'yyyy-MM-dd hh24:mi:ss')");
			}
			String sql = "SELECT C.CARDNO   AS CARDNO,"
					+ "  CO.TIM     AS TIM," + "  TO_CHAR(CO.AMT,'FM999990.00')     AS AMT,"
					+ "  TO_CHAR(CO.BALAAMT,'FM999990.00') AS BALAAMT," + "  F.FIRMDES  AS FIRMDES,"
					+ "  TO_CHAR(CO.QCZAMT,'FM999990.00')  AS QCZAMT," + "  TO_CHAR(CO.QMZAMT,'FM999990.00')  AS QMZAMT"
					+ " FROM CARDORDRS CO, CARD C, FIRM F"
					+ " WHERE CO.CARDID = C.CARDID"
					+ "  AND CO.REST = F.FIRMID  AND C.CARDNO = '"
					+ record.getCardno() + "'"
					+ sbf.toString()
					+ " ORDER BY CO.TIM DESC";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ConsumeRecord consumeRecord = new ConsumeRecord();
				consumeRecord.setCardno(rs.getString("CARDNO"));
				// consumeRecord.setTim(DateFormat.getStringByDate(rs.getTimestamp("TIM"),
				// "yyyy-MM-dd hh:mm:ss"));
				consumeRecord.setTim(DateFormat.getStringByDate(
						rs.getTimestamp("TIM"), "yyyy-MM-dd HH:mm:ss"));
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
		} finally {
			try {
				if (null != st) {
					st.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ListConsumeRecord;
	}
	
	/**
	 * 计算赠送金额
	 * @param cardNo
	 * @param chargeDate
	 * @param chargeAmt
	 * @return
	 */
	public Double calcGiftAmt(String cardNo, String chargeDate, double chargeAmt) {
		Connection conn = null;
		Statement st = null;
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			//存储过程
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call SP_CALC_GIFTAMT(?,?,?,?,?,?) }");
			proc.setInt(1, Integer.parseInt(cardNo));
			proc.setString(2, Commons.getConfig().getProperty("firmId"));
			proc.setDate(3, new java.sql.Date(DateFormat.getDateByString(chargeDate, "yyyy-MM-dd HH:mm:ss").getTime()));
			proc.setDouble(4, chargeAmt);
			proc.setString(5, "");
			proc.registerOutParameter(6, Types.NUMERIC);
			proc.execute();
			double giftAmt = 0;
			if(proc.getDouble(6)>0){//判断返回值，小于0时不参与计算
				giftAmt = proc.getDouble(6);
			}
			return giftAmt;
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
		return 0.0;
	}
	
	/**
	 * 获取电子券列表
	 * @param pk_group
	 * @return
	 */
	public List<Coupon> getCouponList(String pk_group) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT C.PK_COUPON AS PK_ID, '' AS PK_GROUP, C.VCODE AS VCODE, C.VNAME AS VNAME, ")
		.append("C.NMONEY AS NMONEY, C.DISCRATE AS DISCRATE, C.DISCAMT AS DISCAMT, C.NEEDFEN AS NEEDFEN, ")
		.append("C.NMONEY AS NPRICE, C.NEEDFEN AS NFEN, C.PK_COUPONTYP AS VTYPE, '' AS VPIC, C.VNAME AS VMEMO, ")
		.append("C.VALIDMONTH AS VALIDMONTH, C.VACTMCODE AS VACTMCODE, '' AS TS, '' AS DR ")
		.append("FROM COUPON C ")
		.append("WHERE C.IFSELLWX = 1 ");

		sb.append("ORDER BY VCODE ASC");

		return jdbcTemplateCrm.query(sb.toString(), new Coupon());
	}
	
	/**
	 * 获取活动限制门店
	 * @param pk_group
	 * @param compType
	 * @return
	 */
	public List<Firm> getLimitFirmList(String pk_group, String compType) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT STR.PK_STORE AS FIRMID, S.VNAME AS FIRMDES, A.VCODE AS FIRMCODE ")
				.append("FROM CBOH_ACTM_3CH A, CBOH_ACTSTR_3CH STR, CBOH_STORE_3CH S ")
				.append("WHERE A.PK_ACTM = STR.PK_ACTM ")
				.append("AND STR.PK_STORE = S.PK_STORE ");
		
		List<Object> valuesList = new ArrayList<Object>();

		if (StringUtils.hasText(pk_group)) {
			sb.append(" AND T.PK_GROUP = ? ");
			valuesList.add(pk_group);
		}

		if (StringUtils.hasText(compType)) {
			sb.append(" AND A.VCODE = ? ");
			valuesList.add(compType);
		}
		
		sb.append("ORDER BY S.VNAME ");

		return jdbcTemplateCrm.query(sb.toString(), valuesList.toArray(), 
				new RowMapper<Firm>(){
			public Firm mapRow(ResultSet rs, int i) throws SQLException {
				Firm f = new Firm();
				f.setFirmid(rs.getString("FIRMID"));
				f.setFirmdes(rs.getString("FIRMDES"));
				f.setFirmCode(rs.getString("FIRMCODE"));
				
				return f;
			}
		});
	}
	
	/**
	 * 保存商品订单
	 * @param order
	 * @return
	 */
	public String saveGoodsOrder(NetGoodsOrders order) {
		StringBuilder orderSql = new StringBuilder();
		String orderid = CodeHelper.createUUID();
		orderSql.append("INSERT INTO NET_GOODS_ORDERS(ID, RESV, SUMPRICE, SUMPOINT, PAYMONEY, PAYPOINT, ORDERTIME, ")
		.append("STATE, PAYMENTTYPE, OPENID, PK_GROUP) VALUES (?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?)");
		
		List<Object> orderValuse = new ArrayList<Object>();
		orderValuse.add(orderid);
		String resv = DateFormat.getStringByDate(new Date(), "yyMMddHHmmss")
				+ (new java.util.Random().nextInt(90) + 10);
		orderValuse.add(resv);
		orderValuse.add(order.getSumPrice());
		orderValuse.add(order.getSumPoint());
		orderValuse.add(order.getSumPrice());
		orderValuse.add(order.getSumPoint());
		orderValuse.add(order.getState());
		orderValuse.add(order.getPaymentType());
		orderValuse.add(order.getOpenid());
		orderValuse.add(order.getPk_group());
		
		int cnt = jdbcTemplateCrm.update(orderSql.toString(), orderValuse.toArray());
		
		if (cnt > 0) {
			for(NetGoodsOrderDtl dtl : order.getListNetGoodsOrderDtl()) {
				StringBuilder dtlSql = new StringBuilder();
				dtlSql.append("INSERT INTO NET_GOODS_ORDERDETAIL(ID, ORDERSID, GOODSID, GOODSNUM, PRICE, ")
				.append("TOTALPRICE, GOODSNAME, PAYTYPE, REMARK) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				List<Object> valuesList = new ArrayList<Object>();
				valuesList.add(CodeHelper.createUUID());
				valuesList.add(orderid);
				valuesList.add(dtl.getGoodsid());
				valuesList.add(dtl.getGoodsnum());
				valuesList.add(dtl.getPrice());
				valuesList.add(dtl.getTotalprice());
				valuesList.add(dtl.getGoodsname());
				valuesList.add(dtl.getPayType());
				valuesList.add(dtl.getRemark());
				
				cnt = jdbcTemplateCrm.update(dtlSql.toString(), valuesList.toArray());
			}
		}
		
		return cnt + "";
	}

	private final static String getCountOfRowsSql = "SELECT COUNT(PK_ID) AS VALUE FROM　WX_CARDINFO WHERE UPDATETYPE='0' ";
	public Integer getCountOfRows(String cardNo, String openid) {
		int count = 99999;
		if(StringUtils.hasText(cardNo) || StringUtils.hasText(openid)){
			StringBuilder sb = new StringBuilder(getCountOfRowsSql);
			List<Object> params = new ArrayList<Object>();
			if(StringUtils.hasText(cardNo)){
				sb.append(" AND CARDNO=? ");
				params.add(cardNo);
			}
			
			if(StringUtils.hasText(openid)){
				sb.append(" AND OPENID=? ");
				params.add(openid);
			}
			
			count = jdbcTemplateCrm.queryForObject(sb.toString(), params.toArray(), Integer.class);
		}
		return count;
	}

	private final static String addCardUpdateSql = "INSERT INTO WX_CARDINFO(PK_ID,CARDNO,OPENID,UPDATETIME) VALUES(?,?,?,SYSDATE)";
	public void addCardUpdate(String pk_id, String cardNo, String openid) {
		jdbcTemplateCrm.update(addCardUpdateSql, new Object[]{pk_id,cardNo,openid});
	}
	
	/**
	 * 获取会员卡类型列表
	 * @return
	 */
	public List<CardTyp> getCardTypList() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID, NAM, BYTE FROM CARDTYP WHERE WECHATSTATE = 'Y' AND LOCKED='N' ORDER BY ID");
		
		return jdbcTemplateCrm.query(sb.toString(), new RowMapper<CardTyp>(){
			public CardTyp mapRow(ResultSet rs, int i) throws SQLException {
				CardTyp card = new CardTyp();
				card.setId(rs.getString("ID"));
				card.setNam(rs.getString("NAM"));
				
				return card;
			}
		});
	}

	/**
	 * 根据条件查询电子卷
	 * @param voucher
	 * @return
	 */
	public List<Voucher> getVoucherByCondition(Voucher voucher){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT V.ID AS ID,V.CODE AS CODE,V.TYPDES AS TYPDES,V.REST AS REST,F.FIRMDES AS FIRMDES, P.MEMO AS MEMO,")
		.append("V.BDATE AS BDATE,V.EDATE AS EDATE,V.STA AS STA,V.AMT AS AMT,V.ACTCODE AS ACTCODE, V.CARDID AS CARDID ")
		.append("FROM VOUCHER V ")
		.append("LEFT JOIN FIRM F ON F.FIRMID=V.REST ")
		.append("LEFT JOIN CARD C ON C.CARDID=V.CARDID ")
		.append("LEFT JOIN COUPON P ON P.VCODE = V.TYPID ")
		.append("WHERE 1=1 ");
		
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(voucher.getId())){
			sb.append("AND V.ID = ? ");
			valuesList.add(voucher.getId());
		}
		
		return jdbcTemplateCrm.query(sb.toString(), valuesList.toArray(), 
				new RowMapper<Voucher>(){
			public Voucher mapRow(ResultSet rs, int i) throws SQLException {
				Voucher v = new Voucher();
				v.setId(String.valueOf(rs.getInt("ID")));
				v.setCode(rs.getString("CODE"));
				v.setTypdes(rs.getString("TYPDES"));
				v.setRest(String.valueOf(rs.getInt("REST")));
				v.setFirmname(rs.getString("FIRMDES"));
				v.setBdate(DateFormat.getStringByDate(rs.getDate("BDATE"), "yyyy-MM-dd"));
				v.setEdate(DateFormat.getStringByDate(rs.getDate("EDATE"), "yyyy-MM-dd"));
				v.setSta(rs.getString("STA"));
				v.setAmt(rs.getString("AMT"));
				v.setActmCode(rs.getString("ACTCODE"));
				v.setCardId(rs.getString("CARDID"));
				v.setMemo(rs.getString("MEMO"));
				
				return v;
			}
		});
	}
	
	/**
	 * 
	 * @param voucher
	 * @return
	 */
	public int updateCardIdOfVoucher(Voucher voucher) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE VOUCHER SET CARDID=")
		.append(voucher.getCardId())
		.append(" WHERE ID=")
		.append(voucher.getId());
		
		return jdbcTemplateCrm.update(sb.toString());
	}
	
	/**
	 * 根据卡类型获取会员特权列表
	 * @param cardType
	 * @return
	 */
	public List<MemberRight> getMemberRightList(String cardType) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM CARDTYP WHERE 1=1 ");
		
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(cardType)){
			sb.append(" AND ID = ?");
			valuesList.add(cardType);
		}
		
		return jdbcTemplateCrm.query(sb.toString(), valuesList.toArray(), 
				new RowMapper<MemberRight>() {
			public MemberRight mapRow(ResultSet rs, int i) throws SQLException {
				MemberRight f = new MemberRight();
				f.setId(rs.getString("ID"));
				//f.setPk_group(rs.getString("PK_GROUP"));
				//f.setCardType(rs.getString("CARDTYPE"));
				String s = rs.getString("MEMO");
				s = s.replaceAll("\r\n", "<br />");
				s = s.replaceAll("\n\r", "<br />");
				s = s.replaceAll("\n", "<br />");
				s = s.replaceAll("\r", "<br />");
				f.setMemberRight(s);
				//f.setTs(rs.getString("TS"));
				//f.setDr(rs.getInt("DR"));
				return f;
			}
		});
	}
	
	/**
	 * 更新会员信息
	 * @param card
	 * @return
	 */
	public int updateCardInfo(Card card) {
		List<Object> valuesList = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("UPDATE CARD SET ");
		
		if(StringUtils.hasText(card.getCity())) {
			sb.append(" CITY=? ");
			valuesList.add(card.getCity());
		}
		
		if(StringUtils.hasText(card.getCity()) && StringUtils.hasText(card.getRest())) {
			sb.append(", ");
		}
		
		if(StringUtils.hasText(card.getRest())) {
			sb.append(" REST=? ");
			valuesList.add(card.getRest());
		}
		
		//返回结果
		int result = 0;
		if(StringUtils.hasText(card.getCardNo())) {
			sb.append(" WHERE CARDNO=? ");
			valuesList.add(card.getCardNo());
			
			result = jdbcTemplateCrm.update(sb.toString(), valuesList.toArray());
		}
		
		return result;
	}
	
	/**
	 * 调用存储过程扣减积分
	 * @param cardId
	 * @param pxfJf
	 * @param pPosserial
	 * @param pRest
	 * @param pEmp
	 * @param vCnt
	 * @return
	 */
	public int callCostFen(int cardId, float pxfJf, String pPosserial, String pRest, int pEmp, float vCnt) {
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter(Types.INTEGER));
		params.add(new SqlParameter(Types.FLOAT));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.DATE));
		params.add(new SqlParameter(Types.INTEGER));
		params.add(new SqlParameter(Types.INTEGER));
		params.add(new SqlParameter(Types.INTEGER));
		params.add(new SqlParameter(Types.FLOAT));
		params.add(new SqlParameter(Types.VARCHAR));
		params.add(new SqlParameter(Types.INTEGER));
		
		final int cardid = cardId;
		final float pxfjf = pxfJf;
		final String pposserial = pPosserial;
		final String prest = pRest;
		final int pemp = pEmp;
		final float vcnt = vCnt;

		Map<String, Object> outValues = jdbcTemplateCrm.call(
				new CallableStatementCreator() {
					@Override
					public CallableStatement createCallableStatement(Connection conn) throws SQLException {
						CallableStatement cstmt = conn.prepareCall("{ call SP_COSTFEN(?,?,?,?,?,?,?,?,?,?) }");
						cstmt.setInt(1, cardid);
						cstmt.setFloat(2, pxfjf);
						cstmt.setString(3, pposserial);
						cstmt.setString(4, prest);
						cstmt.setDate(5, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
						cstmt.setInt(6, pemp);
						cstmt.setInt(7, 0);
						cstmt.setInt(8, -1);
						cstmt.setFloat(9, vcnt);
						cstmt.setString(10, "购买电子券");
						cstmt.registerOutParameter(11, Types.INTEGER);
						return cstmt;
					}
				}, params);

		return 1;
	}

	/**
	 * 保存积分支出记录
	 * @param cardId
	 * @param pxfJf
	 * @param vCnt
	 * @return
	 */
	private final static String addCostFenSql = "INSERT INTO PAYGIFT(ID,CARDID,REST,INPUTBY,GIFTID,INVOICE,PAYDAT,PAYFEN,CNT,TIM) "
			+ "VALUES (PAYGIFT_ID.NEXTVAL,?,?,?,?,?,TO_DATE(?, 'yyyy-MM-dd'),?,?,SYSDATE)";
	public int addCostFen(int cardId, float pxfJf, float vCnt) {
		return jdbcTemplateCrm.update(addCostFenSql, new Object[]{cardId, Commons.firmId, 99999, -1, "购买电子券", 
				DateFormat.getStringByDate(Calendar.getInstance().getTime(), "yyyy-MM-dd"), pxfJf, vCnt});
	}
	
	/**
	 * 验证会员卡密码是否正确
	 * @param cardId
	 * @param passwd
	 * @return
	 */
	public boolean verifyPassword(String cardId, String passwd) {
		List<Object> valuesList = new ArrayList<Object>();
		String sql = "SELECT COUNT(1) FROM CARD WHERE CARDID = ? AND PASSWD = ?";
		valuesList.add(Integer.parseInt(cardId));
		valuesList.add(passwd);
		
		int cnt =  jdbcTemplateCrm.queryForInt(sql, valuesList.toArray());
		
		return cnt > 0 ? true : false;
	}
	
	/**
	 * 修改密码
	 * @param cardId
	 * @param passwd
	 * @return
	 */
	public int setPassword(String cardId, String passwd) {
		List<Object> valuesList = new ArrayList<Object>();
		String sql = "UPDATE CARD SET PASSWD = ? WHERE CARDID = ? ";
		valuesList.add(passwd);
		valuesList.add(Integer.parseInt(cardId));
		
		return jdbcTemplateCrm.update(sql, valuesList.toArray());
	}
	
	/**
	 * 调用赠券存储过程
	 * @param cardId
	 * @param totalAmt
	 * @param firmId
	 * @param posserial
	 */
	public void callGiftVoucher(int cardId, double totalAmt, String firmId, String posserial){
		Connection conn = null;
		Statement st = null;
		
		try {
			//连接数据库
			conn = new JdbcConnection().getCRMConnection();
			//存储过程
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call SP_GIFTVOUCHER(?,?,?,?,?) }");
			proc.setInt(1, cardId);
			proc.setDouble(2, totalAmt);
			proc.setString(3, firmId);
			proc.setDate(4, new java.sql.Date(new Date().getTime()));
			proc.setString(5, posserial);
			proc.execute();
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
	 * 根据openid判断是否是会员
	 * @param openid
	 * @return String Y/N
	 * */
	@Override
	public String isVipofOpenid(String openid) {
		String sql = "select count(1) from card where status='有效' and wechatid=?";
		int cnt =  jdbcTemplateCrm.queryForInt(sql,openid);
		if(cnt>0){
			return "Y";
		}
		return "N";
	}
}
