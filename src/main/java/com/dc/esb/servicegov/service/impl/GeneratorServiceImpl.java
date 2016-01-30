package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.Generator;
import com.dc.esb.servicegov.service.GeneratorService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import com.dc.esb.servicegov.service.support.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.GeneratorDAOImpl;

import java.util.List;

@Service
@Transactional
public class GeneratorServiceImpl extends AbstractBaseService<Generator, String> implements GeneratorService {
	@Autowired
	private  GeneratorDAOImpl generatorDAOImpl;

	@Override
	public HibernateDAO<Generator, String> getDAO() {
		return generatorDAOImpl;
	}

	public Generator findLikeName(String name){
		String hql = "from Generator where name like '%"+ name +"%'";
		List<Generator> list= generatorDAOImpl.find(hql);
		if (list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}


}
