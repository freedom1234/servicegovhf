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
import java.sql.*;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by kongxfa on 2016/1/11.
 */
//@Component
public class SqlGenerateServiceImpl_bk {

    private Log log = LogFactory.getLog(SqlGenerateServiceImpl_bk.class);

    private static String url = "jdbc:oracle:thin:@192.168.123.175:1521:esbdb";
    private static String username = "esbdata";
    private static String password = "esbdata";
    //    private static String date1 =new SimpleDateFormat("yyyyMMdd").format(new Date());
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

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

    private static String path = SqlGenerateServiceImpl_bk.class.getResource("/").getPath() + "/sqlgenerator" + new Date().getTime();

    /*
     * 获取数据库连接
     */
    public Connection getConnection(String url, String username, String password) {
        try {
            //java驱动程序
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //连接数据库的url串,用户密码,用户名;
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


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
        StringBuffer strsBack = new StringBuffer();

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
                String sqlBack = createBussServiceBack(jycode, jyname, serviceid, adapter, frame_out, channel, system);
                strs.append(sql);
                strsBack.append(sqlBack);
            }
        }
        strs.append(" commit; \r\n");
        strsBack.append(" commit; \r\n");

        writeFile(strs.toString(), "05_esbdata_dml_bussservice");
        writeFileBack(strsBack.toString(), "05_esbdata_dml_bussservice_roolback");

        for (String adapter : adapterNoUse) {
            adapterSet.remove(adapter);
        }
        for (String frame : frameNoUse) {
            frameSet.remove(frame);
        }
        for (String system : systemNoUse) {
            systemSet.remove(system);
        }

        //协议脚本、服务脚本、服务系统脚本、适配流程脚本 --需要连接数据库
        getConnection(url, username, password);

        createProtocol(adapterSet);
        createProtocolBack(adapterSet);

        createChannel(channelSet);
        createChannelBack(channelSet);

        createServiceSystem(systemSet);
        createServiceSystemBack(systemSet);

        createAdapterFrame(frameSet);
        createAdapterFrameBack(frameSet);

        if (conn != null) {
            conn.close();
        }
    }

    /*
     * 生成协议脚本
	 */
    public void createProtocol(Set<String> set) throws Exception {
        StringBuffer sql = new StringBuffer();
        StringBuffer strs = new StringBuffer();
        for (String adapter : set) {
            strs.append("----===================================" + adapter + "============================================================================= \r\n");
            sql.append(" select 'insert into PROTOCOLBIND (PROTOCOLID, BINDTYPE, BINDURI, REQUESTADAPTER, RESPONSEADAPTER, THREADPOOL) values (''' ||");
            sql.append("       t.protocolid || ''',''' || t.bindtype || ''',''' || t.binduri ||");
            sql.append("       ''',''' || t.requestadapter || ''',''' || t.responseadapter ||");
            sql.append("       ''','''');' as name");
            sql.append("  from PROTOCOLBIND t");
            sql.append("  where t.protocolid = '" + adapter + "' ");
            sql.append(" union all");
            sql.append(" select 'insert into BINDMAP (SERVICEID, STYPE, LOCATION, VERSION, PROTOCOLID, MAPTYPE) values (''' ||");
            sql.append("       t.serviceid || ''',''' || t.stype || ''',''' || t.location ||");
            sql.append("       ''','''',''' || t.protocolid || ''',''' || t.maptype || ''');' as name");
            sql.append("  from BINDMAP t");
            sql.append("  where t.protocolid = '" + adapter + "'");
            sql.append("   and t.serviceid in ('local_in', 'local_out')");
            strs.append(querySql(sql.toString()));
            strs.append("\r\n");
            sql.setLength(0);
        }
        strs.append(" commit; \r\n");
        writeFile(strs.toString(), "01_esbdata_dml_protocol");
    }

    /*
     * 生成协议脚本 -回滚
	 */
    public void createProtocolBack(Set<String> set) throws Exception {
        StringBuffer sql = new StringBuffer();
        StringBuffer strs = new StringBuffer();
        for (String adapter : set) {
            strs.append("----===================================" + adapter + "============================================================================= \r\n");
            //BINDMAP外键是 PROTOCOLBIND的protocolid ????
            strs.append(" delete from BINDMAP where protocolid='" + adapter + "' and serviceid in ('local_in', 'local_out');\r\n");
            strs.append(" delete from PROTOCOLBIND where protocolid='" + adapter + "';\r\n");
        }
        strs.append(" commit; \r\n");
        writeFileBack(strs.toString(), "01_esbdata_dml_protocol_roolback");
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

    /*
     * 生成渠道脚本
	 */
    public void createChannel(Set<String> set) throws Exception {
        StringBuffer sql = new StringBuffer();
        StringBuffer strs = new StringBuffer();
        for (String channel : set) {
            strs.append("----===================================" + channel + "============================================================================= \r\n");
            sql.append("  select 'insert into SERVICEINFO (SERVICEID, SERVICETYPE, CONTRIBUTION, PREPARED, GROUPNAME, LOCATION, DESCRIPTION, MODIFYTIME, ADAPTERTYPE, ISCREATE) values (''' ||");
            sql.append("       t.serviceid || ''',''' || t.servicetype || ''',''' || t.contribution ||");
            sql.append("       ''',''' || t.prepared || ''',''' || t.groupname || ''',''' ||");
            sql.append("       t.location || ''',''' || t.description || ''',''' || t.modifytime ||");
            sql.append("       ''',''' || t.adaptertype || ''',''' || t.iscreate || '''); ' as name");
            sql.append("  from SERVICEINFO t");
            sql.append("  where t.serviceid = '" + channel + "' ");
            sql.append("  union all");
            sql.append("   select 'insert into SERVICES (NAME, INADDRESSID, OUTADDRESSID, TYPE, SESSIONCOUNT, DELIVERYMODE, NODEID, LOCATION, ROUTERABLE) values (''' ||");
            sql.append("         t.name || ''',''' || t.inaddressid || ''',''' || t.outaddressid ||");
            sql.append("         ''',''' || t.type || ''',' || t.sessioncount || ',''' ||");
            sql.append("         t.deliverymode || ''',''' || t.nodeid || ''',''' || location ||");
            sql.append("         ''','''');' as name");
            sql.append("    from SERVICES t");
            sql.append("   where t.name = '" + channel + "' ");
            sql.append("  union all ");
            sql.append("  select 'insert into BINDMAP (SERVICEID, STYPE, LOCATION, VERSION, PROTOCOLID, MAPTYPE)values (''' ||");
            sql.append("       t.serviceid || ''',''' || t.stype || ''',''' || t.location ||");
            sql.append("       ''',''' || t.version || ''',''' || t.protocolid || ''',''' ||");
            sql.append("       t.maptype || ''');' as name");
            sql.append("  from BINDMAP t");
            sql.append("  where t.serviceid = '" + channel + "'");
            sql.append("  union all");
            sql.append("  select 'insert into DATAADAPTER (DATAADAPTERID, DATAADAPTER, LOCATION, ADAPTERTYPE) values (''' ||");
            sql.append("         t.dataadapterid || ''',''' || t.dataadapter || ''',''' || t.location ||");
            sql.append("         ''',''' || t.adaptertype || ''')'");
            sql.append("    from DATAADAPTER t");
            sql.append("   where t.dataadapterid = '" + channel + "'");
            strs.append(querySql(sql.toString()));
            strs.append("\r\n");
            sql.setLength(0);
        }
        strs.append(" commit; \r\n");
        writeFile(strs.toString(), "02_esbdata_dml_channel");
    }

    /*
     * 生成渠道脚本 -回滚
	 */
    public void createChannelBack(Set<String> set) throws Exception {
        StringBuffer sql = new StringBuffer();
        StringBuffer strs = new StringBuffer();
        for (String channel : set) {
            strs.append("----===================================" + channel + "============================================================================= \r\n");
            //SERVICEINFO:serviceid数据唯一
            sql.append(" delete from SERVICEINFO where serviceid='"+channel+"';\r\n");
            //SERVICES:name数据唯一
            sql.append(" delete from SERVICES where name='"+channel+"'; \r\n");
            //BINDMAP：serviceid不唯一
//            sql.append(" delete from BINDMAP where serviceid='"+channel+"';\r\n");
            //DATAADAPTER:dataadapterid主键
            sql.append(" delete from DATAADAPTER where dataadapterid='"+channel+"';\r\n");
            strs.append(sql);
            sql.setLength(0);
        }
        strs.append(" commit; \r\n");
        writeFileBack(strs.toString(), "02_esbdata_dml_channel_roolback");
    }

    /*
	 * 生成服务系统脚本
	 */
    public void createServiceSystem(Set<String> set) throws Exception {
        StringBuffer sql = new StringBuffer();
        StringBuffer strs = new StringBuffer();
        for (String system : set) {
            strs.append("----===================================" + system + "============================================================================= \r\n");
            sql.append("   select 'insert into SERVICESYSTEM (NAME, DESCRIPTION, REALCHANNEL, ADAPTER) values (''' ||");
            sql.append("          t.name || ''',''' || t.description || ''',''' || t.realchannel ||");
            sql.append("          ''',''' || t.adapter || ''');' as name");
            sql.append("     from SERVICESYSTEM t");
            sql.append("    where t.name = '" + system + "'");
            strs.append(querySql(sql.toString()));
            strs.append("\r\n");
            sql.setLength(0);
        }
        strs.append(" commit; \r\n");
        writeFile(strs.toString(), "03_esbdata_dml_servicesystem");
    }

    /*
	 * 生成服务系统脚本 -回滚
	 */
    public void createServiceSystemBack(Set<String> set) throws Exception {
        StringBuffer strs = new StringBuffer();
        for (String system : set) {
            strs.append("----===================================" + system + "============================================================================= \r\n");
            //SERVICESYSTEM：name主键
            strs.append(" delete from SERVICESYSTEM where name='"+system+"';\r\n");
        }
        strs.append(" commit; \r\n");
        writeFileBack(strs.toString(), "03_esbdata_dml_servicesystem_roolback");
    }

    /*
	 * 生成适配流程脚本
	 */
    public void createAdapterFrame(Set<String> set) throws Exception {
        StringBuffer sql = new StringBuffer();
        StringBuffer strs = new StringBuffer();
        for (String frame : set) {
            strs.append("----===================================" + frame + "============================================================================= \r\n");
            sql.append("  select 'insert into ESB_ADAPTER_TEMPLATE (NAME, ADAPTERS, TYPE, REMARK, PROTOCOLADAPTER) values (''' ||");
            sql.append("         t.name || ''',''' || t.adapters || ''',''' || t.type || ''',''' ||");
            sql.append("         t.remark || ''',''' || t.protocoladapter || ''');' as name");
            sql.append("    from ESB_ADAPTER_TEMPLATE t");
            sql.append("   where t.name = '" + frame + "'");
            strs.append(querySql(sql.toString()));
            strs.append("\r\n");
            sql.setLength(0);
        }
        strs.append(" commit; \r\n");
        writeFile(strs.toString(), "04_esbdata_dml_adapterframe");
    }

    /*
    * 生成适配流程脚本 -回滚
    */
    public void createAdapterFrameBack(Set<String> set) throws Exception {
        StringBuffer strs = new StringBuffer();
        for (String frame : set) {
            strs.append("----===================================" + frame + "============================================================================= \r\n");
            //ESB_ADAPTER_TEMPLATE:name主键
            strs.append(" delete from ESB_ADAPTER_TEMPLATE where name='"+frame+"';\r\n");
        }
        strs.append(" commit; \r\n");
        writeFileBack(strs.toString(), "04_esbdata_dml_adapterframe_roolback");
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

    /*
	 * 查询sql语句
	 */
    public String querySql(String sql) {
        StringBuffer strs = new StringBuffer();
        try {
            stmt = conn.createStatement();
            System.out.println("sql语句：\n" + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                strs.append(rs.getString("name") + "\r\n");
            }
            rs.close();//关闭结果集
            stmt.close();//关闭结果集通道
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strs.toString();
    }

}
