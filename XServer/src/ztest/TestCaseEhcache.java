package ztest;

import java.util.Date;

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
public class TestCaseEhcache {
	@Autowired
	public StudentService studentService;

	@Test
	public void TestCase() {
		Student s = studentService.selectById(1);
		Student s1 = studentService.selectById(1);
//		s.setId(6);
//		studentService.insert(s);
		System.out.println(s.getId());
	}
}
