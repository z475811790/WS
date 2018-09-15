package ztest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.entity.Student;
import com.service.StudentService;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class TestCaseCacheAspectJ {

	@Autowired
	public StudentService service;

	@Test
	public void studentServiceTest() {
		Student s = service.selectById(1);
		s = service.selectById(1);
		s.setId(null);
		service.insert(s);
		service.selectById(30);
		service.deleteById(30);
		s = service.selectById(30);
		System.out.println(s);
	}

}
