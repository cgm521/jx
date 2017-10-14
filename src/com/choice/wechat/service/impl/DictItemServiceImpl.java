package com.choice.wechat.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.choice.wechat.domain.common.dict.DictItem;
import com.choice.wechat.persistence.common.dict.DictItemMapper;

/**
 * 字典项
 * @author 王恒军
 */
@Repository
public class DictItemServiceImpl implements DictItemMapper {
	
	@Autowired
	public DataSource dataSource;
	
	/**
	 * 通过编号查询字典项
	 * @param dictItem
	 * @return
	 */
	public DictItem findDictItemById(DictItem dictItem){
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		DictItem dictItemData = new DictItem();
		
		try {
			// 连接数据库
			conn = dataSource.getConnection();

			String sql = "select groupid,itemid,itemname,status,sortnumber from dict_item where groupid = ? and itemid = ? and status = 1";
			pst = conn.prepareStatement(sql);
			pst.setString(1, dictItem.getGroupID());
			pst.setString(2, dictItem.getItemID());

			rs = pst.executeQuery();

			while (rs.next()) {
				dictItemData.setGroupID(rs.getString("groupid"));
				dictItemData.setItemID(rs.getString("itemid"));
				dictItemData.setItemName(rs.getString("itemname"));
				dictItemData.setStatus(rs.getString("status"));
				dictItemData.setSortNumber(rs.getString("sortnumber"));
				
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

		return dictItemData;
	}
	
	/**
	 * 通过条件查询字典项列表
	 * @param dictItem
	 * @return
	 */
	public List<DictItem> findDictItemsByGroup(DictItem dictItem) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DictItem> dictItemList = new ArrayList<DictItem>();
		
		try {
			// 连接数据库
			conn = dataSource.getConnection();

			String sql = "select groupid,itemid,itemname,status,sortnumber from dict_item where groupid = ? and status = 1 order by sortnumber asc";
			pst = conn.prepareStatement(sql);
			pst.setString(1, dictItem.getGroupID());

			rs = pst.executeQuery();

			while (rs.next()) {
				DictItem dictItemData = new DictItem();
				dictItemData.setGroupID(rs.getString("groupid"));
				dictItemData.setItemID(rs.getString("itemid"));
				dictItemData.setItemName(rs.getString("itemname"));
				dictItemData.setStatus(rs.getString("status"));
				dictItemData.setSortNumber(rs.getString("sortnumber"));

				dictItemList.add(dictItemData);
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

		return dictItemList;
	}
}
