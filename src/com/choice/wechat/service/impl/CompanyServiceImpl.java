package com.choice.wechat.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.choice.wechat.domain.bookDesk.Company;
import com.choice.wechat.persistence.common.company.CompanyMapper;

/**
 * 字典项
 * @author 王恒军
 */
@Repository
public class CompanyServiceImpl implements CompanyMapper {
	
	@Autowired
	public DataSource dataSource;
	
	/**
	 * 通过企业标识查询企业信息
	 * @param pkGroup 企业标识
	 * @return
	 */
	public Company findCompanyById(String pkGroup) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Company info = new Company();

		try {
			// 连接数据库
			conn = dataSource.getConnection();

			String sql = "select pk_group, vcontent, vtitle, vcomUrl, vcomPic, vtakeOrdr, vregestPic, appId, secret, app_key,"
					+ " partner, partner_key, wx_title, softwareSerialNo, softwareKey, isGift, inAmt, minBookHours, sessionType "
					+ " from company_config where pk_group = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, pkGroup);

			rs = pst.executeQuery();

			while (rs.next()) {
				info.setPk_group(rs.getString("pk_group"));
				info.setVcontent(rs.getString("vcontent"));
				info.setVtitle(rs.getString("vtitle"));
				info.setVcomUrl(rs.getString("vcomUrl"));
				info.setVcomPic(rs.getString("vcomPic"));
				info.setVtakeOrdr(rs.getString("vtakeOrdr"));
				info.setVregestPic(rs.getString("vregestPic"));
				info.setAppId(rs.getString("appId"));
				info.setSecret(rs.getString("secret"));
				info.setApp_key(rs.getString("app_key"));
				info.setPartner(rs.getString("partner"));
				info.setPartner_key(rs.getString("partner_key"));
				info.setWx_title(rs.getString("wx_title"));
				info.setSoftwareSerialNo(rs.getString("softwareSerialNo"));
				info.setSoftwareKey(rs.getString("softwareKey"));
				info.setIsGift(rs.getString("isGift"));
				info.setInAmt(rs.getInt("inAmt"));
				info.setMinBookHours(rs.getInt("minBookHours"));
				info.setSessionType(rs.getString("sessionType"));

				break;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != pst) {
					pst.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return info;
	}
}
