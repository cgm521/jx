package com.choice.wechat.persistence.common.dict;

import java.util.List;

import com.choice.wechat.domain.common.dict.DictItem;

public interface DictItemMapper {

	/**
	 * 通过编号查询字典项
	 * @param dictItem
	 * @return
	 */
	public DictItem findDictItemById(DictItem dictItem);
	
	/**
	 * 通过条件查询字典项列表
	 * @param dictItem
	 * @return
	 */
	public List<DictItem> findDictItemsByGroup(DictItem dictItem);
}
