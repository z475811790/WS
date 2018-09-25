package service;

import java.util.*;
import entity.*;

@SuppressWarnings("unused")
public interface BusiService {

	public Busi selectById(int id);

	public int insert(Busi entity);

	public int update(Busi entity);

	public int deleteById(int id);

}