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
public class TestCaseDao {
	@Autowired
	public StudentService studentService;

	@Test
	public void TestCaseInsert() {
//		studentService.testMethod();
		
		Student s = studentService.selectById(1);
//		studentService.insert(s);
//		s.setId(null);
//		studentService.insert(s);
		Student s1 = studentService.selectById(1);
		System.out.println(s1.getId());
	}
}