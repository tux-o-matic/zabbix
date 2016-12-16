package cn.netkiller.zabbix;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.*;

/**
 * Hello world!
 *
 */
public class Oracle {

	private String url = null; // 数据库链接地址
	private String username = null;// 用户名,系统默认的账户名
	private String password = null;// 你安装时选设置的密码

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public Oracle() {
		try {
			this.logger.setLevel(Level.INFO);

			Formatter formatter = new Formatter() {

				@Override
				public String format(LogRecord logRecord) {
					StringBuilder b = new StringBuilder();
					b.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
					b.append(" ");
					b.append(logRecord.getSourceClassName());
					b.append(" ");
					b.append(logRecord.getSourceMethodName());
					b.append(" ");
					b.append(logRecord.getLevel());
					b.append(" ");
					b.append(logRecord.getMessage());
					b.append(System.getProperty("line.separator"));
					return b.toString();
				}

			};

			FileHandler fileHandler = new FileHandler("monitor.%g.log");

			logger.setUseParentHandlers(false);

			fileHandler.setLevel(Level.INFO);
			logger.addHandler(fileHandler);

			fileHandler.setFormatter(formatter);
			logger.setLevel(Level.INFO);

			LogManager lm = LogManager.getLogManager();
			lm.addLogger(logger);

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public void openConfig(String config) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(config));
			this.url = properties.getProperty("jdbc.url");
			this.username = properties.getProperty("jdbc.username");
			this.password = properties.getProperty("jdbc.password");
		} catch (FileNotFoundException e) {
			logger.info(e.getMessage() + " Working Directory = " + config);
			System.exit(1);
		} catch (IOException e) {
			logger.info(e.getMessage());
			System.exit(1);
		}
		if (this.url == null || this.username == null || this.password == null) {
			logger.info("This Propertie file is invalid");
			System.exit(1);
			// throw new Exception("");
		}

	}

	public void testConnection() {
		String sql = "select current_date from dual";
		this.query(sql);
	}

	public void query(String sql) {
		Connection connection = null;// 创建一个数据库连接
		ResultSet result = null;// 创建一个结果集对象
		Statement statement = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
			connection = DriverManager.getConnection(this.url, this.username, this.password);
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
			if (result.next()) {
				logger.info(String.format("%s %s", result.getDate(1), result.getTime(1)));
				System.out.println(1);
			} else {
				System.out.println(0);
			}

		} catch (ClassNotFoundException e) {
			logger.info(e.getMessage());
			System.exit(1);
		} catch (SQLException e) {
			logger.info(e.getMessage());
			System.exit(1);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				logger.info(e.getMessage());
				System.exit(1);
			}
		}
	}

	public void help(String prog) {
		System.out.println(String.format("%s -Dconfig=/path/to/jdbc.properties", prog));
	}

	public static void main(String[] args) {
		try {
			Oracle oracle = new Oracle();
			if (System.getProperty("config") == null) {
				oracle.help(oracle.getClass().getName());
				System.exit(1);
			}
			oracle.openConfig(System.getProperty("config"));
			if (args[0].equals("--conn")) {
				oracle.testConnection();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
