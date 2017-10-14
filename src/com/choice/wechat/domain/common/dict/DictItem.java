package com.choice.wechat.domain.common.dict;

import java.io.Serializable;

/**
 * 字典项
 * @author 
 *
 */
public class DictItem implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 297435753931027947L;

	/**
	* 字典组编号
	*/
	private String groupID;
	
	/**
	* 字典项编号
	*/
	private String itemID;
	
	/**
	* 字典项名称
	*/
	private String itemName;
	
	/**
	* 状态
	*/
	private String status;

	/**
	* 排序
	*/
	private String sortNumber;
	
	public DictItem() {
		groupID = "";
		itemID = "";
		itemName = "";
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(String sortNumber) {
		this.sortNumber = sortNumber;
	}
}

