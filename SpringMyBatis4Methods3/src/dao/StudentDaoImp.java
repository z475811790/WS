package dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import entity.Student;

@Service
public class StudentDaoImp extends SqlSessionDaoSupport implements IStudentDao {

	/**
	 * 我们发现这个类中没有把SqlSessionTemplate作为一个属性，因为我们继承了SqlSessionDaoSupport
	 *SqlSessionDaoSupport他会提供sqlsession
	*/

	@Override
	public void save(Student student) {
		// TODO Auto-generated method stub
		this.getSqlSession().insert("dao.IStudentDao.save", student);
	}

	@Override
	public Student getStudent(int id) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectOne("dao.IStudentDao.getStudent", id);
	}

	/**
	 * 使用SqlSessionDaoSupport必须注意，此处源码1.1.1中有自动注入，1.2中取消了自动注入，需要手工注入，侵入性强
	 * 也可在spring-mybatis.xml中如下配置，但是这种有多少个dao就要配置到多少个，多个dao就很麻烦。
	 * <bean id="userDao"class="dao.UserDao">
	 * <propertyname="sqlSessionFactory" ref="sqlSessionFactory"/>
	 * </bean>
	 */
	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

}
