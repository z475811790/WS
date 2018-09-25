package service;

import java.util.*;
import entity.*;

@SuppressWarnings("unused")
public interface UserService {

	public User selectById(int id);

	public int insert(User entity);

	public int update(User entity);

	public int deleteById(int id);

}