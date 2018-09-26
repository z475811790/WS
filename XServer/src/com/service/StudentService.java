/**
 * Created by xYzDl Builder
 */
package com.service;

import java.util.*;
import com.entity.*;

public interface StudentService {

	public String getDAO();

	public Student selectById(int id);

	public int insert(Student entity);

	public int update(Student entity);

	public int deleteById(int id);

	public List<Student> selectByAgeRange(int age);

}