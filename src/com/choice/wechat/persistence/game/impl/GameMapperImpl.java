package com.choice.wechat.persistence.game.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.test.utils.DateFormat;
import com.choice.wechat.domain.game.Actm;
import com.choice.wechat.domain.game.DrawCount;
import com.choice.wechat.domain.game.Gift;
import com.choice.wechat.domain.game.SN;
import com.choice.wechat.persistence.game.GameMapper;
import com.choice.wechat.util.LogUtil;

@Repository
public class GameMapperImpl implements GameMapper{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static String getDrawCountSql = "";
	public List<DrawCount> getDrawCount(String openid, String gameType) {
		List<DrawCount> list = null;
		if(StringUtils.hasText(openid)){
			//当gameType为lucky_draw时，更改updateDrawCountSql，此时 updateDrawCountSql不能被final修饰
			if("lucky_draw".equals(gameType)){
				getDrawCountSql="SELECT OPENID, NOWCOUNT,UPDATETIME,GAMETYPE FROM WX_GAME_DRAWCOUNT WHERE OPENID=? AND DAT = TO_CHAR(SYSDATE, 'YYYY-MM-DD') AND GAMETYPE='lucky_draw'";
			}else{
				getDrawCountSql="SELECT OPENID, NOWCOUNT,UPDATETIME,GAMETYPE FROM WX_GAME_DRAWCOUNT WHERE OPENID=? AND GAMETYPE='get_coupon'";
			}
			LogUtil.writeToTxt("getCoupon", "getDrawCountSql【" + getDrawCountSql + "】，openid【" + openid+ "】" 
					+"】gameType【"+gameType+"】" + System.getProperty("line.separator"));
			list = jdbcTemplate.query(getDrawCountSql, new Object[]{openid}, new RowMapper<DrawCount>(){
				public DrawCount mapRow(ResultSet rs, int i)
						throws SQLException {
					DrawCount d = new DrawCount();
					d.setGameType(rs.getString("GAMETYPE"));
					d.setOpenid(rs.getString("OPENID"));
					d.setUpdateTime(DateFormat.getStringByDate(rs.getDate("UPDATETIME"), "yyyy-MM-dd HH:mm:ss"));
					d.setNowCount(rs.getInt("NOWCOUNT"));
					return d;
				}
			});
		}
		return list;
	}

	private  static String updateDrawCountSql = "";
	public void updateDrawCount(String openid, String gameType) {
		if(StringUtils.hasText(openid)){
			//当gameType为lucky_draw时，更改updateDrawCountSql，此时 updateDrawCountSql不能被final修饰
			if("lucky_draw".equals(gameType)){
				updateDrawCountSql="UPDATE WX_GAME_DRAWCOUNT SET NOWCOUNT=NOWCOUNT+1,UPDATETIME=SYSDATE WHERE OPENID=? AND DAT = TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
			}else{
				updateDrawCountSql="UPDATE WX_GAME_DRAWCOUNT SET NOWCOUNT=NOWCOUNT+1,UPDATETIME=SYSDATE WHERE OPENID=? AND GAMETYPE='get_coupon'";
			}
			jdbcTemplate.update(updateDrawCountSql,new Object[]{openid});
		}
	}

	private final static String insertDrawCountSql = "INSERT INTO WX_GAME_DRAWCOUNT(OPENID,NOWCOUNT,GAMETYPE,UPDATETIME,DAT) VALUES(?,1,?,SYSDATE,?)";
	public void insertDrawCount(String openid, String gameType) {
		if(StringUtils.hasText(openid)){
			//gameType为lucky_draw时，sql语句里的日期为系统的当天日期；gameType为get_coupon时，sql语句里面的日期，因为没有用处，所以默认为1900-00-00，
			if("lucky_draw".equals(gameType)){
				jdbcTemplate.update(insertDrawCountSql,new Object[]{openid,gameType,new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())});			
			}else{
				jdbcTemplate.update(insertDrawCountSql,new Object[]{openid,gameType,"1900-00-00"});			
			}
		}
	}
	
	private final static String insertSNSql = "INSERT INTO WX_GAME_SN(SN,OPENID,LUCKY,GAMETYPE,UPDATETIME,DAT) VALUES(?,?,?,?,SYSDATE,TO_CHAR(SYSDATE,'YYYY-MM-DD'))";
	public void insertSN(SN sn) {
		jdbcTemplate.update(insertSNSql,new Object[]{sn.getSn(),sn.getOpenid(),sn.getLucky(),sn.getGameType()});
	}

	private final static String getSNSql = "SELECT SN,OPENID,LUCKY,GAMETYPE,UPDATETIME,DRAW FROM WX_GAME_SN WHERE DAT = TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
	public List<SN> getSN(String SN, String openid, String gameType) {
		StringBuilder sb = new StringBuilder(getSNSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(SN)){
			sb.append(" AND SN=? ");
			valuesList.add(SN);
		}
		
		if(StringUtils.hasText(openid)){
			sb.append(" AND OPENID=? ");
			valuesList.add(openid);
		}
		
		if(StringUtils.hasText(gameType)){
			sb.append(" AND GAMETYPE=? ");
			valuesList.add(gameType);
		}
		
		List<SN> list = jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<SN>(){
			public SN mapRow(ResultSet rs, int i)
					throws SQLException {
				SN sn = new SN();
				sn.setGameType(rs.getString("GAMETYPE"));
				sn.setOpenid(rs.getString("OPENID"));
				sn.setUpdateTime(DateFormat.getStringByDate(rs.getDate("UPDATETIME"), "yyyy-MM-dd HH:mm:ss"));
				sn.setSn(rs.getString("SN"));
				sn.setLucky(rs.getString("LUCKY"));
				sn.setDraw(rs.getInt("DRAW"));
				return sn;
			}
		});
		return list;
	}

	private final static String getGiftSql = "SELECT GAMETYPE,LUCKY,LUCKYNAME,QUANTITY,USABLE,PRIORITY,PK_ACTM,IPERCNT FROM WX_GAME_GIFT WHERE 1=1";
	public List<Gift> getGift(String gameType, int lucky) {
		
		StringBuilder sb = new StringBuilder(getGiftSql);
		List<Object> valuesList = new ArrayList<Object>();
		
		if(StringUtils.hasText(gameType)){
			sb.append(" AND GAMETYPE=? ");
			valuesList.add(gameType);
		}
		
		if(lucky>0){
			sb.append(" AND LUCKY=? ");
			valuesList.add(lucky);
		}
		
		sb.append(" ORDER BY LUCKY");
		
		List<Gift> list = jdbcTemplate.query(sb.toString(), valuesList.toArray(), new RowMapper<Gift>(){
			public Gift mapRow(ResultSet rs, int i)
					throws SQLException {
				Gift sn = new Gift();
				sn.setGameType(rs.getString("GAMETYPE"));
				sn.setLucky(rs.getInt("LUCKY"));
				sn.setLuckyName(rs.getString("LUCKYNAME"));
				sn.setQuantity(rs.getInt("QUANTITY"));
				sn.setUsable(rs.getInt("USABLE"));
				sn.setPriority(rs.getInt("PRIORITY"));
				sn.setPk_actm(rs.getString("PK_ACTM"));
				sn.setIpercnt(rs.getInt("IPERCNT"));
				return sn;
			}
		});
		return list;
	}

	private final static String updateGiftQuantitySql = "UPDATE WX_GAME_GIFT SET USABLE=USABLE-1 WHERE USABLE>=1 AND GAMETYPE=? AND LUCKY=?";
	public int updateGiftQuantity(String gameType, int lucky) {
		return jdbcTemplate.update(updateGiftQuantitySql,new Object[]{gameType,lucky});
	}
	
	private final static String updateSNSql = "UPDATE WX_GAME_SN SET DRAW=?,UPDATETIME=SYSDATE WHERE SN=?";
	public void updateSN(String SN, int draw) {
		jdbcTemplate.update(updateSNSql,new Object[]{draw,SN});
	}

	private final static String getActmDetailVoucherSql = "SELECT A.PK_ACTM,A.VCODE,B.VVOUCHERCODE AS VOUCHERCODE,B.IVOUCHERNUM AS VOUCHERNUM FROM CBOH_ACTM_3CH A JOIN CBOH_ACTMVOUCHER_3CH B ON A.PK_ACTM=B.PK_ACTM WHERE A.VOUCHERBACK='Y' AND A.PK_ACTM=?";
	public List<Actm> getActmDetailVoucher(String pk_actm) {
		List<Actm> list = null;
		if(StringUtils.hasText(pk_actm)){
			list = jdbcTemplate.query(getActmDetailVoucherSql,new Object[]{pk_actm},new RowMapper<Actm>(){

				public Actm mapRow(ResultSet rs, int i)
						throws SQLException {
					Actm a = new Actm();
					a.setPk_actm(rs.getString("PK_ACTM"));
					a.setVcode(rs.getString("VCODE"));
					a.setVouchercode(rs.getString("VOUCHERCODE"));
					a.setVouchernum(rs.getInt("VOUCHERNUM"));
					return a;
				}
				
			});
		}
		return list;
	}
	
	private final static String getActmDetailFenSql = "SELECT A.PK_ACTM,A.VCODE,B.IFENNUM FROM CBOH_ACTM_3CH A JOIN CBOH_ACTMFEN_3CH B ON A.PK_ACTM=B.PK_ACTM WHERE A.VFENBACK='Y' AND A.PK_ACTM=?";
	public List<Actm> getActmDetailFen(String pk_actm) {
		List<Actm> list = null;
		if(StringUtils.hasText(pk_actm)){
			list = jdbcTemplate.query(getActmDetailFenSql,new Object[]{pk_actm},new RowMapper<Actm>(){

				public Actm mapRow(ResultSet rs, int i)
						throws SQLException {
					Actm a = new Actm();
					a.setPk_actm(rs.getString("PK_ACTM"));
					a.setVcode(rs.getString("VCODE"));
					a.setIfennum(rs.getInt("IFENNUM"));
					return a;
				}
				
			});
		}
		return list;
	}

	private final static String getActmDetailVoucherFromVcodeSql = "SELECT A.PK_ACTM,A.VCODE,A.ITICKETNUM,B.VVOUCHERCODE AS VOUCHERCODE,B.IVOUCHERNUM AS VOUCHERNUM FROM CBOH_ACTM_3CH A JOIN CBOH_ACTMVOUCHER_3CH B ON A.PK_ACTM=B.PK_ACTM WHERE A.VOUCHERBACK='Y' AND A.VCODE=?";
	public List<Actm> getActmDetailVoucherFromVcode(String vcode) {
		List<Actm> list = null;
		if(StringUtils.hasText(vcode)){
			list = jdbcTemplate.query(getActmDetailVoucherFromVcodeSql,new Object[]{vcode},new RowMapper<Actm>(){

				public Actm mapRow(ResultSet rs, int i)
						throws SQLException {
					Actm a = new Actm();
					a.setPk_actm(rs.getString("PK_ACTM"));
					a.setVcode(rs.getString("VCODE"));
					a.setVouchercode(rs.getString("VOUCHERCODE"));
					a.setVouchernum(rs.getInt("VOUCHERNUM"));
					try{
						a.setIticketnum(Integer.parseInt(rs.getString("ITICKETNUM")));
					}catch(Exception e){
						a.setIticketnum(0);
					}
					return a;
				}
				
			});
		}
		return list;
	}
	
	private final static String getWordActmCountSql = "SELECT COUNT(PK_ACTM) FROM WX_WORD_VOUCHER WHERE PK_ACTM=?";
	public int getWordActmCount(String pk_actm) {
		return jdbcTemplate.queryForInt(getWordActmCountSql, pk_actm);
	}
	
	private final static String insertWordActmSql = "INSERT INTO WX_WORD_VOUCHER(PK_ACTM,VCODE) VALUES(?,?)";
	public void insertWordActm(String pk_actm, String vcode) {
		jdbcTemplate.update(insertWordActmSql,new Object[]{pk_actm,vcode});
	}
	
	
	private final static String updateWordActmSql = "UPDATE WX_WORD_VOUCHER SET USEDNUM=USEDNUM+1 WHERE PK_ACTM=? AND USEDNUM<?";
	public int updateWordActm(String pk_actm,int iticketnum) {
		return jdbcTemplate.update(updateWordActmSql,new Object[]{pk_actm,iticketnum});
	}
}
