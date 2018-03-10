import java.util.Scanner;
import java.util.*;
import java.util.Map.*;
import java.sql.*;
import java.math.*;

public class prob2 {
	public static void main(String[] args) {

		BookSystem bs = new BookSystem();
		bs.loadSys();
	}
}

class Book {
	String num, name;
	Float price;
	public Boolean lend;
	Book(String num, String name, Float price) {
		this.num = num;
		this.name = name;
		this.price = price;
		this.lend = false;
	}
	public void printBookInfo() {
		System.out.print("编号：" + num + " 名称：" + name + " 价格：" + price.toString() + 
					" 借出：");
		if(lend)
			System.out.print("是\n");
		else System.out.print("否\n");
	}

}

class BookSystem {
	//HashMap<String, Book> bookMap;
	Connection conn;
	Statement stmt;
	PreparedStatement pstmt;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/bookSys?useUnicode=true&amp;characterEncoding=utf-8";

	//要在自己的电脑上跑起来，请安装好mysql并替换下面的用户和密码
	//题目所需要的数据库
	static final String USER = "root";
	static final String PWD = "xzw13607011526";
	BookSystem() {
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to mysql...");
			conn = DriverManager.getConnection(DB_URL, USER, PWD);
			stmt = conn.createStatement();
			// String str_createDb = "CREATE DATABASE IF NOT EXISTS bookSys";
			// stmt.executeUpdate(str_createDb);
			String str_chan = "use bookSys";
			stmt.executeUpdate(str_chan);


			// str_style = "set character_set_client=utf8";
			// stmt.executeUpdate(str_style);
			// str_style = "set character_set_connection=utf8";
			// stmt.executeUpdate(str_style);

			// String str_style = "SET names utf8";
			// stmt.executeUpdate(str_style);
			// str_style = "set character_set_server=utf8";
			// stmt.executeUpdate(str_style);
			// str_style = "set character_set_database=utf8";
			// stmt.executeUpdate(str_style);

			// str_style = "SET collation_server=utf8_general_ci";
			// stmt.executeUpdate(str_style);
			// str_style = "SET collation_database=utf8_general_ci";
			// stmt.executeUpdate(str_style);

			String str_createTable = "CREATE TABLE IF NOT EXISTS booklist" + 
									"(num VARCHAR(255)," +
									"name VARCHAR(255)," +
									"price FLOAT not NULL," +
									"lend BIT not NULL)";
			stmt.executeUpdate(str_createTable);
 		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void loadSys() {
		//bookMap = new HashMap<String, Book>();
		printInfo();
		Scanner scn = new Scanner(System.in);
		String cmd = "";
		while(true) {
			try{
				printCmd();
				cmd = scn.nextLine();
				//System.out.println(cmd);
				if(cmd.equals("1")) {
					showBook();
				} else if(cmd.equals("2")) {
					lendBook();
				} else if(cmd.equals("3")) {
					returnBook();
				} else if(cmd.equals("4")) {
					addBook();
				} else if(cmd.equals("5")) {
					removeBook();
				} else if(cmd.equals("6")) {
					refresh();
				} else if(cmd.equals("7")) {
					quit();
					break;
				} else {
					printErrorInfo();
				}
			} catch(Exception e) {
				//System.out.println("Error!");
				e.printStackTrace();
			}
		}
	}

	private void showBook() throws Exception {
		String sql = "SELECT num, name, price, lend FROM booklist";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			String num = rs.getString("num");
			String name = rs.getString("name");
			Float price = rs.getFloat("price");
			Boolean lend = rs.getBoolean("lend");
			System.out.print("编号：" + num + " 名称：" + name + " 价格：" + price.toString() + 
					" 借出：");
			if(lend)
				System.out.print("是\n");
			else System.out.print("否\n");
		}
	}

	private void lendBook() throws Exception {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要借阅的书籍名：");
		String mess = scn.nextLine();
		//Book tar = bookMap.get(mess);
		String sql = "SELECT lend FROM booklist WHERE name=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, mess);
		ResultSet rs = pstmt.executeQuery();
		if(!rs.next()) {
			System.out.print("没有这本书！\n");
			return;
		}
		Boolean lend = rs.getBoolean("lend");
		if(lend)
			System.out.print("已被借阅走！\n");
		else {
			sql = "UPDATE booklist SET lend=1 WHERE name=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mess);
			pstmt.executeUpdate();
			System.out.print("成功借阅！\n");
		}
		return;
	}

	private void returnBook() throws Exception {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要归还的书籍名：");
		String mess = scn.nextLine();
		//Book tar = bookMap.get(mess);
		String sql = "SELECT lend FROM booklist WHERE name=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, mess);
		ResultSet rs = pstmt.executeQuery();
		if(!rs.next()) {
			System.out.print("没有这本书！\n");
			return;
		}
		Boolean lend = rs.getBoolean("lend");
		if(lend) {
			//tar.lend = false;
			sql = "UPDATE booklist SET lend=0 WHERE name=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mess);
			pstmt.executeUpdate();
			System.out.print("成功归还！\n");
		} else {
			System.out.print("并未借出！\n");
		}
		return;
	}

	private void addBook() throws Exception {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要添加的书籍名：");
		String name = scn.nextLine();
		System.out.print("请输入想要添加的书编号：");
		String num = scn.nextLine();
		System.out.print("请输入想要添加的书价格：");
		Float price = Float.parseFloat(scn.nextLine());
		//Book newBook = new Book(num, name, price);
		//bookMap.put(name, newBook);
		String sql = "INSERT INTO booklist VALUES(?, ?, ?, ?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, num);
		pstmt.setString(2, name);
		pstmt.setFloat(3, price);
		pstmt.setBoolean(4, false);
		pstmt.executeUpdate();
		System.out.print("成功添加！\n");
		return;
	}

	private void removeBook() throws Exception {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要删除的书籍名：");
		String name = scn.nextLine();
		//Book tar = bookMap.get(name);
		String sql = "SELECT lend FROM booklist WHERE name=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		ResultSet rs = pstmt.executeQuery();
		if(!rs.next()) {
			System.out.print("没有这本书！\n");
			return;
		}
		sql = "DELETE FROM booklist WHERE name=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		pstmt.executeUpdate();
		//bookMap.remove(name);
		System.out.print("成功删除！\n");
	}

	private void refresh() {
		printInfo();
		return;
	}

	private void quit() {
		System.out.print("成功退出！\n");
	}

	private void printErrorInfo() {
		System.out.print("无效的指令！\n");
	}

	private void printInfo() {
		System.out.println("**********欢迎光临BookShop**********\n1、显示图书\n2、借出图书\n3、归还图书\n4、添加图书\n5、删除图书\n6、刷新界面\n7、退出系统");
	}
	private void printCmd() {
		System.out.print("**********欢迎光临BookShop**********\n请选择功能[1-7]：");
	}
}
