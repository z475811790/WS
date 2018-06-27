package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import entity.Book;
import service.IBookService;
import service.impl.BookServiceImpl;

public class UnitTest {
	public static void main(String[] args) {
		new UnitTest().test1();
	}

	@Test
	public void test1() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IBookService bookService = (IBookService) context.getBean("bookService");
		Book book = new Book();
		book.setName("bookn");
		book.setPrice(1234);
		bookService.addBook(book);
	}
}
