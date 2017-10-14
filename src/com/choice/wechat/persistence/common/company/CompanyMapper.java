package com.choice.wechat.persistence.common.company;

import com.choice.wechat.domain.bookDesk.Company;

public interface CompanyMapper {

	/**
	 * 通过企业标识查询企业信息
	 * @param pkGroup 企业标识
	 * @return
	 */
	public Company findCompanyById(String pkGroup);
}
