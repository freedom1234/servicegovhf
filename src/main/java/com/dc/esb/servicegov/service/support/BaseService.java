package com.dc.esb.servicegov.service.support;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.dao.support.SearchCondition;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface BaseService<T, PK extends Serializable> {

	public List<T> getAll();

	/**
	 * 根据id获取对象
	 *
	 * @param id
	 * @return
	 */
	public T getById(PK id);

	/**
	 * 根据多个id获取对象List
	 *
	 * @param ids
	 * @return
	 */
	public List<T> getByIds(Collection<PK> ids);

	/**
	 * 保存对象
	 *
	 * @param entity
	 */
	public void save(T entity);

	/**
	 * 修改对象
	 *
	 * @param entity
	 */
	public void update(T entity);

	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	public void delete(T entity);

	/**
	 * 根据Id删除
	 *
	 * @param id
	 */
	public void deleteById(PK id);

	public List<T> findBy(Map<String, String> properties);

	public List<T> findBy(String key, String value);


	public List<T> findBy(SearchCondition searchCond, Page page);

	public Page findBy(SearchCondition searchCond, int pageSize);

	public List<T> findBy(String hql, Page page,List<SearchCondition> searchConds);

	public List<T> findBy(String hql);

	public T findUniqueBy(Map<String,String> params);

	public T findUniqueBy(String name, Object value);

	public List<T> findLike(Map<String, String> params);

	public Page findPage(final String hql, int pageSize, List<SearchCondition> searchConds);

	public List<T> findBy(final Map<String, String> properties, String orderByProperties);
}
