package com.choice.wechat.service.common.company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choice.test.exception.CRUDException;
import com.choice.wechat.persistence.common.company.CompanyMapper;
import com.choice.wechat.domain.bookDesk.Company;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyMapper companyMapper;

	private final transient Log log = LogFactory.getLog(CompanyService.class);
	
	/**
	 * 通过企业标识查询企业信息
	 * @param pkGroup 企业标识
	 * @return
	 */
	public Company findCompanyById(String pkGroup) throws CRUDException {
		return companyMapper.findCompanyById(pkGroup);
	}
}
