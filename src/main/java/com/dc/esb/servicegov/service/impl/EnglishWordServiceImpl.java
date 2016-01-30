package com.dc.esb.servicegov.service.impl;

import java.util.List;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.EnglishWordDAOImpl;
import com.dc.esb.servicegov.entity.EnglishWord;
import com.dc.esb.servicegov.service.EnglishWordService;

@Service
@Transactional
public class EnglishWordServiceImpl extends AbstractBaseService<EnglishWord, String> implements EnglishWordService {

	@Autowired
	private EnglishWordDAOImpl englishWordDAOImpl;

	@Override
	public HibernateDAO getDAO() {
		return englishWordDAOImpl;
	}
}
