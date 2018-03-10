import java.util.Scanner;
import java.util.*;
import java.util.Map.*;

public class prob1 {
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
	HashMap<String, Book> bookMap;
	public void loadSys() {
		bookMap = new HashMap<String, Book>();
		printInfo();
		Scanner scn = new Scanner(System.in);
		String cmd = "";
		while(true) {
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
		}
	}

	private void showBook() {
		Set<Entry<String, Book>> tmpSet = bookMap.entrySet();
		for(Entry<String, Book> entry : tmpSet) {
			Book tmpBook = entry.getValue();
			tmpBook.printBookInfo();
		}
	}

	private void lendBook() {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要借阅的书籍名：");
		String mess = scn.nextLine();
		Book tar = bookMap.get(mess);
		if(tar == null) {
			System.out.print("没有这本书！\n");
			return;
		}
		if(tar.lend)
			System.out.print("已被借阅走！\n");
		else {
			tar.lend = true;
			System.out.print("成功借阅！\n");
		}
		return;
	}

	private void returnBook() {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要归还的书籍名：");
		String mess = scn.nextLine();
		Book tar = bookMap.get(mess);
		if(tar == null) {
			System.out.print("没有这本书！\n");
			return;
		}
		if(tar.lend) {
			tar.lend = false;
			System.out.print("成功归还！\n");
		} else {
			System.out.print("并未借出！\n");
		}
		return;
	}

	private void addBook() {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要添加的书籍名：");
		String name = scn.nextLine();
		System.out.print("请输入想要添加的书编号：");
		String num = scn.nextLine();
		System.out.print("请输入想要添加的书价格：");
		Float price = Float.parseFloat(scn.nextLine());
		Book newBook = new Book(num, name, price);
		bookMap.put(name, newBook);
		System.out.print("成功添加！\n");
		return;
	}

	private void removeBook() {
		Scanner scn = new Scanner(System.in);
		System.out.print("请输入想要删除的书籍名：");
		String name = scn.nextLine();
		Book tar = bookMap.get(name);
		if(tar == null) {
			System.out.print("没有这本书！\n");
			return;
		}
		bookMap.remove(name);
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
