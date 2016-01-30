package com.dc.esb.servicegov.service.support;

import org.apache.commons.lang.StringUtils;

/**
 * Created by vincentfxz on 15/7/12.
 */
public class Constants {
    public static final String BANK_NAME="ChangShu";

    public static final String STATE_PASS = "1";
    public static final String STATE_UNPASS = "2";

    public static final String DELTED_FALSE = "0";//0:未删除，1：已删除
    public static final String DELTED_TRUE = "1";

    public static final String DEFAULT_GENERATOR_ID = "0";//默认标准XML拆组包生成器

    public static final String INTERFACE_STATUS_TC = "0";//0投产  1废弃
    public static final String INTERFACE_STATUS_FQ = "1";
    public static String getInterfaceStatus(String stateName){
        if(StringUtils.isNotEmpty(stateName)){
            if("废弃".equalsIgnoreCase(stateName)){
                return INTERFACE_STATUS_FQ;
            }else{
                return INTERFACE_STATUS_TC;
            }
        }else{
            return INTERFACE_STATUS_TC;
        }
    }


    public static final String INVOKE_TYPE_CONSUMER = "1";//接口映射类型，1：消费者，0：提供者
    public static final String INVOKE_TYPE_PROVIDER = "0";
    public static String getInvokeType(String name){
        if(StringUtils.isNotEmpty(name)){
            if("consumer".equalsIgnoreCase(name)){
                return INVOKE_TYPE_CONSUMER;
            }else{
                return INVOKE_TYPE_PROVIDER;
            }
        }else{
            return INVOKE_TYPE_PROVIDER;
        }
    }

    public static final String INVOKE_TYPE_STANDARD_Y = "0";//是否标准接口，1：否，0：是,99：未知
    public static final String INVOKE_TYPE_STANDARD_N = "1";
    public static final String INVOKE_TYPE_STANDARD_U = "99";

    public static final String IDA_STATE_COMMON = "0";//0:普通，1：导出使用
    public static final String IDA_STATE_DISABLE = "1";

    public static class ElementAttributes{
        public static final String ROOT_NAME = "root";
        public static final String ROOT_ALIAS = "根节点";
        public static final String ROOT_XPATH = "/";

        public static final String REQUEST_NAME = "request";
        public static final String REQUEST_ALIAS = "请求报文体";
        public static final String REQUEST_XPATH = "/request";

        public static final String RESPONSE_NAME = "response";
        public static final String RESPONSE_ALIAS = "响应报文体";
        public static final String RESPONSE_XPATH = "/response";
    }

    public static final String EXCEL_TEMPLATE_SERVICE = Constants.class.getResource("/").getPath() + "/template/excel_service_template.xls";
    public static final String EXCEL_TEMPLATE_SERVICE_VIEW = Constants.class.getResource("/").getPath() + "/template/excel_service_view_template.xls";
    public static final String EXCEL_TEMPLATE_INTERFACE = Constants.class.getResource("/").getPath() + "/template/excel_interface_template.xls";
    public static final String EXCEL_TEMPLATE_SYSTEM_REUSERATE = Constants.class.getResource("/").getPath() + "/template/excel_system_reuserate_template.xls";
    public static final String EXCEL_TEMPLATE_SERVICE_REUSERATE = Constants.class.getResource("/").getPath() + "/template/excel_service_reuserate_template.xls";
    public static final String EXCEL_TEMPLATE_RELEASE_COUNT = Constants.class.getResource("/").getPath() + "/template/excel_release_count_template.xls";
    public static final String EXCEL_TEMPLATE_RELEASE_STATE = Constants.class.getResource("/").getPath() + "/template/excel_release_state_template.xls";
    public static final String EXCEL_TEMPLATE_DATA_DICTIONARY= Constants.class.getResource("/").getPath() + "/template/excel_data_dictionary_template.xls";

    public static class Operation {
        public static final String OPT_STATE_UNAUDIT = "0";  //0.服务定义 1：审核通过，2：审核不通过, 3:已发布 4:已上线 5 已下线 6待审核 7修订 8下线 9废弃
        public static final String OPT_STATE_PASS = "1";
        public static final String OPT_STATE_UNPASS = "2";
        public static final String OPT_STATE_REQUIRE_UNAUDIT = "6";
        public static final String OPT_STATE_REVISE = "7";
        public static final String OPT_STATE_QUIT = "8";
        public static final String OPT_STATE_ABANDONED = "9";

        public static final String LIFE_CYCLE_STATE_PUBLISHED = "3";
        public static final String LIFE_CYCLE_STATE_ONLINE = "4";
        @Deprecated
        public static final String LIFE_CYCLE_STATE_DISCHARGE = "5";

        public static String getStateName(String state){
            if(OPT_STATE_UNAUDIT.equals(state)){
                return "服务定义";
            }
            if(OPT_STATE_REQUIRE_UNAUDIT.equals(state)){
                return "待审核";
            }
            if(OPT_STATE_PASS.equals(state)){
                return "审核通过";
            }
            if(OPT_STATE_UNPASS.equals(state)){
                return "审核不通过";
            }
            if(LIFE_CYCLE_STATE_PUBLISHED.equals(state)){
                return "已发布";
            }
            if(LIFE_CYCLE_STATE_ONLINE.equals(state)){
                return "已上线";
            }
            if(LIFE_CYCLE_STATE_DISCHARGE.equals(state)){
                return "已下线";
            }
            if(OPT_STATE_ABANDONED.equals(state)){
                return "废弃";
            }
            return "";
        }
        public static String getState(String stateName){
            if("服务定义".equals(stateName)){
                return OPT_STATE_UNAUDIT;
            }
            if("待审核".equals(stateName)){
                return OPT_STATE_REQUIRE_UNAUDIT;
            }
            if("审核通过".equals(stateName)){
                return OPT_STATE_PASS;
            }
            if("审核不通过".equals(stateName)){
                return OPT_STATE_UNPASS;
            }
            if("已发布".equals(stateName)){
                return LIFE_CYCLE_STATE_PUBLISHED;
            }
            if("已上线".equals(stateName)){
                return LIFE_CYCLE_STATE_ONLINE;
            }
            if("已下线".equals(stateName)){
                return LIFE_CYCLE_STATE_DISCHARGE;
            }
            if("废弃".equals(stateName)){
                return  OPT_STATE_ABANDONED;
            }
            return OPT_STATE_UNAUDIT;//默认服务定义状态
        }
//    	public static final String LIFE_CYCLE_STATE_TEST = "3";//测试
//    	public static final String LIFE_CYCLE_STATE_STOP = "4";//停止
//    	public static final String LIFE_CYCLE_STATE_MATAIN = "5";//维护
//    	public static final String LIFE_CYCLE_STATE_BASELINE = "6";//基线
//    	public static final String LIFE_CYCLESTATE_EXPIRE = "7";//失效
//    	public static final String OPT_STATE_UNPASS = "8";


    }

    public static class Version {
        public static final String TYPE_ELSE = "0";  //0：非基线，1：基线
        public static final String TYPE_BASELINE = "1";

        public static final String STATE_USING = "0";//0:生效，1：废弃
        public static final String STATE_DROPPED = "1";

        public static final String TARGET_TYPE_BASELINE = "0";//基线
        public static final String TARGET_TYPE_OPERATION = "1";//场景
        public static final String TARGET_TYPE_PC = "2";//公共代码
        public static final String TARGET_TYPE_INTERFACE = "3";//接口
        public static final String TARGET_TYPE_METADATA = "4";//元数据

        public static final String OPT_TYPE_ADD = "0"; //操作类型，0：新增，1：修改，2：删除,3:发布
        public static final String OPT_TYPE_EDIT = "1";
        public static final String OPT_TYPE_DELETE = "2";
        public static final String OPT_TYPE_RELEASE = "3";

        public static final String COMPARE_TYPE0 = "0";//对比类型：0：当前版本VS历史版本， 1：历史版本VS历史版本,2:历史版本VS当前版本
        public static final String COMPARE_TYPE1 = "1";
        public static final String COMPARE_TYPE2 = "2";
    }

    public static class Metadata {
        public static final String STATUS_UNAUDIT = "未审核";
        public static final String STATUS_FORMAL = "正式";
        public static final String STATUS_OUTDATED = "过时";

        public static final String ARRAY_TYPE = "Array";
        public static final String STRUCT_TYPE = "Struct";
    }

    public static class ServiceHead{
        public static final String DEFAULT_SYSHEAD_ID = "SYS_HEAD";
        public static final String DEFAULT_APPHEAD_ID = "APP_HEAD";
        public static final String DEFAULT_HEAD_ID = "SYS_HEAD,APP_HEAD";

        public static final String REQUIRED_Y = "Y";
        public static final String REQUIRED_N = "N";
    }
    public static class ServiceCategory{
        public static final Integer CATEGORY_CHILD_LENGTH = 5;//服务分类id长度
        public static final Integer CATEGORY_PARENT_LENGTH = 4;//服务大类id长度
    }
}
