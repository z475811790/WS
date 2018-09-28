/**
 * Created by xYzDl Builder
 */
package com.service;

import java.util.*;
import com.entity.*;

public interface UserService {

	public String getDAO();

	public User selectById(Integer id);

	public User insert(User entity);

	public User update(User entity);

	public int deleteById(Integer id);

}