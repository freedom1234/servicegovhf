package com.dc.esb.servicegov.dao.support;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * 单个Entity 提供CRUD操作的Hibernate DAO基类
 *
 * @author <a href="mailto:HL_Qu@hotmail.com">Along</a>
 * @version $Revision: 1.2 $
 * @since 2009-10-12
 */
@SuppressWarnings("all")
public abstract class HibernateEntityDAO<T> extends HibernateDaoSupport {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * DAO所管理的Entity类型.
     */
    protected Class<T> entityClass;

    public HibernateEntityDAO() {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class<T>) params[0];
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 根据ID获取对象
     *
     * @param id 对象的Id
     * @return 获得的对象或null。
     */
    public T get(Serializable id) {
        return (T) getHibernateTemplate().get(getEntityClass(), id);
    }

    /**
     * 通过对象的ID串获得对象
     *
     * @param ids 用","分割的对象Id
     * @return 对象的List，List的size可能为0。
     */
    public List<T> getByIds(String ids) {
        StringBuffer hqlStr = new StringBuffer("select u from ");
        hqlStr.append(getEntityClass().getName());
        hqlStr.append(" as u where u.id in (");
        hqlStr.append(ids);
        hqlStr.append(")");
        Query query = this.createQuery(hqlStr.toString());

        List<T> resultList = query.list();

        hqlStr.delete(0, hqlStr.length());
        hqlStr = null;

        return resultList;
    }

    /**
     * 获取全部对象
     *
     * @return 获取的全部对象的List，List的size可能为0。
     */
    public List<T> getAll() {
        return getHibernateTemplate().loadAll(getEntityClass());
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
        return criteria.list();
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
     * 新增或者更新对象
     *
     * @param o 要新增或更新的对象
     */
    public void saveOrUpdate(Object o) {
        getHibernateTemplate().saveOrUpdate(o);
    }

    /**
     * 更新对象
     *
     * @param o 要更新的对象
     */
    public void update(Object o) {
        getHibernateTemplate().update(o);
    }

    /**
     * 删除对象
     *
     * @param o 要删除的对象
     */
    public void remove(Object o) {
        getHibernateTemplate().delete(o);
    }

    /**
     * 根据ID删除对象
     *
     * @param id 对象的Id
     */
    public void removeById(Serializable id) {
        remove(get(id));
    }

    /**
     * HQL查询
     *
     * @param hql    HQL语句
     * @param values 可变参数 用户可以如下四种方式使用 dao.find(hql) dao.find(hql,arg0);
     *               dao.find(hql,arg0,arg1); dao.find(hql,new
     *               Object[arg0,arg1,arg2])
     * @return 查到的对象List，List的size可能为0。
     */
    public List find(String hql, Object... values) {
        return getHibernateTemplate().find(hql, values);
    }

    /**
     * 根据属性名和属性值查询对象
     *
     * @param searchCond 查询条件
     * @return 符合条件的对象List，List的size可能为0。
     */
    public List<T> findBy(SearchCondition searchCond) {
        return createCriteria(
                Restrictions.eq(searchCond.getField(),
                        searchCond.getFieldValue())).list();
    }

    /**
     * 根据属性名和属性值查询对象
     *
     * @param searchCond 查询条件
     * @param order      排序的字段
     * @param orderBy    排序，asc：升序，desc：降序。
     * @return 符合条件的对象List，List的size可能为0。
     */
    public List<T> findBy(SearchCondition searchCond, String order,
                          String orderBy) {
        Criteria criteria = createCriteria(Restrictions.eq(
                searchCond.getField(), searchCond.getFieldValue()));

        if (order.equalsIgnoreCase("asc")) {
            criteria.addOrder(Order.asc(order));
        } else if (order.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(order));
        }

        return criteria.list();
    }

    /**
     * 根据属性名和属性值查询不符合查询条件的对象
     *
     * @param searchCond 查询条件
     * @return 找到的对象List，List的size可能为0。
     */
    public List<T> findByNotLike(SearchCondition searchCond) {
        return createCriteria(
                Restrictions.ne(searchCond.getField(),
                        searchCond.getFieldValue())).list();
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
     * 根据属性名和属性值查询对象，并根据page返回当前页的List。
     *
     * @param searchCond 查询条件
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
     * 根据属性名和属性值查询单个对象.
     *
     * @param name  属性名
     * @param value 属性值
     * @return 符合条件的唯一对象
     */
    public T findUniqueBy(String name, Object value) {
        return (T) createCriteria(Restrictions.eq(name, value)).uniqueResult();
    }

    /**
     * 根据属性名和属性值以Like AnyWhere方式查询对象
     *
     * @param searchCond 查询条件
     * @return 符合条件的对象List，List的size可能为0。
     */
    public List<T> findByLike(SearchCondition searchCond) {
        return createCriteria(
                Restrictions.like(searchCond.getField(),
                        "%" + searchCond.getFieldValue() + "%",
                        MatchMode.ANYWHERE)).list();
    }

    /**
     * 根据属性名和属性值以Like AnyWhere方式查询对象，并根据page返回当前页的List。
     *
     * @param searchCond 查询条件
     * @param page       分页信息对象
     * @return 符合条件的对象List，List的size可能为0。
     */
    public List<T> findByLike(SearchCondition searchCond, Page page) {
        Criteria criteria = getEntityCriteria();
        criteria.add(Restrictions.like(searchCond.getField(),
                "%" + searchCond.getFieldValue() + "%", MatchMode.ANYWHERE));

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
     * 根据属性名和属性值以Like AnyWhere方式查询对象，并返回该对象的分页信息对象。
     *
     * @param searchCond 查询条件
     * @param pageSize   分页大小
     * @return 分页信息对象
     */
    public Page findByLike(SearchCondition searchCond, int pageSize) {
        StringBuffer hqlStr = new StringBuffer("select count(*) from ");
        hqlStr.append(getEntityClass().getName());
        hqlStr.append(" g where g.");
        hqlStr.append(searchCond.getField());
        hqlStr.append(" like :fieldValue");
        // 创建查询
        Query query = getSession().createQuery(hqlStr.toString());
        query.setParameter("fieldValue", "%" + searchCond.getFieldValue() + "%");

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
     * 判断对象某些属性的值在数据库中不存在重复
     *
     * @param entity 具有属性值的对象
     * @param names  在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     * @return 是否存在重复
     */
    public boolean isNotUnique(Object entity, String names) {
        Criteria criteria = getEntityCriteria().setProjection(
                Projections.rowCount());
        String[] nameList = names.split(",");
        try {
            // 循环加入
            for (String name : nameList) {
                criteria.add(Restrictions.eq(name,
                        PropertyUtils.getProperty(entity, name)));
            }

            // 以下代码为了如果是update的情况,排除entity自身.
            // 通过Hibernate的MetaData接口取得主键名
            String idName = getIdName();
            // 取得entity的主键值
            Serializable id = getId(entity);
            // 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
            if (id != null) {
                criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
            }
        } catch (IllegalAccessException e) {
            logger.error("Error when reflection on entity," + e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            logger.error("Error when reflection on entity," + e.getMessage());
            return false;
        } catch (NoSuchMethodException e) {
            logger.error("Error when reflection on entity," + e.getMessage());
            return false;
        }
        return (Integer) criteria.uniqueResult() > 0;
    }

    /**
     * 创建Query对象.
     * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以返回Query后自行设置.
     * 留意可以连续设置,如 dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     *
     * @param values 可变参数 用户可以如下四种方式使用 dao.getQuery(hql) dao.getQuery(hql,arg0);
     *               dao.getQuery(hql,arg0,arg1); dao.getQuery(hql,new
     *               Object[arg0,arg1,arg2])
     */
    public Query createQuery(String hql, Object... values) {
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query;
    }

    /**
     * 创建SQLQuery对象.
     * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以返回Query后自行设置.
     * 留意可以连续设置,如 dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     *
     * @param values 可变参数 用户可以如下四种方式使用 dao.getQuery(hql) dao.getQuery(hql,arg0);
     *               dao.getQuery(hql,arg0,arg1); dao.getQuery(hql,new
     *               Object[arg0,arg1,arg2])
     */
    public SQLQuery createSQLQuery(String hql, Object... values) {
        SQLQuery query = getSession().createSQLQuery(hql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query;
    }

    /**
     * 通过Query来创建结果集
     *
     * @param hql      HQL
     * @param index    分页的索引
     * @param pageSize 分页大小
     * @param values   帮定的对象
     * @return List 查询结果的List，List的size可能为0。
     */
    public List<Object> listQuery(String hql, int index, int pageSize,
                                  Object... values) {
        Query query = createQuery(hql, values);
        List resultList = query.setFirstResult(index).setMaxResults(pageSize)
                .list();

        return resultList;
    }

    /**
     * 创建Criteria对象
     *
     * @param criterion 可变条件列表,Restrictions生成的条件
     * @return 创建的Criteria对象
     */
    public Criteria createCriteria(Criterion... criterion) {
        Criteria criteria = getSession().createCriteria(getEntityClass());
        for (Criterion c : criterion) {
            criteria.add(c);
        }

        return criteria;
    }

    /**
     * 获得当前类的PK字段名
     *
     * @return 获得的字段名
     */
    public String getIdName() {
        String idName = getSessionFactory().getClassMetadata(getEntityClass())
                .getIdentifierPropertyName();
        return idName;
    }

    /**
     * 取得对象的主键值,辅助函数
     *
     * @param entity 要获取主键的对象
     * @return 取得对象的主键值
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Serializable getId(Object entity) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return (Serializable) PropertyUtils.getProperty(entity, getIdName());
    }

    /**
     * 取得Entity的Criteria
     *
     * @return 取得的Entity的Criteria
     */
    protected Criteria getEntityCriteria() {
        return getSession().createCriteria(getEntityClass());
    }
    /**
     * TODO 暂时不关心分页。请完善
     */
    /** 分页 */
    // -- 分页查询函数 --//

    /**
     * 分页获取全部对象.
     */
//    public org.springside.modules.orm.Page<T> getAll(
//            final org.springside.modules.orm.Page<T> page) {
//        return findPage(page);
//    }

    /**
     * 按HQL分页查询.
     *
     * @param page   分页参数.不支持其中的orderBy参数.
     * @param hql    hql语句.
     * @param values 数量可变的查询参数,按顺序绑定.
     * @return 分页查询结果, 附带结果列表及所有查询时的参数.
     */
//    public org.springside.modules.orm.Page<T> findPage(
//            final org.springside.modules.orm.Page<T> page, final String hql,
//            final Object... values) {
//        Assert.notNull(page, "page不能为空");
//
//        Query q = createQuery(hql, values);
//
//        if (page.isAutoCount()) {
//            long totalCount = countHqlResult(hql, values);
//            page.setTotalCount(totalCount);
//        }
//
//        setPageParameter(q, page);
//        List result = q.list();
//        page.setResult(result);
//        return page;
//    }

    /**
     * 按HQL分页查询.
     *
     * @param page   分页参数.
     * @param hql    hql语句.
     * @param values 命名参数,按名称绑定.
     * @return 分页查询结果, 附带结果列表及所有查询时的参数.
     */
//    public org.springside.modules.orm.Page<T> findPage(
//            final org.springside.modules.orm.Page<T> page, final String hql,
//            final Map<String, Object> values) {
//        Assert.notNull(page, "page不能为空");
//
//        Query q = createQuery(hql, values);
//
//        if (page.isAutoCount()) {
//            long totalCount = countHqlResult(hql, values);
//            page.setTotalCount(totalCount);
//        }
//
//        setPageParameter(q, page);
//
//        List result = q.list();
//        page.setResult(result);
//        return page;
//    }

    /**
     * 按Criteria分页查询.
     *
     * @param page       分页参数.
     * @param criterions 数量可变的Criterion.
     * @return 分页查询结果.附带结果列表及所有查询时的参数.
     */
//    public org.springside.modules.orm.Page<T> findPage(
//            final org.springside.modules.orm.Page<T> page,
//            final Criterion... criterions) {
//        Assert.notNull(page, "page不能为空");
//
//        Criteria c = createCriteria(criterions);
//
//        if (page.isAutoCount()) {
//            int totalCount = countCriteriaResult(c);
//            page.setTotalCount(totalCount);
//        }
//
//        setPageParameter(c, page);
//        List result = c.list();
//        page.setResult(result);
//        return page;
//    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
//    protected Query setPageParameter(final Query q,
//                                     final org.springside.modules.orm.Page<T> page) {
//        // hibernate的firstResult的序号从0开始
//        q.setFirstResult(page.getFirst() - 1);
//        q.setMaxResults(page.getPageSize());
//        return q;
//    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
//    protected Criteria setPageParameter(final Criteria c,
//                                        final org.springside.modules.orm.Page<T> page) {
//        // hibernate的firstResult的序号从0开始
//        c.setFirstResult(page.getFirst() - 1);
//        c.setMaxResults(page.getPageSize());
//
//        if (page.isOrderBySetted()) {
//            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
//            String[] orderArray = StringUtils.split(page.getOrder(), ',');
//
//            Assert.isTrue(orderByArray.length == orderArray.length,
//                    "分页多重排序参数中,排序字段与排序方向的个数不相等");
//
//            for (int i = 0; i < orderByArray.length; i++) {
//                if (org.springside.modules.orm.Page.ASC.equals(orderArray[i])) {
//                    c.addOrder(Order.asc(orderByArray[i]));
//                } else {
//                    c.addOrder(Order.desc(orderByArray[i]));
//                }
//            }
//        }
//        return c;
//    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p/>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(final String hql, final Object... values) {
        Long count = 0L;
        String fromHql = hql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");

        String countHql = "select count(*) " + fromHql;

        try {
            count = findUnique(countHql, values);
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }
        return count;
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p/>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(final String hql,
                                  final Map<String, Object> values) {
        Long count = 0L;
        String fromHql = hql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");

        String countHql = "select count(*) " + fromHql;

        try {
            count = findUnique(countHql, values);
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }

        return count;
    }

    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
//    protected int countCriteriaResult(final Criteria c) {
//        CriteriaImpl impl = (CriteriaImpl) c;
//
//        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
//        Projection projection = impl.getProjection();
//        ResultTransformer transformer = impl.getResultTransformer();
//
//        List<CriteriaImpl.OrderEntry> orderEntries = null;
//        try {
//            orderEntries = (List) ReflectionUtils.getFieldValue(impl,
//                    "orderEntries");
//            ReflectionUtils
//                    .setFieldValue(impl, "orderEntries", new ArrayList());
//        } catch (Exception e) {
//            logger.error("不可能抛出的异常:{}", e.getMessage());
//        }
//
//        // 执行Count查询
//        int totalCount = (Integer) c.setProjection(Projections.rowCount())
//                .uniqueResult();
//
//        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
//        c.setProjection(projection);
//
//        if (projection == null) {
//            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
//        }
//        if (transformer != null) {
//            c.setResultTransformer(transformer);
//        }
//        try {
//            ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
//        } catch (Exception e) {
//            logger.error("不可能抛出的异常:{}", e.getMessage());
//        }
//
//        return totalCount;
//    }

    /**
     * 按HQL查询唯一对象.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public <X> X findUnique(final String hql, final Object... values) {
        return (X) createQuery(hql, values).uniqueResult();
    }
}
