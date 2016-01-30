package com.dc.esb.servicegov.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.esb.servicegov.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.esb.servicegov.dao.impl.CategoryWordDAOImpl;
import com.dc.esb.servicegov.dao.support.HibernateDAO;
import com.dc.esb.servicegov.entity.CategoryWord;
import com.dc.esb.servicegov.service.CategoryWordService;
import com.dc.esb.servicegov.service.support.BaseService;

@Service
@Transactional
public class CategoryWordServiceImpl extends AbstractBaseService<CategoryWord, String> implements CategoryWordService{
	
	@Autowired
	private CategoryWordDAOImpl categoryWordDAOImpl;
	


	@Override
	public HibernateDAO getDAO() {
		return categoryWordDAOImpl;
	}

	/**
	 * TODO 已经存在的方法，去掉
	 * @param Id
	 * @return
	 */
//	@Override
//	public List<CategoryWord> getById(String Id){
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("Id", Id);
//		return categoryWordDAOImpl.findBy(params);
//	}
	/**
	 * TODO 已经存在的方法getAll，去掉
	 * @param Id
	 * @return
	 */
//	public List<CategoryWord> getAllCategory(){
//		return categoryWordDAOImpl.getAll();
//	}
	
	public List<CategoryWord> getByEnglishWord(String englishWord) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("englishWord", englishWord);
		return findBy(params);
	}

	public List<CategoryWord> getByChineseWord(String chineseWord) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("chineseWord", chineseWord);
		return findBy(params);
	}


	public List<CategoryWord> getByEsglisgAb(String esglisgAb) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("esglisgAb", esglisgAb);
		return findBy(params);
	}


	public List<CategoryWord> getByRemark(String remark) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("remark", remark);
		return findBy(params);
	}

	public List<CategoryWord> getByPotUser(String potUser) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("potUser", potUser);
		return findBy(params);
	}

	public List<CategoryWord> getByPotDate(String potDate) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("potDate", potDate);
		return findBy(params);
	}

	/**
	 * TODO 已经存在的方法，save 去掉
	 * @param categoryWord
	 * @return
	 */
//	public boolean addCategoryWord(CategoryWord categoryWord){
//		save(categoryWord);
//		return true;
//	}
	/**
	 * TODO 已经存在的方法，save 去掉
	 * @param categoryWord
	 * @return
	 */
//	public boolean modifyCategoryWord(CategoryWord categoryWord){
//		categoryWordDAOImpl.save(categoryWord);
//		return true;
//	}

	/**
	 * TODO 已经存在的方法，deleteById 去掉
	 * @param categoryWord
	 * @return
	 */
//	public void deleteCategoryWord(int id){
//		categoryWordDAOImpl.delete(id);
//	}

	/**
	 * 前端唯一性验证
	 * @param esglisgAb
	 * @return
	 */
	public boolean uniqueValid(String esglisgAb) {
		CategoryWord entity = findUniqueBy("esglisgAb",esglisgAb);
		if (entity != null) {
			return false;
		}
		return true;
	}

}
