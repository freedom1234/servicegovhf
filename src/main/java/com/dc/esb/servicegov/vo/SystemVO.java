package com.dc.esb.servicegov.vo;

/**
 * Created by Administrator on 2015/12/15.
 */
public class SystemVO {
    private String systemId;
    private String systemChineseName;
    private String systemAb;
    private boolean checked;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemChineseName() {
        return systemChineseName;
    }

    public void setSystemChineseName(String systemChineseName) {
        this.systemChineseName = systemChineseName;
    }

    public String getSystemAb() {
        return systemAb;
    }

    public void setSystemAb(String systemAb) {
        this.systemAb = systemAb;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
