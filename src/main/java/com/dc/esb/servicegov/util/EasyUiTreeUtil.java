package com.dc.esb.servicegov.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class EasyUiTreeUtil {
		private static EasyUiTreeUtil instance;

	public static EasyUiTreeUtil getInstance() {
		return instance == null ? new EasyUiTreeUtil() : instance;
	}
	//通用获取对象某个属性值
		public String getFieldValue(Object t, String fieldName){
			Field fields[]=t.getClass().getDeclaredFields();//获得对象所有属性
			for(Field field:fields){
				if(fieldName.equals(field.getName())){
					try {
						//设置访问权限
						field.setAccessible(true);
						if(field.get(t) != null){
							return field.get(t).toString();
						}
						return null;
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return "";
		}
	//将一个对象转换为treenode
	public TreeNode convertTreeNode(Object obj, Map<String,String > fields){
		if(obj != null){
			TreeNode t = new TreeNode();
			String id = fields.get("id");
			String text = fields.get("text");
			String iconCls = fields.get("iconCls");
			String checked = fields.get("checked");
			String state = fields.get("state");
			String attributes = fields.get("attributes");
			String target = fields.get("target");
			String parentId = fields.get("parentId");
			String append1 = fields.get("append1");
			String append2 = fields.get("append2");
			String append3 = fields.get("append3");
			String append4 = fields.get("append4");
			String append5 = fields.get("append5");
			String append6 = fields.get("append6");
			String append7 = fields.get("append7");
			String append8 = fields.get("append8");
			String append9 = fields.get("append9");
			String append10 = fields.get("append10");

			t.id = StringUtils.isNotEmpty(id)? getFieldValue(obj, id): getFieldValue(obj, "id");
			t.text = StringUtils.isNotEmpty(text)? getFieldValue(obj, text): getFieldValue(obj, "text");
			t.iconCls = StringUtils.isNotEmpty(iconCls)? getFieldValue(obj, iconCls): getFieldValue(obj, "iconCls");
			t.checked = StringUtils.isNotEmpty(checked)? getFieldValue(obj, checked): getFieldValue(obj, "checked");
			t.state = StringUtils.isNotEmpty(state)? getFieldValue(obj, state): null;
			t.attributes= StringUtils.isNotEmpty(attributes)? getFieldValue(obj, attributes): getFieldValue(obj, "attributes");
			t.target = StringUtils.isNotEmpty(target)? getFieldValue(obj, id): getFieldValue(obj, "target");
			t.parentId = StringUtils.isNotEmpty(parentId)? getFieldValue(obj, parentId): getFieldValue(obj, "parentId");

			t.append1 = StringUtils.isNotEmpty(append1)? getFieldValue(obj, append1): null;
			t.append2 = StringUtils.isNotEmpty(append2)? getFieldValue(obj, append2): null;
			t.append3 = StringUtils.isNotEmpty(append3)? getFieldValue(obj, append3): null;
			t.append4 = StringUtils.isNotEmpty(append4)? getFieldValue(obj, append4): null;
			t.append5 = StringUtils.isNotEmpty(append5)? getFieldValue(obj, append5): null;
			t.append6 = StringUtils.isNotEmpty(append6)? getFieldValue(obj, append6): null;
			t.append7 = StringUtils.isNotEmpty(append7)? getFieldValue(obj, append7): null;
			t.append8 = StringUtils.isNotEmpty(append8)? getFieldValue(obj, append8): null;
			t.append9 = StringUtils.isNotEmpty(append9)? getFieldValue(obj, append9): null;
			t.append10 = StringUtils.isNotEmpty(append10)? getFieldValue(obj, append10): null;
			return t;
		}
		return null;
	}
	
	public void genderTreeNode(TreeNode t , List<TreeNode> list){
		if(list != null && list.size() > 0){
			for(TreeNode child : list){
				if(StringUtils.isNotEmpty(child.parentId)){
					if(child.parentId.equals(t.id)){
						if(t.children == null){
							List<TreeNode> children = new ArrayList<TreeNode>();
							t.children = children;
						}
						t.children.add(child);
						//t.state = "closed";
						genderTreeNode(child, list);
					}
				}
			}
		}
		
	}
	
	public <T> List<TreeNode> convertTree(List<T> list , Map<String, String> fields){
		if(list != null && list.size() > 0){
			List<TreeNode> tmp = new ArrayList<TreeNode>();
			List<TreeNode> result = new ArrayList<TreeNode>();
			for(int i = 0; i< list.size(); i++){
				Object obj = list.get(i);
				if(obj == null){
					continue;
				}
				TreeNode t;
				if(obj instanceof  TreeNode){
					t = (TreeNode)obj;
				}else{
					t = convertTreeNode(obj, fields);
				}
				tmp.add(t);
				if(StringUtils.isEmpty(t.parentId) || (StringUtils.isNotEmpty(t.parentId) && t.getParentId().equals("/")) ){
					result.add(t);
				}
			}
			
			for(TreeNode t : result){
				genderTreeNode(t, tmp);
			}
			return result;
		}
		return null;
	}
}
