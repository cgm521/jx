package com.choice.wechat.service.common.dict;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choice.test.exception.CRUDException;
import com.choice.wechat.persistence.common.dict.DictItemMapper;
import com.choice.wechat.domain.common.dict.DictItem;

@Service
public class DictItemService {
	
	@Autowired
	private DictItemMapper dictItemMapper;

	private final transient Log log = LogFactory.getLog(DictItemService.class);

	/**
	 * 查询字典项名称
	 * @param dictItem
	 * @return
	 * @throws CRUDException
	 */
	public DictItem findDictItemById(DictItem dictItem) throws CRUDException {
		try {
			return dictItemMapper.findDictItemById(dictItem);
		} catch (Exception e) {
			log.error(e);
			throw new CRUDException(e);
		}
	}

	/**
	 * 根据字典组编号查询全部字典项信息
	 * @param dictItem
	 * @return
	 * @throws CRUDException
	 */
	public List<DictItem> findDictItemsByGroup(DictItem dictItem) throws CRUDException {
		try {
			return dictItemMapper.findDictItemsByGroup(dictItem);
		} catch (Exception e) {
			log.error(e);
			throw new CRUDException(e);
		}
	}
}
