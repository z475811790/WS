/**
 * Created by xYzDl Builder
 */
package com.service;

import java.util.*;
import com.entity.*;

@SuppressWarnings("unused")
public interface StudentService {

	public Student selectById(int id);

	public Student insert(Student entity);

	public Student update(Student entity);

	public int deleteById(int id);

	public void testMethod();

	public List<Student> selectByAgeRange(int age);
}