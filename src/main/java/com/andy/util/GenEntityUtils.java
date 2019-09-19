package com.andy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 *
 * @author Dandy
 * @根據表名 和數據庫連接生成實體類 默認是portrait這個庫
 * 如果是xdao的話主鍵註解還是要自己手動加
 */

public class GenEntityUtils {

	private static final Logger logger = LoggerFactory.getLogger(GenEntityUtils.class);

	private String packageOutPath = "com.andy.model";// 指定实体生成所在包的路径
	private String authorName = "Andy";// 作者名字
	private String tablename = "";// 表名
	private String[] colnames; // 列名数组
	private String[] colTypes; // 列名类型数组
	private int[] colSizes; // 列名大小数组
	private boolean importUtil = false; // 是否需要导入包java.util.*
	private boolean importSql = false; // 是否需要导入包java.sql.*
	private boolean importColumn = true; //導入column註解

	// 数据库连接
	private String URL = "jdbc:mysql://49.234.41.101:33307/woniutrip";
	private String NAME = "root";
	private String PASS = "1234Andy";
	private String DRIVER = "com.mysql.jdbc.Driver";

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public GenEntityUtils(String tableName) {
		this.tablename = tableName;
	}

	public GenEntityUtils(String tablename,String packageName) {
		this.tablename = tablename;
		this.packageOutPath = packageName;
	}

	public GenEntityUtils(String tableName,String url,String name,String password,String driverName) {
		this.tablename = tableName;
		this.URL=url;
		this.NAME = name;
		this.PASS=password;
		this.DRIVER=driverName;
	}

	public void genEntity(){
		Connection con =null;
		ResultSetMetaData rsmd;
		ResultSet rs=null;
		// 查要生成实体类的表
		String sql = "select * from " + tablename;
		Statement pStemt = null;

		try {
			try {
				Class.forName(DRIVER);
			} catch (ClassNotFoundException e1) {
				logger.warn("[genEntity]反射获取数据库连接驱动出错！", e1);
			}

			con = DriverManager.getConnection(URL, NAME, PASS);
			pStemt = (Statement) con.createStatement();
			rs = pStemt.executeQuery(sql);
			rsmd = rs.getMetaData();
			int size = rsmd.getColumnCount(); // 统计列
			colnames = new String[size];
			colTypes = new String[size];
			colSizes = new int[size];
			for (int i = 0; i < size; i++) {
				colnames[i] = rsmd.getColumnName(i + 1);
				colTypes[i] = rsmd.getColumnTypeName(i + 1);

				if (colTypes[i].equalsIgnoreCase("date") || colTypes[i].equalsIgnoreCase("timestamp")) {
					importUtil = true;
				}
				if (colTypes[i].equalsIgnoreCase("blob") || colTypes[i].equalsIgnoreCase("char")) {
					importSql = true;
				}
				colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
			}

			String content = parse(colnames, colTypes, colSizes);

			FileWriter fw = null;
			try {
				File directory = new File("");
				String path = this.getClass().getResource("").getPath();

				logger.info("[genEntity]path:{}", path);
				logger.info("[genEntity]{}", "src/?/" + path.substring(path.lastIndexOf("/com/", path.length())));

				String outputPath = directory.getAbsolutePath() + "/src/main/java/"
						+ this.packageOutPath.replace(".", "/") + "/"
						+ initcap(StringUtilsApi.underlineToCamel(tablename)) + ".java";
				fw = new FileWriter(outputPath);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(content);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				logger.warn("[genEntity]出现IOException！", e);
			} finally {
				if (fw != null) {
					fw.close();
				}
			}
		} catch (Exception e) {
			logger.warn("[genEntity]关闭流出现Exception！", e);
		} finally {
			// 这里比较乱，建议用try-with-resource优化，思考怎么同时处理多个源
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e1) {
				logger.warn("[genEntity]关闭rs出现SQLException！", e1);
			}
			try {
				if (pStemt != null) {
					pStemt.close();
				}
			} catch (SQLException e2) {
				logger.warn("[genEntity]关闭pStemt出现SQLException！", e2);
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e3) {
				logger.warn("[genEntity]关闭con出现SQLException！", e3);
			}
		}
	}

	private String parse(String[] colnames, String[] colTypes, int[] colSizes) {
		StringBuffer sb = new StringBuffer();

		sb.append("package ").append(this.packageOutPath).append(";\r\n");
		sb.append("\r\n");
		// 判断是否导入工具包
		if (importUtil) {
			sb.append("import java.util.Date;\r\n");
		}
		if (importSql) {
			sb.append("import java.sql.*;\r\n");
		}
		if(importColumn){
			sb.append("import com.mama.xsx.data.dao.annotation.Column;\r\n");
			sb.append("import com.mama.xsx.data.dao.annotation.Entity;\r\n");
		}
		// 注释部分
		sb.append("   /**\r\n");
		sb.append("    * ").append(StringUtilsApi.underlineToCamel(tablename)).append(" 实体类\r\n");
		sb.append("    *  ").append(this.authorName).append("\r\n");
		sb.append("    */ \r\n");
		// 实体部分

		sb.append("@Entity(tableName = \"").append(tablename).append("\")");
		sb.append("\r\n\r\npublic class ").append(StringUtilsApi.underlineToCamel(initcap(tablename))).append("{\r\n");
		processAllAttrsWithAnnoation(sb);// 属性
		processAllMethod(sb);// get set方法
		sb.append("}\r\n");

		return sb.toString();
	}

	/**
	 * 功能：生成所有属性
	 *
	 * @param sb
	 */
	private void processAllAttrs(StringBuffer sb) {

		for (int i = 0; i < colnames.length; i++) {
			sb.append("\tprivate ").append(sqlType2JavaType(colTypes[i]))
					.append(" ").append(StringUtilsApi.underlineToCamel(colnames[i])).append(";\r\n");
		}

	}

	/**
	 * 功能：生成所有属性並加上註解
	 *
	 * @param sb
	 */
	private void processAllAttrsWithAnnoation(StringBuffer sb) {

		for (int i = 0; i < colnames.length; i++) {
			sb.append("\t@Column(value = \"").append(colnames[i]).append("\")");
			sb.append("\r\n");
			sb.append("\tprivate ").append(sqlType2JavaType(colTypes[i]))
					.append(" ").append(StringUtilsApi.underlineToCamel(colnames[i])).append(";\r\n");
		}

	}

	/**
	 * 功能：生成所有方法
	 *
	 * @param sb
	 */
	private void processAllMethod(StringBuffer sb) {
		sb.append("/r/n");
		for (int i = 0; i < colnames.length; i++) {
			String fieldName = StringUtilsApi.underlineToCamel(colnames[i]);
			sb.append("\tpublic void set").append(initcap(fieldName))
					.append("(").append(sqlType2JavaType(colTypes[i])).append(" ").append(fieldName).append("){\r\n");
			sb.append("\tthis.").append(fieldName).append("=").append(fieldName).append(";\r\n");
			sb.append("\t}\r\n");
			sb.append("\tpublic ").append(sqlType2JavaType(colTypes[i])).append(" get").append(initcap(fieldName)).append("(){\r\n");
			sb.append("\t\treturn ").append(fieldName).append(";\r\n");
			sb.append("\t}\r\n");
		}

		sb.append("/r/n");
	}

	/**
	 * 功能：将输入字符串的首字母改成大写
	 *
	 * @param str
	 * @return
	 */
	private String initcap(String str) {

		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}

		return new String(ch);
	}

	/**
	 * . * 功能：获得列的数据类型
	 *
	 * @param sqlType  目前只是支持mysql 如果其他库要改一下类型对应关系
	 * @return
	 */
	private String sqlType2JavaType(String sqlType) {

		if (sqlType.equalsIgnoreCase("decimal")) {
			return "Double";
		} else if (sqlType.equalsIgnoreCase("binary_float")) {
			return "float";
		} else if (sqlType.equalsIgnoreCase("blob")) {
			return "byte[]";
		} else if (sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar2")
				|| sqlType.equalsIgnoreCase("varchar")|| sqlType.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("timestamp")
				|| sqlType.equalsIgnoreCase("timestamp with local time zone")
				|| sqlType.equalsIgnoreCase("timestamp with time zone")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("int")) {
			return "Long";
		}
		return "Integer";
	}

	public static void main(String[] args) {
		GenEntityUtils gen = new GenEntityUtils("tour_admin");
		gen.genEntity();
	}

}
