package com.dc.esb.servicegov.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.SDAHisDAOImpl;
import com.dc.esb.servicegov.entity.SDA;
import com.dc.esb.servicegov.entity.SDAHis;
import com.dc.esb.servicegov.service.SDAHisService;
import com.dc.esb.servicegov.util.EasyUiTreeUtil;
import com.dc.esb.servicegov.util.TreeNode;

@Service
@Transactional
public class SDAHisServiceImpl extends AbstractBaseService<SDAHis, String> implements SDAHisService{
	@Autowired
	private SDAHisDAOImpl daoImpl;
	
	public List<SDAHis> getListByOperation(String autoId){
		String hql = " from SDAHis where operationHisId = ?";
		return daoImpl.find(hql, autoId);
	}
	public List<TreeNode> genderSDATree(String autoId) {
		List<SDAHis> list = getListByOperation(autoId);
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("id", "sdaId");
		fields.put("text", "structName");
		fields.put("append1", "structAlias");
		fields.put("append2", "desc");
		fields.put("append3", "remark");

		EasyUiTreeUtil eUtil = new EasyUiTreeUtil();

		return eUtil.convertTree(list, fields);

	}

	@Override
	public HibernateDAO<SDAHis, String> getDAO() {
		return null;
	}
	@Override
	public void save(SDAHis entity){
		daoImpl.save(entity);
	}
}
