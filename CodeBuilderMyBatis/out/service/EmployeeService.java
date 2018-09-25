package service;

import java.util.*;
import entity.*;

@SuppressWarnings("unused")
public interface EmployeeService {

	public Employee selectById(int id);

	public int insert(Employee entity);

	public int update(Employee entity);

	public int deleteById(int id);

}