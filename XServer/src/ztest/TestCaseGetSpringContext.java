package ztest;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestCaseGetSpringContext {

	@Test
	public void testcase() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
}
