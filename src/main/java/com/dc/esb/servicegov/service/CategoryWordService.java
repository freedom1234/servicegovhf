package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.entity.CategoryWord;
import com.dc.esb.servicegov.service.support.BaseService;

import java.util.List;

public interface CategoryWordService extends BaseService<CategoryWord, String> {

//    public List<CategoryWord> getAllCategory();

    public List<CategoryWord> getByEnglishWord(String englishWord);

    public List<CategoryWord> getByChineseWord(String chineseWord);

    public List<CategoryWord> getByEsglisgAb(String esglisgAb);

    public List<CategoryWord> getByRemark(String remark);

    //TODO Pot 是什么鬼
    public List<CategoryWord> getByPotUser(String potUser);
    //TODO Pot 是什么鬼
    public List<CategoryWord> getByPotDate(String potDate);

//    public boolean addCategoryWord(CategoryWord categoryWord);

//    public boolean modifyCategoryWord(CategoryWord categoryWord);

//    public void deleteCategoryWord(int id);
}
