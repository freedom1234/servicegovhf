package com.dc.esb.servicegov.util;

import java.util.List;

/**
 * Created by vincentfxz on 15/9/22.
 * 交易链路节点属性 的VO
 */
public class GraphColumn {
    private String id;
    private String datatype = "varchar";
    private List<Icon> icons;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public List<Icon> getIcons() {
        return icons;
    }

    public void setIcons(List<Icon> icons) {
        this.icons = icons;
    }

    public static class Icon {
        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
