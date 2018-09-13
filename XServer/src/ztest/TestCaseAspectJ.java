package ztest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ztest.service.BookService;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:applicationContextTest.xml" })
public class TestCaseAspectJ {

	@Autowired
	public BookService service;

	@Test
	public void studentServiceTest() {
		service.getMerchantById("hello");
		System.out.println(1);
	}

}
