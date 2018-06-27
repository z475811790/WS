package action;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import entity.Student;
import service.StudentService;

@Controller
public class StudentAction {

	@Autowired
	private StudentService studentService;

	@Test
	public void test1() {
		// 获取上下文对象
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		StudentAction studentAction = (StudentAction) context.getBean("studentAction");

		// Student student = new Student(1, "小明", "男", 20);
		// studentAction.studentService.save(student);
		Student std = studentAction.studentService.getStudent(1);
		System.out.println(std);
	}
}