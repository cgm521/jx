package com.choice.wechat.persistence.reply.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.choice.wechat.domain.reply.KeyWord;
import com.choice.wechat.persistence.reply.KeyWordMapper;

@Repository
public class KeyWordMapperImpl implements KeyWordMapper{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static String findMsgFormKeyWordSql = "SELECT PK_ID,KEYWORD,REPLYTYPE,CONTENT,TITLE,DESCRIPTION,PICURL,URL,WXMSGTYP AS MSGTYPE,MEDIA_ID FROM　WX_KEYWORD,CBOH_BRAND_3CH WHERE CBOH_BRAND_3CH.PK_BRAND=WX_KEYWORD.PK_BRAND AND DR=0 ";
	public List<KeyWord> findMsgFormKeyWord(String appid,String keyWord,String msgType) {
		StringBuilder sb = new StringBuilder(findMsgFormKeyWordSql);
		List<Object> param = new ArrayList<Object>();
		if(StringUtils.hasText(keyWord) && StringUtils.hasText(msgType)){
			if("text".equals(msgType)){
				sb.append(" AND INSTR(KEYWORD,?)>0 or INSTR(?,KEYWORD)>0 ");
				param.add(keyWord);
				param.add(keyWord);
			}else if("event".equals(msgType)){
				sb.append(" AND KEYWORD=?");
				param.add(keyWord);
			}
		}
		
		if(StringUtils.hasText(appid)){
			sb.append(" AND VAPPID=?");
			param.add(appid);
		}
		
		sb.append(" ORDER BY ILEV DESC");
		
		List<KeyWord> result = jdbcTemplate.query(sb.toString(), param.toArray(), new RowMapper<KeyWord>(){
			public KeyWord mapRow(ResultSet rs, int i) throws SQLException {
				KeyWord k = new KeyWord();
				k.setContent(rs.getString("CONTENT"));
				k.setDescription(rs.getString("DESCRIPTION"));
				k.setKeyWord(rs.getString("KEYWORD"));
				k.setMsgType(rs.getString("MSGTYPE"));
				k.setReplyType(rs.getString("REPLYTYPE"));
				k.setPicUrl(rs.getString("PICURL"));
				k.setPk_id(rs.getString("PK_ID"));
				k.setTitle(rs.getString("TITLE"));
				k.setUrl(rs.getString("URL"));
				k.setMedia_id(rs.getString("MEDIA_ID"));
				return k;
			}
			
		});
		if(result.size()<1&&StringUtils.hasText(msgType)){
			System.out.println("msgType="+msgType);
			sb = new StringBuilder(findMsgFormKeyWordSql);
			param=new ArrayList<Object>();
			if(StringUtils.hasText(keyWord) ){			
				sb.append(" AND INSTR(KEYWORD,'非关键字回复')>0");				
			}
			if(StringUtils.hasText(appid)){
				sb.append(" AND VAPPID=?");
				param.add(appid);
			}
			sb.append(" ORDER BY ILEV DESC");
			result = jdbcTemplate.query(sb.toString(), param.toArray(), new RowMapper<KeyWord>(){
			public KeyWord mapRow(ResultSet rs, int i) throws SQLException {
				KeyWord k = new KeyWord();
				k.setContent(rs.getString("CONTENT"));
				k.setDescription(rs.getString("DESCRIPTION"));
				k.setKeyWord(rs.getString("KEYWORD"));
				k.setMsgType(rs.getString("MSGTYPE"));
				k.setReplyType(rs.getString("REPLYTYPE"));
				k.setPicUrl(rs.getString("PICURL"));
				k.setPk_id(rs.getString("PK_ID"));
				k.setTitle(rs.getString("TITLE"));
				k.setUrl(rs.getString("URL"));
				k.setMedia_id(rs.getString("MEDIA_ID"));
				return k;
			  }
				
			});
		}
		return result;
	}

}
