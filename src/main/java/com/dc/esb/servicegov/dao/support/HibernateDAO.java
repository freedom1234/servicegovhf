package com.dc.esb.servicegov.dao.support;

import com.dc.esb.servicegov.util.ReflectionUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 封装Hibernate原生API的DAO泛型基类.
 * <p/>
 * 可在Service层直接使用, 也可以扩展泛型DAO子类使用, 见两个构造函数的注释.
 * 参考Spring2.5自带的Petlinc例子, 取消了HibernateTemplate, 直接使用Hibernate原生API.
 *
 * @param <T>  DAO操作的对象类型
 * @param <PK> 主键类型
 * @author Vincent Fan
 */
@SuppressWarnings("unchecked")
public class HibernateDAO<T, PK extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SessionFactory sessionFactory;

    protected Class<T> entityClass;

    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 用于Dao层子类使用的构造函数.
     * 通过子类的泛型定义取得对象类型Class.
     * eg.
     * public class UserDao extends SimpleHibernateDao<User, Long>
     */
    public HibernateDAO() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    /**
     * 用于用于省略Dao层, 在Service层直接使用通用SimpleHibernateDao的构造函数.
     * 在构造函数中定义对象类型Class.
     * eg.
     * SimpleHibernateDao<User, Long> userDao = new SimpleHibernateDao<User, Long>(sessionFactory, User.class);
     */
    public HibernateDAO(final SessionFactory sessionFactory, final Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    /**
     * 取得sessionFactory.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 采用@Autowired按类型注入SessionFactory, 当有多个SesionFactory的时候在子类重载本函数.
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 取得当前Session.
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存新增的对象.
     */
    public void insert(final T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().save(entity);
        logger.debug("insert entity: {}", entity);
    }

    /**
     * 保存新增或修改的对象.
     */
    public void save(final T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().saveOrUpdate(entity);
        logger.debug("save or update entity: {}", entity);
    }

    public void deleteAll(){
        String hql = "delete from " + getEntityClass().getName();
        exeHql(hql);
    }

    /**
     * 删除对象.
     *
     * @param entity 对象必须是session中的对象或含id属性的transient对象.
     */
    public void delete(final T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().delete(entity);
        logger.debug("delete entity: {}", entity);
    }

    /**
     * 删除ENTITY LIST
     *
     * @param list
     */
    public void delete(final List<T> list) {
        Assert.notNull(list, "list不能为空");
        for (T entity : list) {
            delete(entity);
        }
    }

    /**
     * 按id删除对象.
     */
    public void delete(final PK id) {
        Assert.notNull(id, "id不能为空");
        delete(get(id));
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
    }

    /**
     * 按id获取对象.
     */
    public T get(final PK id) {
        Assert.notNull(id, "id不能为空");
        return (T) getSession().get(entityClass, id);
    }

    /**
     * 按id获取对象. 查不多不会抛出异常 返回null
     */
    public T getEntity(final PK id) {
        Assert.notNull(id, "id不能为空");
        return (T) getSession().get(entityClass, id);
    }
    /**
     * 按id列表获取对象列表.
     */
    public List<T> get(final Collection<PK> ids) {
        return find(Restrictions.in(getIdName(), ids));
    }

    /**
     * 获取全部对象.
     */
    public List<T> getAll() {
        return find();
    }

    /**
     * 获取全部对象, 支持按属性行序.
     */
    public List<T> getAll(String orderByProperty, boolean isAsc) {
        Criteria c = createCriteria();
        if (isAsc) {
            c.addOrder(Order.asc(orderByProperty));
        } else {
            c.addOrder(Order.desc(orderByProperty));
        }
        return c.list();
    }

    /**
     * 获取全部对象，并根据page返回当前页的List。
     *
     * @param page 分页信息对象
     * @return 符合条件的对象List，List的size可能为0。
     */

    public List<T> getAll(Page page) {
        Criteria criteria = getEntityCriteria();
        if (page.getOrderBy() != null) {
            if (page.getOrder().equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(page.getOrderBy()));
            } else {
                criteria.addOrder(Order.desc(page.getOrderBy()));
            }
        } else {
            criteria.addOrder(Order.desc(this.getIdName()));
        }
        criteria.setFirstResult(page.getFirstItemPos());
        criteria.setMaxResults(page.getPageSize());
        List<T> objects =  criteria.list();
        for(T object : objects){
            initProxyObject(object);
        }
        return objects;
    }

    /**
     * 获取全部对象，并返回该对象的分页信息对象。
     *
     * @param pageSize 分页大小
     * @return 分页信息对象
     */
    public Page getAll(int pageSize) {
        String countQueryString = " select count(*) from "
                + getEntityClass().getName();
        // 创建查询
        Query query = getSession().createQuery(countQueryString);

        List countlist = query.list();
        long totalCount = (Long) countlist.get(0);

        // 返回分页对象
        if (totalCount < 1) {
            totalCount = 0;
        }

        return new Page(totalCount, pageSize);
    }

    /**
     * 获取对象的分页信息对象。
     *
     * @param hql
     * @return 分页信息对象
     */
    public Page getPageBy(String hql,int pageSize) {
        // 创建查询
        Query query = getSession().createQuery(hql);

        List countlist = query.list();
        long totalCount = countlist.size();
        // 返回分页对象
        if (totalCount < 1) {
            totalCount = 0;
        }
        return new Page(totalCount, pageSize);
    }

    /**
     * 获取对象的分页信息对象。
     *
     * @param hql
     * @return 分页信息对象
     */
    public Page getPageBy(String hql,int pageSize,List<SearchCondition> searchConds) {
        // 创建查询
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < searchConds.size(); i++) {
            query.setParameter(i, searchConds.get(i).getFieldValue());
        }

        List countlist = query.list();
        long totalCount = countlist.size();

        // 返回分页对象
        if (totalCount < 1) {
            totalCount = 0;
        }
        return new Page(totalCount, pageSize);
    }

    /**
     * 获取对象的分页信息对象。
     *
     * @param hql
     * @return 分页信息对象
     */
    public Page getPageBy(String hql,int pageSize,final Object... values) {
        // 创建查询
        Query query = getSession().createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }

        List countlist = query.list();
        long totalCount = countlist.size();

        // 返回分页对象
        if (totalCount < 1) {
            totalCount = 0;
        }
        return new Page(totalCount, pageSize);
    }

    /**
     * 获取全部对象数量
     *
     * @return 全部对象数量
     */
    public Long getAllCount() {
        String countQueryString = " select count(*) from "
                + getEntityClass().getName();
        // 创建查询
        Query query = getSession().createQuery(countQueryString);

        List<?> countlist = query.list();
        Long totalCount = (Long) countlist.get(0);

        return totalCount;
    }


    /**
     * 按属性查找对象列表, 匹配方式为相等.
     */
    public List<T> findBy(final String propertyName, final Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = Restrictions.eq(propertyName, value);
        return find(criterion);
    }

    public List<T> findBy(final Map<String, String> properties) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
            criteria.add(criterion);
        }
        return criteria.list();
    }

    public List<T> findBy(final Map<String, String> properties, String orderByProperties) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
            criteria.add(criterion);
            if (null != orderByProperties) {
                criteria.addOrder(Order.asc(orderByProperties));
            }
        }
        return criteria.list();
    }

    /**
     * 根据属性名和属性值查询对象，并根据page返回当前页的List。
     *
     * @param searchCond 查询条件
     * @param page       分页信息对象
     * @return 符合条件的对象List，List的size可能为0。
     */
    public List<T> findBy(SearchCondition searchCond, Page page) {
        Criteria criteria = getEntityCriteria();
        criteria.add(Restrictions.eq(searchCond.getField(),
                searchCond.getFieldValue()));

        if (page.getOrderBy() != null) {
            if (page.getOrder().equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(page.getOrderBy()));
            } else {
                criteria.addOrder(Order.desc(page.getOrderBy()));
            }
        } else {
            criteria.addOrder(Order.desc(this.getIdName()));
        }

        criteria.setFirstResult(page.getFirstItemPos());
        criteria.setMaxResults(page.getPageSize());
        return criteria.list();
    }

    /**
     * 根据属性名和属性值查询对象，并返回该对象的分页信息对象。
     *
     * @param searchCond 查询条件
     * @param pageSize   分页大小
     * @return 分页信息对象
     */
    public Page findBy(SearchCondition searchCond, int pageSize) {
        StringBuffer hqlStr = new StringBuffer("select count(*) from ");
        hqlStr.append(getEntityClass().getName());
        hqlStr.append(" g where g.");
        hqlStr.append(searchCond.getField());
        hqlStr.append("=:fieldValue");
        // 创建查询
        Query query = getSession().createQuery(hqlStr.toString());
        query.setParameter("fieldValue", searchCond.getFieldValue());

        List countlist = query.list();
        long totalCount = (Long) countlist.get(0);

        hqlStr.delete(0, hqlStr.length());
        hqlStr = null;

        // 返回分页对象
        if (totalCount < 1) {
            totalCount = 0;
        }

        return new Page(totalCount, pageSize);
    }

    /**
     * 按属性查找唯一对象, 匹配方式为相等.
     */
    @Transactional
    public T findUniqueBy(final String propertyName, final Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = Restrictions.eq(propertyName, value);
        return (T) createCriteria(criterion).uniqueResult();
    }

    @Transactional
    public T findUniqureBy(final Map<String, String> params) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Criterion criterion = Restrictions.eq(entry.getKey(), entry.getValue());
            criteria.add(criterion);
        }
        return (T) criteria.uniqueResult();
    }

    @Transactional
    public List<T> findLike(final Map<String, String> params) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Criterion criterion = Restrictions.like(entry.getKey(), entry.getValue());
            criteria.add(criterion);
        }
        return criteria.list();
    }

    @Transactional
    public List<T> findLike(final Map<String, String> params, MatchMode matchMode, Object ... other){
        Criteria criteria = getSession().createCriteria(entityClass);

        if(other != null && other.length > 0){
            Page page = (Page)other[0];
            if (page.getOrderBy() != null) {
                if (page.getOrder().equalsIgnoreCase("asc")) {
                    criteria.addOrder(Order.asc(page.getOrderBy()));
                } else {
                    criteria.addOrder(Order.desc(page.getOrderBy()));
                }
            } else {
                criteria.addOrder(Order.desc(this.getIdName()));
            }

            criteria.setFirstResult(page.getFirstItemPos());
            criteria.setMaxResults(page.getPageSize());
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            Criterion criterion = Restrictions.like(entry.getKey(), entry.getValue(), matchMode);
            criteria.add(criterion);
        }
        return criteria.list();
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    @Transactional
    public List<T> find(final String hql, final Object... values) {
        return createQuery(hql, values).list();
    }
    @Transactional
    public <T> List<T> findFree(final String hql, final Object... values) {
        return createQuery(hql, values).list();
    }
    /**
     * 按HQL查询对象列表.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    @Transactional
    public List<T> find(final String hql, Page page,  final Object... values) {
        return createQuery(hql, page, values).list();
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param values 命名参数,按名称绑定.
     */
    @Transactional
    public <T> List<T> find(final String hql, final Map<String, ?> values) {
        return createQuery(hql, values).list();
    }

    /**
     * 按HQL查询唯一对象.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    @Transactional
    public <T> T findUnique(final String hql, final Object... values) {
        return (T) createQuery(hql, values).uniqueResult();
    }

    /**
     * 按HQL查询唯一对象.
     *
     * @param values 命名参数,按名称绑定.
     */
    @Transactional
    public <X> X findUnique(final String hql, final Map<String, ?> values) {
        return (X) createQuery(hql, values).uniqueResult();
    }

    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param values 数量可变的参数,按顺序绑定.
     * @return 更新记录数.
     */
    @Transactional
    public int batchExecute(final String hql, final Object... values) {
        return createQuery(hql, values).executeUpdate();
    }

    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param values 命名参数,按名称绑定.
     * @return 更新记录数.
     */
    @Transactional
    public int batchExecute(final String hql, final Map<String, ?> values) {
        return createQuery(hql, values).executeUpdate();
    }

    /**
     * 根据查询HQL与参数列表创建Query对象.
     * 与find()函数可进行更加灵活的操作.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public Query createQuery(final String queryString, final Object... values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    public Query createQuery(final String queryString, Page page, final Object... values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        query.setMaxResults(page.getPageSize());
        query.setFirstResult(page.getFirstItemPos());
        return query;
    }


    /**
     * 根据查询HQL与参数列表创建Query对象.
     * 与find()函数可进行更加灵活的操作.
     *
     * @param values 命名参数,按名称绑定.
     */
    public Query createQuery(final String queryString, final Map<String, ?> values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    /**
     * 按Criteria查询对象列表.
     *
     * @param criterions 数量可变的Criterion.
     */
    @Transactional
    public List<T> find(final Criterion... criterions) {
        return createCriteria(criterions).list();
    }

    /**
     * 按Criteria查询唯一对象.
     *
     * @param criterions 数量可变的Criterion.
     */
    @Transactional
    public T findUnique(final Criterion... criterions) {
        return (T) createCriteria(criterions).uniqueResult();
    }

    /**
     * 根据Criterion条件创建Criteria.
     * 与find()函数可进行更加灵活的操作.
     *
     * @param criterions 数量可变的Criterion.
     */
    public Criteria createCriteria(final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    /**
     * 初始化对象.
     * 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化.
     * 如果传入entity, 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性.
     * 如需初始化关联属性,需执行:
     * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
     * Hibernate.initialize(user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
     */
    public void initProxyObject(Object proxy) {
        Hibernate.initialize(proxy);
    }

    /**
     * Flush当前Session.
     */
    public void flush() {
        getSession().flush();
    }

    /**
     * 为Query添加distinct transformer.
     * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
     */
    public Query distinct(Query query) {
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return query;
    }

    /**
     * 为Criteria添加distinct transformer.
     * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
     */
    public Criteria distinct(Criteria criteria) {
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    /**
     * 取得对象的主键名.
     */
    public String getIdName() {
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
        return meta.getIdentifierPropertyName();
    }

    /**
     * 判断对象的属性值在数据库内是否唯一.
     * <p/>
     * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
     */
    public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
        if (newValue == null || newValue.equals(oldValue)) {
            return true;
        }
        Object object = findUniqueBy(propertyName, newValue);
        return (object == null);
    }

    /**
     * 取得Entity的Criteria
     *
     * @return 取得的Entity的Criteria
     */
    protected Criteria getEntityCriteria() {
        return getSession().createCriteria(entityClass);
    }

    /**
     * 根据属性名和属性值查询对象，并根据page返回当前页的List。
     *
     * @param searchConds 查询条件
     * @param page       分页信息对象
     * @return 符合条件的对象List，List的size可能为0。
     */
    public List<T> findBy(List<SearchCondition> searchConds, Page page) {
        Criteria criteria = getEntityCriteria();
        for (SearchCondition searchCond : searchConds) {
            criteria.add(Restrictions.eq(searchCond.getField(),
                    searchCond.getFieldValue()));
        }

        if (page.getOrderBy() != null) {
            if (page.getOrder().equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(page.getOrderBy()));
            } else {
                criteria.addOrder(Order.desc(page.getOrderBy()));
            }
        } else {
            criteria.addOrder(Order.desc(this.getIdName()));
        }

        criteria.setFirstResult(page.getFirstItemPos());
        criteria.setMaxResults(page.getPageSize());
        return criteria.list();
    }

    public List findBy(String hql, Page page, final Object... values) {
        // 创建查询
        Query query = getSession().createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        query.setFirstResult(page.getFirstItemPos());
        query.setMaxResults(page.getPageSize());
        List list = query.list();
        return list;
    }

    public List findBy(String hql, Page page, List<SearchCondition> searchConds) {

        // 创建查询
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < searchConds.size(); i++) {
            query.setParameter(i, searchConds.get(i).getFieldValue());
        }
        query.setFirstResult(page.getFirstItemPos());
        query.setMaxResults(page.getPageSize());
        List list = query.list();
        return list;
    }

    public List findBy(String hql) {

        // 创建查询
        Query query = getSession().createQuery(hql);
        List list = query.list();
        return list;
    }

    public List findBy(String hql, Page page) {

        // 创建查询
        Query query = getSession().createQuery(hql);
        query.setFirstResult(page.getFirstItemPos());
        query.setMaxResults(page.getPageSize());
        List list = query.list();
        return list;
    }

    public List<T> findBy(String hql, List<SearchCondition> searchConds) {

        // 创建查询
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < searchConds.size(); i++) {
            query.setParameter(i, searchConds.get(i).getFieldValue());
        }

        List list = query.list();
        return list;
    }

    public boolean exeHql(String hql, final Object... values) {
        Query query = getSession().createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.executeUpdate() > 0;
    }

    public List<T> exeSQL(String sql){
        SQLQuery query = getSession().createSQLQuery(sql);
        List list = query.list();
        return list;
    }

    public List<T> exeSQL(String sql,final Object... values){
        SQLQuery query = getSession().createSQLQuery(sql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        List list = query.list();
        return list;
    }

    public List<T> exeSQL(String sql,Page page,ArrayList<SearchCondition> searchConds){
        SQLQuery query = getSession().createSQLQuery(sql);
        for (int i = 0; i < searchConds.size(); i++) {
            query.setParameter(i, searchConds.get(i).getFieldValue());
        }
        query.setFirstResult(page.getFirstItemPos());
        query.setMaxResults(page.getPageSize());
        List list = query.list();
        return list;
    }

    public List exeSQL(String sql,Page page){
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setFirstResult(page.getFirstItemPos());
        query.setMaxResults(page.getPageSize());
        List list = query.list();
        return list;
    }


    public Page findPage(final String hql, int pageSize, List<SearchCondition> searchConds) {
        List<T> entitys = findBy(hql, searchConds);
        Page page = new Page(entitys.size(),pageSize);
        return page;
    }

}
