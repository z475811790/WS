package ztest;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.action.ChatAction;
import com.componet.SocketOuter;
import com.entity.Student;
import com.entity.User;
import com.service.StudentService;
import com.service.UserService;
import com.service.impl.UserServiceImpl;
import com.util.CommonUtil;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class TestCaseSpringContext {

	@Autowired
	public StudentService studentService;
	@Autowired
	public ChatAction chatAction;
	@Autowired
	public SocketOuter socketOuter;

	@Autowired

	@Test
	public void studentServiceTest() {
		studentService.selectById(1);
		studentService.selectById(1);

		System.out.println(socketOuter);
	}

	@Test
	public void temp2() {
		List<Student> list = studentService.selectByAgeRange(19);
		for (Student s : list) {
			CommonUtil.outObject(s);
		}
		List<Student> list2 = studentService.selectByAgeRange(19);
		for (Student s : list2) {
			CommonUtil.outObject(s);
		}
		for (Student s : list2) {
			CommonUtil.outObject(s);
		}
	}

	@Autowired
	private UserServiceImpl userService;

	@Test
	public void temp3() {
		User user = userService.selectById(1);
		System.out.println(user.getAccount());
		user.setUserId(2);
		userService.insert(user);
		userService.insert(user);
		userService.flushCache();
		System.out.println(user.getUserId());

	}
}
