package com.dc.esb.servicegov.service;

import com.dc.esb.servicegov.dao.support.Page;
import com.dc.esb.servicegov.service.support.BaseService;
import com.dc.esb.servicegov.util.TreeNode;
import com.dc.esb.servicegov.vo.ReleaseListVO;
import com.dc.esb.servicegov.vo.ReleaseVO;
import com.dc.esb.servicegov.vo.ReuseRateVO;

import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/8/14.
 */
public interface StatisticsService{
    public List<ReuseRateVO> getReuseRate(Map<String, String[]> values, Page page);
    public long getReuseRateCount(Map<String, String[]> values);
    public List<ReleaseVO> getReleaseCountVO(Map<String, String[]> values, Page page);
    public List<ReleaseVO> getReleaseStateVO(Map<String, String[]> values, Page page);
    public long getReleaseVOCount(Map<String, String[]> values);
    public List<TreeNode> getServiceReuseRate();
    public List<TreeNode> getServiceReuseRate2();
    public List<TreeNode> getServiceCategoryChildren(String serviceCategoryId);
}
