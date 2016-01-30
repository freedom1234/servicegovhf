package com.dc.esb.servicegov.service.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.EnumDAOImpl;
import com.dc.esb.servicegov.dao.impl.EnumElementMapDAOImpl;
import com.dc.esb.servicegov.dao.impl.EnumElementsDAOImpl;
import com.dc.esb.servicegov.dao.impl.MasterSlaveEnumMapDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;
import com.dc.esb.servicegov.entity.EnumElementMap;
import com.dc.esb.servicegov.entity.EnumElements;
import com.dc.esb.servicegov.entity.MasterSlaveEnumMap;
import com.dc.esb.servicegov.entity.SGEnum;
import com.dc.esb.servicegov.service.EnumService;
import com.dc.esb.servicegov.service.support.AbstractBaseService;

@Service
@Transactional
public class EnumServiceImpl extends AbstractBaseService<SGEnum, String> implements EnumService {

    @Autowired
    public EnumDAOImpl enumDAOImpl;

    @Autowired
    public EnumElementsDAOImpl elementsDAOImpl;

    @Autowired
    public MasterSlaveEnumMapDAOImpl masterSlaveEnumMapDAOImpl;

    @Autowired
    public EnumElementMapDAOImpl elementMapDAOImpl;

    @Override
    public HibernateDAO<SGEnum, String> getDAO() {
        return enumDAOImpl;
    }

    @Override
    public boolean insertEnum(SGEnum anEnum) {
        enumDAOImpl.insert(anEnum);
        return true;
    }

    @Override
    public HashMap<String, Object> getAllEnum(String hql, List<SearchCondition> searchConds, int pageNum, int rows) {
        Page page = enumDAOImpl.findPage(hql.toString(), rows, searchConds);
        page.setPage(pageNum);
        List<SGEnum> list = enumDAOImpl.findBy(hql.toString(), page, searchConds);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("total", page.getResultCount());
        map.put("rows", list);
        return map;
    }

    @Override
    public SGEnum getByEnumId(String id) {
        return enumDAOImpl.findBy("id", id).get(0);
    }

    @Override
    public List<SGEnum> getSlaveByEnumId(String id) {
        String hql = "select a from SGEnum a where a.id in(select m.slaveId from MasterSlaveEnumMap m where m.masterId = '" + id + "')";
        return enumDAOImpl.find(hql);
    }

    public List<SGEnum> checkSlaveByEnumId(String id, String processId) {
        String hql = "select a from SGEnum a where a.processId ='" + processId + "' and a.id in(select m.slaveId from MasterSlaveEnumMap m where m.masterId = '" + id + "')";
        return enumDAOImpl.find(hql);
    }

    @Override
    public SGEnum getMasterBySlaveId(String id) {
        String hql = "select a from SGEnum a where a.id in(select m.masterId from MasterSlaveEnumMap m where m.slaveId = '" + id + "')";
        SGEnum enum1 = new SGEnum();
        try {
            enum1 = enumDAOImpl.find(hql).get(0);
        } catch (Exception e) {

        }
        return enum1;
    }

    @Override
    public boolean deleteEnumById(String id) {
        enumDAOImpl.delete(id);
        return true;
    }

    @Override
    public boolean deleteEnumElementsByIds(List<String> ids) {
        for (String id : ids) {
            elementsDAOImpl.delete(id);
        }
        return true;
    }

    @Override
    public boolean updateEnum(SGEnum anEnum) {
        enumDAOImpl.save(anEnum);
        return true;
    }

    @Override
    public List<SGEnum> getEnumByParams(HashMap<String, String> map) {
        return enumDAOImpl.findLike(map, MatchMode.ANYWHERE);
    }

    @Override
    public HashMap<String, Object> getElementByMasterId(String hql, int pageNum, int rows) {
        Page page = elementsDAOImpl.getPageBy(hql.toString(), rows);
        page.setPage(pageNum);
        List<EnumElements> list = elementsDAOImpl.findBy(hql.toString(), page);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("total", page.getResultCount());
        map.put("rows", list);
        return map;
    }

    @Override
    public List<EnumElements> getElementsByEnumId(String id) {
        String hql = "select a from EnumElements a where a.enumId = '" + id + "'";
        return elementsDAOImpl.find(hql);
    }

    @Override
    public boolean addElement(EnumElements elements) {
        elementsDAOImpl.insert(elements);
        return true;
    }

    @Override
    public List getElementsMapping(StringBuffer sql) {
        Session session = enumDAOImpl.getSession();
        Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.list();
        return list;
    }

    @Override
    public HashMap<String, Object> getElementsMapping(String sql, int pageNum, int rows) {
        Session session = enumDAOImpl.getSession();
        Query queryA = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<EnumElements> listAll = queryA.list();

        Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setFirstResult((pageNum < 1 ? 0 : pageNum - 1) * rows);
        query.setMaxResults(rows);
//		List<EnumElements> listAll = elementsDAOImpl.exeSQL(sql);
//		Page page=new Page(listAll.size()<1?0:listAll.size(), rows);
//		page.setPage(pageNum);
////		List<EnumElements> list = elementsDAOImpl.findBy(hql.toString(), page);
//		List<EnumElements> list = elementMapDAOImpl.exeSQL(sql, page);
        List<EnumElements> list = query.list();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("total", listAll.size());
        map.put("rows", list);
        return map;
    }

//    @Override
//    public HashMap<String, Object> getElementsMapping(String hql, int pageNum, int rows) {
////		Page page = elementsDAOImpl.getPageBy(hql.toString(), rows);
//        List<EnumElements> listAll = elementsDAOImpl.exeSQL(hql);
//        Page page = new Page(listAll.size() < 1 ? 0 : listAll.size(), rows);
//        page.setPage(pageNum);
////		List<EnumElements> list = elementsDAOImpl.findBy(hql.toString(), page);
//        List<EnumElements> list = elementMapDAOImpl.exeSQL(hql, page);
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("total", page.getResultCount());
//        map.put("rows", list);
//        return map;
//    }
//	public HashMap<String, Object> getElementsMapping(String hql,int pageNum,int rows){
//		Page page = elementsDAOImpl.getPageBy(hql.toString(), rows);
//		page.setPage(pageNum);
//		List<EnumElements> list = elementsDAOImpl.findBy(hql.toString(), page);
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("total", page.getResultCount());
//		map.put("rows", list);
//		return map;
//	}

    @Override
    public boolean addMasterSlaveEnumMap(MasterSlaveEnumMap enumMap) {
        masterSlaveEnumMapDAOImpl.insert(enumMap);
        return true;
    }

    @Override
    public List<EnumElements> getElementsByHql(String hql) {
        return elementsDAOImpl.find(hql);
    }

    @Override
    public boolean addEnumElementMap(EnumElementMap elementMap) {
        elementMapDAOImpl.insert(elementMap);
        return true;
    }

    @Override
    public boolean deleteElementsMapping(List<EnumElementMap> mappingList) {
        for (EnumElementMap enumElementMap : mappingList) {
            elementMapDAOImpl.delete(enumElementMap);
        }
        return true;
    }

    @Override
    public boolean deleteElementsMappingByPK(String masterId, String slaveId) {
        EnumElementMap elementMap = new EnumElementMap();
        elementMap.setMasterElementId(masterId);
        elementMap.setSlaveElementId(slaveId);
        elementMapDAOImpl.delete(elementMap);
        return true;
    }

    //前端唯一性验证
    public boolean uniqueValid(String key, String value) {
        SGEnum entity = findUniqueBy(key, value);
        if (entity != null) {
            return false;
        }
        return true;
    }
}
