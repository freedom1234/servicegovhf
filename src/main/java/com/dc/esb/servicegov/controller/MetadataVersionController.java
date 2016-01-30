package com.dc.esb.servicegov.controller;

import com.dc.esb.servicegov.entity.MetadataVersion;
import com.dc.esb.servicegov.service.impl.MetadataVersionServiceImpl;
import com.dc.esb.servicegov.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/1/4.
 */
@Controller
@RequestMapping("/metadataVersion")
public class MetadataVersionController {
    @Autowired
    MetadataVersionServiceImpl metadataVersionService;

    @RequestMapping(value = "/releasePage")
    public ModelAndView releasePage(){//数据字典发布页面
        ModelAndView mv = new ModelAndView("metadata/metadata_release");
        String lastVersionNo = metadataVersionService.getLastVersion();
        mv.addObject("lastVersionNo", lastVersionNo);
        return mv;
    }

    @RequestMapping(value = "/release")
    public @ResponseBody
    boolean release(String versionNo, String versionDesc) {//保存发布版本
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        MetadataVersion metadataVersion = new MetadataVersion();
        metadataVersion.setId(UUID.randomUUID().toString());
        metadataVersion.setVersionNo(versionNo);
        metadataVersion.setVersionDesc(versionDesc);
        metadataVersion.setOptUser(userName);
        metadataVersion.setOptDate(DateUtils.format(new Date()));

        metadataVersionService.save(metadataVersion);
        return true;
    }
}
