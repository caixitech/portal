package org.helloframework.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.helloframework.codegen.definition.ColumnInfo;
import org.helloframework.codegen.definition.TableInfo;

import java.io.*;
import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by macintosh
 */
public class MysqlCodeGen {


    private static List<String> packageNames = new ArrayList<String>();

    static {
        packageNames.add(".mapper");
        packageNames.add(".dao");
        packageNames.add(".dao.impl");
        packageNames.add(".pojo");
    }

    private static String basePath = "/Users/lanjian";
    private static String basePackageName = "com.iflytek.kuyin.tools.mv.process";

    public static void main(String[] args) throws Exception {

        String driver = "org.mariadb.jdbc.Driver";
        String url = "jdbc:mysql://172.31.4.48:3306/KuYinRing?autoReconnectForPools=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
        String user = "root";
        String password = "root";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        // 获取所有表名
        Statement statement = conn.createStatement();
        for (String packageName : packageNames) {
            String codePath = (basePackageName + packageName).replaceAll("\\.", "/");
            FileUtils.deleteDirectory(new File(basePath + "/mybatis/" + codePath));
            FileUtils.forceMkdir(new File(basePath + "/mybatis/" + codePath));
        }
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(URLDecoder.decode(MysqlCodeGen.class.getResource("/").getFile())));
        List<String> tables = tables(conn);
        for (String table : tables) {
            genCode(statement, table, cfg);
        }
        statement.close();
        conn.close();

    }

    public static String newName(String str) {
        if (str.startsWith("_")) {
            str = str.substring(1);
        }
        System.out.println(str);
        StringBuffer pojoName = new StringBuffer();
        String[] ws = str.split("_");
        for (int i = 0; i < ws.length; i++) {
            String w = ws[i];
            pojoName.append(w.substring(0, 1).toUpperCase() + w.substring(1));
        }
        return pojoName.toString();
    }

    private static void dao(Configuration cfg, TableInfo tableInfo) throws IOException, TemplateException {
        Template template = cfg.getTemplate("mysql/dao.ftl");
        String codePath = (basePackageName + ".dao").replaceAll("\\.", "/");
        FileOutputStream fos = new FileOutputStream(basePath + "/mybatis/" + codePath + "/I" + tableInfo.getPojoName() + "Dao.java");
        Writer out = new OutputStreamWriter(fos, "utf-8");
        Map data = new HashMap();
        data.put("pojoName", tableInfo.getPojoName());
        data.put("packageName", basePackageName);
        template.process(data, out);
        out.flush();
    }

    private static void mapper(Configuration cfg, TableInfo tableInfo) throws IOException, TemplateException {
        Template template = cfg.getTemplate("mysql/mapper.ftl");
        String codePath = (basePackageName + ".mapper").replaceAll("\\.", "/");
        FileOutputStream fos = new FileOutputStream(basePath + "/mybatis/" + codePath + "/" + tableInfo.getPojoName() + "Mapper.java");
        Writer out = new OutputStreamWriter(fos, "utf-8");
        Map data = new HashMap();
        data.put("pojoName", tableInfo.getPojoName());
        data.put("packageName", basePackageName);
        template.process(data, out);
        out.flush();
    }

    private static void daoImpl(Configuration cfg, TableInfo tableInfo) throws IOException, TemplateException {
        Template template = cfg.getTemplate("mysql/dao_impl.ftl");
        String codePath = (basePackageName + ".dao.impl").replaceAll("\\.", "/");
        FileOutputStream fos = new FileOutputStream(basePath + "/mybatis/" + codePath + "/" + tableInfo.getPojoName() + "DaoImpl.java");
        Writer out = new OutputStreamWriter(fos, "utf-8");
        Map data = new HashMap();
        data.put("pojoName", tableInfo.getPojoName());
        data.put("packageName", basePackageName);
        template.process(data, out);
        out.flush();
    }

    private static void pojo(Configuration cfg, TableInfo tableInfo) throws IOException, TemplateException {
        Template template = cfg.getTemplate("mysql/pojo.ftl");
        String codePath = (basePackageName + ".pojo").replaceAll("\\.", "/");
        FileOutputStream fos = new FileOutputStream(basePath + "/mybatis/" + codePath + "/" + tableInfo.getPojoName() + ".java");
        Writer out = new OutputStreamWriter(fos, "utf-8");
        Map data = new HashMap();
        data.put("pojoName", tableInfo.getPojoName());
        data.put("packageName", basePackageName);
        data.put("tableName", tableInfo.getTableName());
        data.put("columnInfos", tableInfo.getColumnInfos());
        template.process(data, out);
        out.flush();
    }

    private static void genCode(Statement statement, String table, Configuration cfg) throws SQLException, ClassNotFoundException, IOException, TemplateException {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(table);
        tableInfo.setPojoName(newName(table));
        ResultSet resultSet = statement.executeQuery("select * from " + table + " limit 1");
        // 获取列名
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i + 1);
            String dType = Class.forName(metaData.getColumnClassName(i + 1)).getSimpleName();
            columnInfos.add(new ColumnInfo(columnName, sqlType2javaType(metaData.getColumnType(i + 1), dType, columnName), newName(columnName)));
        }
        tableInfo.setColumnInfos(columnInfos);
        dao(cfg, tableInfo);
        daoImpl(cfg, tableInfo);
        mapper(cfg, tableInfo);
        pojo(cfg, tableInfo);
    }

    public static String sqlType2javaType(Integer type, String dType, String columnName) {
        System.out.println(columnName + "======" + dType + "=====" + type);
        if (Types.INTEGER == type || Types.TINYINT == type) {
            return Integer.class.getSimpleName();
        } else if (Types.VARCHAR == type || Types.LONGNVARCHAR == type) {
            return String.class.getSimpleName();
        } else if (Types.BIGINT == type) {
            return Long.class.getSimpleName();
        } else if (Types.TIME == type || Types.TIMESTAMP == type || Types.DATE == type) {
            return java.util.Date.class.getSimpleName();
        } else if (Types.DOUBLE == type || Types.DECIMAL == type) {
            return Double.class.getSimpleName();
        } else if (Types.BOOLEAN == type || Types.BIT == type) {
            return Boolean.class.getSimpleName();
        } else if (Types.FLOAT == type) {
            return Float.class.getSimpleName();
        }
        return dType;
    }


    private static List<String> tables(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<String>();
        DatabaseMetaData dbMetData = conn.getMetaData();
        ResultSet rs = dbMetData.getTables(null, null, null, new String[]{"TABLE"});
        while (rs.next()) {
            tables.add(rs.getString(3));
        }
        return tables;
    }
}
