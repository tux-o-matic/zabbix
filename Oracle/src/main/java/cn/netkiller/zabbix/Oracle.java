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

class LogFormatter extends Formatter {
	@Override
	public String format(LogRecord record) {
		return String.format("%s %s\t%s\r\n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()),
				record.getLevel(), record.getMessage());
	}
}

public class Oracle {

	String url = null; // 数据库链接地址
	String username = null;// 用户名,系统默认的账户名
	String password = null;// 你安装时选设置的密码

	Logger log = Logger.getLogger(this.getClass().getName());

	public Oracle() {

		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler("monitor.%g.log");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.OFF);
		log.addHandler(consoleHandler);

		fileHandler.setLevel(Level.INFO);
		this.log.addHandler(fileHandler);

		fileHandler.setFormatter(new LogFormatter());
		this.log.setLevel(Level.INFO);

	}

	public void openConfig() {
		String connectionfig = "jdbc.properties";
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(connectionfig));
			this.url = properties.getProperty("jdbc.url");
			this.username = properties.getProperty("jdbc.username");
			this.password = properties.getProperty("jdbc.password");
		} catch (FileNotFoundException e) {
			this.log.info(
					e.getMessage() + " Working Directory = " + System.getProperty("user.dir") + "/" + connectionfig);
			System.exit(1);
		} catch (IOException e) {
			this.log.info(e.getMessage());
			System.exit(1);
		}
		if (this.url == null || this.username == null || this.password == null) {
			this.log.info("This Propertie file is invalid");
			System.exit(1);
			// throw new Exception("");
		}

	}

	public void testConnection() {
		Connection connection = null;// 创建一个数据库连接
		ResultSet result = null;// 创建一个结果集对象
		Statement statement = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
			connection = DriverManager.getConnection(this.url, this.username, this.password);
			String sql = "select current_date from dual";
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
			if (result.next()) {
				this.log.info(String.format("%s %s", result.getDate(1), result.getTime(1)));
				System.out.println(1);
			} else {
				System.out.println(0);
			}

		} catch (ClassNotFoundException e) {
			log.info(e.getMessage());
			System.exit(1);
		} catch (SQLException e) {
			log.info(e.getMessage());
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
				this.log.info(e.getMessage());
				System.exit(1);
			}
		}
	}

	public static void main(String[] args) {
		try {
			Oracle oracle = new Oracle();
			oracle.openConfig();
			if(args[1] == "--conn"){
				oracle.testConnection();
			}
		} catch (Exception e) {
			System.out.println(0);
		}

	}
}
