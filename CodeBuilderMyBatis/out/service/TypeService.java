package service;

import java.util.*;
import entity.*;

@SuppressWarnings("unused")
public interface TypeService {

	public Type selectById(int id);

	public int insert(Type entity);

	public int update(Type entity);

	public int deleteById(int id);

}