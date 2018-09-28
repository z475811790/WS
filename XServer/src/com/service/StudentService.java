/**
 * Created by xYzDl Builder
 */
package com.service;

import java.util.*;
import com.entity.*;

public interface StudentService {

	public String getDAO();

	public Student selectById(Integer id);

	public Student insert(Student entity);

	public Student update(Student entity);

	public int deleteById(Integer id);

}