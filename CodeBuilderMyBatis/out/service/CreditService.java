package service;

import java.util.*;
import entity.*;

@SuppressWarnings("unused")
public interface CreditService {

	public Credit selectById(int id);

	public int insert(Credit entity);

	public int update(Credit entity);

	public int deleteById(int id);

}