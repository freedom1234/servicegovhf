package com.dc.esb.servicegov.util;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TreeNode implements Serializable {
    String id;
    String text;
    String iconCls;
    String checked;
    String state;
    String attributes;
    String target;
    String parentId;
    List<TreeNode> children;
    String append1;
    String append2;
    String append3;
    String append4;
    String append5;
    String append6;
    String append7;
    String append8;
    String append9;
    String append10;
    String click;

    public TreeNode() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getAppend1() {
        return append1;
    }

    public void setAppend1(String append1) {
        this.append1 = append1;
    }

    public String getAppend2() {
        return append2;
    }

    public void setAppend2(String append2) {
        this.append2 = append2;
    }

    public String getAppend3() {
        return append3;
    }

    public void setAppend3(String append3) {
        this.append3 = append3;
    }

    public String getAppend4() {
        return append4;
    }

    public void setAppend4(String append4) {
        this.append4 = append4;
    }

    public String getAppend5() {
        return append5;
    }

    public void setAppend5(String append5) {
        this.append5 = append5;
    }

    public String getAppend6() {
        return append6;
    }

    public void setAppend6(String append6) {
        this.append6 = append6;
    }

    public String getAppend7() {
        return append7;
    }

    public void setAppend7(String append7) {
        this.append7 = append7;
    }

    public String getAppend8() {
        return append8;
    }

    public void setAppend8(String append8) {
        this.append8 = append8;
    }

    public String getAppend9() {
        return append9;
    }

    public void setAppend9(String append9) {
        this.append9 = append9;
    }

    public String getAppend10() {
        return append10;
    }

    public void setAppend10(String append10) {
        this.append10 = append10;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    @Override
    public boolean equals(Object obj){
        if(obj != null){
            if(obj instanceof  TreeNode){
                TreeNode tn = (TreeNode)obj;
                if(StringUtils.isNotEmpty(tn.id) && tn.id.equals(this.id)){
                    if(StringUtils.isNotEmpty(tn.text) && tn.text.equals(this.text)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
