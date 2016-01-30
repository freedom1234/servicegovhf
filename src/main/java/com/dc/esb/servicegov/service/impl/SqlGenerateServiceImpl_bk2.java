package com.dc.esb.servicegov.service.impl;

import com.dc.esb.servicegov.util.ExcelTool;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by kongxfa on 2016/1/11.
 */
//@Component
public class SqlGenerateServiceImpl_bk2 {

    private Log log = LogFactory.getLog(SqlGenerateServiceImpl_bk2.class);

    private static int jycodeCol = 0;
    private static int jynameCol = 1;
    private static int serviceidCol = 5;
    private static int adapterCol = 10;
    private static int connectorCol = 11;
    private static int frame_inCol = 15;
    private static int frame_outCol = 12;
    private static int channelCol = 12;
    private static int systemCol = 14;
    private static int ifuseCol = 16;

    private static String path = SqlGenerateServiceImpl_bk2.class.getResource("/").getPath() + "/sqlgenerator" + new Date().getTime();


    public String parse(Workbook workbook) {

        try {
            Sheet sheet = workbook.getSheetAt(0);
            dealColumn(sheet);

            readExcel(sheet, path);

        } catch (Exception e) {
            log.error(e, e);
        }
        return path;
    }


    /*
     * 读取excel内容,生成sql脚本
	 */
    public void readExcel(Sheet sheet, String path) throws Exception {
        int n = sheet.getLastRowNum();

        //全部的信息（包括复用与没复用）
        Set<String> adapterSet = new TreeSet<String>();
        Set<String> frameSet = new TreeSet<String>();
        Set<String> channelSet = new TreeSet<String>();
        Set<String> systemSet = new TreeSet<String>();
        //复用的信息
        Set<String> adapterNoUse = new TreeSet<String>();
        Set<String> frameNoUse = new TreeSet<String>();
        Set<String> systemNoUse = new TreeSet<String>();

        StringBuffer strs = new StringBuffer();
//        StringBuffer strsBack = new StringBuffer();

        for (int i = 1; i <= n; i++) {
//            Cell[] cell = sheet.getRow(i);
            Row row = sheet.getRow(i);
            String jycode = row.getCell(jycodeCol).getStringCellValue();//交易码
            String jyname = row.getCell(jynameCol).getStringCellValue();//接口名称
            String serviceid = row.getCell(serviceidCol).getStringCellValue();//服务id
            if (serviceid == null || "".equals(serviceid)) {
                break;
            }
            String adapter = row.getCell(adapterCol).getStringCellValue();//接出协议
            String connector = row.getCell(connectorCol).getStringCellValue();//接入协议
            String frame_in = row.getCell(frame_inCol).getStringCellValue();//适配流程
            String frame_out = row.getCell(frame_outCol).getStringCellValue();//适配流程
            String channel = row.getCell(channelCol).getStringCellValue();//接入渠道
            String system = row.getCell(systemCol).getStringCellValue();//提供方系统
            String ifuse = row.getCell(ifuseCol).getStringCellValue();//是否复用
            serviceid = serviceid.replaceAll(" ", "");
            adapter = adapter.replaceAll(" ", "");
            connector = connector.replaceAll(" ", "");
            frame_in = frame_in.replaceAll(" ", "");
            frame_out = frame_out.replaceAll(" ", "");
            channel = channel.replaceAll(" ", "");
            system = system.replaceAll(" ", "");
            adapterSet.add(adapter);
            adapterSet.add(connector);
            frameSet.add(frame_in);
            frameSet.add(frame_out);
            channelSet.add(channel);
            systemSet.add(system);
            if ("Y".equals(ifuse)) {
                adapterNoUse.add(adapter);
                frameNoUse.add(frame_out);
                systemNoUse.add(system);
            } else {
                String sql = createBussService(jycode, jyname, serviceid, adapter, frame_out, channel, system);
//                String sqlBack = createBussServiceBack(jycode, jyname, serviceid, adapter, frame_out, channel, system);
                strs.append(sql);
//                strsBack.append(sqlBack);
            }
        }
        strs.append(" commit; \r\n");
//        strsBack.append(" commit; \r\n");

        writeFile(strs.toString(), "05_esbdata_dml_bussservice");
//        writeFileBack(strsBack.toString(), "05_esbdata_dml_bussservice_roolback");

        for (String adapter : adapterNoUse) {
            adapterSet.remove(adapter);
        }
        for (String frame : frameNoUse) {
            frameSet.remove(frame);
        }
        for (String system : systemNoUse) {
            systemSet.remove(system);
        }

//        //协议脚本、服务脚本、服务系统脚本、适配流程脚本 --需要连接数据库
//        getConnection(url, username, password);
//
//        createProtocol(adapterSet);
//        createProtocolBack(adapterSet);
//
//        createChannel(channelSet);
//        createChannelBack(channelSet);
//
//        createServiceSystem(systemSet);
//        createServiceSystemBack(systemSet);
//
//        createAdapterFrame(frameSet);
//        createAdapterFrameBack(frameSet);
//
//        if (conn != null) {
//            conn.close();
//        }
    }

    public void createProtocol(Set<String> adapterSet){
        StringBuffer sql = new StringBuffer();
        StringBuffer strs = new StringBuffer();
        for (String adapter : adapterSet) {
            strs.append("----===================================" + adapter + "============================================================================= \r\n");
//            sql.append(" select 'insert into PROTOCOLBIND (PROTOCOLID, BINDTYPE, BINDURI, REQUESTADAPTER, RESPONSEADAPTER, THREADPOOL) values (''' ||");
//            sql.append("       t.protocolid || ''',''' || t.bindtype || ''',''' || t.binduri ||");
//            sql.append("       ''',''' || t.requestadapter || ''',''' || t.responseadapter ||");
//            sql.append("       ''','''');' as name");
//            sql.append("  from PROTOCOLBIND t");
//            sql.append("  where t.protocolid = '" + adapter + "' ");
//            sql.append(" union all");
//            sql.append(" select 'insert into BINDMAP (SERVICEID, STYPE, LOCATION, VERSION, PROTOCOLID, MAPTYPE) values (''' ||");
//            sql.append("       t.serviceid || ''',''' || t.stype || ''',''' || t.location ||");
//            sql.append("       ''','''',''' || t.protocolid || ''',''' || t.maptype || ''');' as name");
//            sql.append("  from BINDMAP t");
//            sql.append("  where t.protocolid = '" + adapter + "'");
//            sql.append("   and t.serviceid in ('local_in', 'local_out')");
//            strs.append(querySql(sql.toString()));
            strs.append("insert into PROTOCOLBIND (PROTOCOLID, BINDTYPE, BINDURI, REQUESTADAPTER, RESPONSEADAPTER, THREADPOOL) \r\n");
            strs.append("values ('nbs_connector', 'TCPChannelConnector', " +
                    "'<protocol.tcp protocolName=\"TCPChannelConnector\" id=\"nbs_connector\" mode=\"synchronous\" ioDirection=\"DataIn/DataOut\" side=\"server\"><common connectMode=\"short\" threadsCount=\"20\" port=\"10001\" /><request policy=\"length:s=0,e=9\" encoding=\"UTF-8\" readTimeout=\"40000\" action=\"toBytes\" /><response policy=\"length:s=0,e=9\" encoding=\"UTF-8\" action=\"toBytes\" /><advanced /></protocol.tcp>', " +
                    "'default_protocolAdapter_req_in', 'default_protocolAdapter_res_in', null);");
            strs.append("\r\n");
            sql.setLength(0);
        }
        strs.append(" commit; \r\n");
        writeFile(strs.toString(), "01_esbdata_dml_protocol");
    }

    /*
     * 生成服务脚本
	 */
    public String createBussService(String jycode, String name, String serviceid, String adapter, String frame, String channel,
                                    String system) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer();
        sql.append("-----===================================================================================" + jycode + name + serviceid + "=================================================================================================================\r\n");
        //insert into SERVICES
        sql.append(" insert into SERVICES (NAME, INADDRESSID, OUTADDRESSID, TYPE, SESSIONCOUNT, DELIVERYMODE,NODEID, LOCATION, ROUTERABLE) values( ");
        sql.append(" '" + serviceid + "', '4cc95abd8b9cba4705f290b39dee3d6a', '70022232c41f240289cac75d5b3e3884', 'SERVICE', 1, '2', 'null', 'local_out', null); \r\n");
        //insert into BUSSSERVICES
        sql.append(" insert into BUSSSERVICES (SERVICEID, CATEGORY, METHODNAME, ISARG, DESCRIPTION) values ( ");
        sql.append(" '" + serviceid + "', '', null, 'false', null);\r\n");
        //insert into SERVICEINFO
        sql.append(" insert into SERVICEINFO (SERVICEID, SERVICETYPE, CONTRIBUTION, PREPARED, GROUPNAME, LOCATION, DESCRIPTION, MODIFYTIME, ADAPTERTYPE, ISCREATE) values ( ");
        sql.append(" '" + serviceid + "', 'BUSS', '" + serviceid + "', 'false', null, 'local_out', '', null, null, 'true');\r\n");
        //insert into BINDMAP
        sql.append(" insert into BINDMAP (SERVICEID, STYPE, LOCATION, VERSION, PROTOCOLID, MAPTYPE) values ( ");
        sql.append(" '" + serviceid + "', 'SERVICE','local_out', '0', '" + adapter + "', 'request');\r\n");
        //insert into DATAADAPTER
        sql.append(" insert into DATAADAPTER (DATAADAPTERID, DATAADAPTER, LOCATION, ADAPTERTYPE) values ( ");
        sql.append(" '" + serviceid + "','" + frame + "', 'local_out', 'OUT');\r\n");
        //insert into SERVICESYSTEMMAP
        sql.append(" insert into SERVICESYSTEMMAP (NAME, SERVICEID, ADAPTER) values ( ");
        sql.append(" '" + system + "', '" + serviceid + "','" + adapter + "');\r\n");
        //insert into DEPLOYMENTS
        sql.append(" insert into DEPLOYMENTS (ID,LOCATION,NAME,VERSION) values ( ");
        sql.append(" '0000','local_out','" + serviceid + "','0');\r\n");
        //update DEPLOYMENTS
        sql.append(" update DEPLOYMENTS set id=to_char((select max(to_number(id))+1 from deployments)) ");
        sql.append(" where name='" + serviceid + "' and id='0000';\r\n ");
        return sql.toString();
    }

    /*
    * 生成服务脚本 -回滚
    */
    public String createBussServiceBack(String jycode, String name, String serviceid, String adapter, String frame, String channel,
                                        String system) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer();
        sql.append("-----===================================================================================" + jycode + name + serviceid + "=================================================================================================================\r\n");
        // SERVICES name数据唯一
        sql.append(" delete from SERVICES where NAME='" + serviceid + "' and INADDRESSID='4cc95abd8b9cba4705f290b39dee3d6a' and OUTADDRESSID='70022232c41f240289cac75d5b3e3884' " +
                "and TYPE='SERVICE' and SESSIONCOUNT=1 and DELIVERYMODE='2' and LOCATION='LOCATION'; \r\n");
        // BUSSSERVICES表SERVICEID数据唯一
        sql.append(" delete from BUSSSERVICES where SERVICEID='" + serviceid + "' and ISARG='false'; \r\n");
        //SERVICEINFO SERVICEID数据唯一
        sql.append(" delete from SERVICEINFO where SERVICEID='" + serviceid + "' and SERVICETYPE='BUSS' and CONTRIBUTION='" + serviceid + "' " +
                "and PREPARED='false' and LOCATION='local_out' and ISCREATE='true'; \r\n");
        //
        sql.append(" delete from BINDMAP where SERVICEID='" + serviceid + "' and STYPE='SERVICE' and LOCATION='local_out'" +
                " and VERSION='0' PROTOCOLID='" + adapter + "' and MAPTYPE='request';\r\n");
        //DATAADAPTER主键：DATAADAPTERID
        sql.append(" delete from DATAADAPTER where DATAADAPTERID='" + serviceid + "' and DATAADAPTER='" + frame + "' and LOCATION='local_out' and ADAPTERTYPE='OUT'; \r\n ");
        //SERVICESYSTEMMAP 主键：NAME，SERVICEID
        sql.append(" delete from SERVICESYSTEMMAP where NAME='" + system + "' and SERVICEID='" + serviceid + "' and ADAPTER='" + adapter + "'; \r\n");
        //
        sql.append(" delete from DEPLOYMENTS where LOCATION='local_out' and NAME='" + serviceid + "' and VERSION='0';\r\n");
        return sql.toString();
    }



    public boolean writeFile(String content, String fileName) {
        fileName = path + File.separator + fileName + ".sql";
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file, true);
            out.write(content.getBytes("utf-8"));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error(e, e);
        }
        return true;
    }

    public boolean writeFileBack(String content, String fileName) {
        fileName = path + File.separator + "roolback" + File.separator + fileName + ".sql";
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file, true);
            out.write(content.getBytes("utf-8"));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error(e, e);
        }
        return true;
    }

    public void dealColumn(Sheet sheet) {
        Row row = sheet.getRow(0);
        int cols = row.getLastCellNum();
        for (int i = 0; i < cols; i++) {
            String content = ExcelTool.getInstance().getCellContent(row.getCell(i));
            if (StringUtils.isEmpty(content)) continue;

            if ("交易码".equals(content)) {
                jycodeCol = i;
            } else if ("接口名称".equals(content)) {
                jynameCol = i;
            } else if ("UAT状态".equals(content)) {
                continue;
            } else if ("UAT测试".equals(content)) {
                continue;
            } else if ("获取报文".equals(content)) {
                continue;
            } else if ("服务".equals(content)) {
                serviceidCol = i;
            } else if ("最终生成服务".equals(content)) {
                continue;
            } else if ("最终生成服务场景".equals(content)) {
                continue;
            } else if ("服务调用方".equals(content)) {
                continue;
            } else if ("服务提供方".equals(content)) {
                continue;
            } else if ("接出协议".equals(content)) {
                adapterCol = i;
            } else if ("接入协议".equals(content)) {
                connectorCol = i;
            } else if ("out适配流程".equals(content)) {
                frame_outCol = i;
            } else if ("接入渠道".equals(content)) {
                channelCol = i;
            } else if ("服务方系统".equals(content)) {
                systemCol = i;
            } else if ("in适配流程".equals(content)) {
                frame_inCol = i;
            } else if ("是否复用".equals(content)) {
                ifuseCol = i;
            }
        }
    }

}
