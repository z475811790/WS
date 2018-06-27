package com.xyzdl.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xyzdl.bean.CatBean;
import com.xyzdl.bean.DogBean;
import com.xyzdl.bean.PetBean;
import com.xyzdl.mapper.PetMapper;
import com.xyzdl.util.DBUtil;

public class ExtendsService {
	public static void main(String[] args) {
		saveCat();
		saveDog();
		findAllCat();
		findAllPet();
	}

	private static void findAllCat() {
		// TODO Auto-generated method stub
		SqlSession session = DBUtil.getSession();
		PetMapper pm = session.getMapper(PetMapper.class);
		List<CatBean> cats = pm.findAllCat();
		for (CatBean catBean : cats) {
			System.out.println(catBean);
		}
	}

	private static void findAllPet() {
		// TODO Auto-generated method stub
		SqlSession session = DBUtil.getSession();
		PetMapper pm = session.getMapper(PetMapper.class);
		List<PetBean> pets = pm.findAllPet();
		for (PetBean petBean : pets) {
			System.out.println(petBean);
		}
	}

	private static void saveDog() {
		// TODO Auto-generated method stub
		SqlSession session = DBUtil.getSession();
		PetMapper pm = session.getMapper(PetMapper.class);
		DogBean dog = new DogBean(null, "哈士奇");
		dog.setBone(10);
		pm.saveDog(dog);
		session.commit();
	}

	private static void saveCat() {
		// TODO Auto-generated method stub
		SqlSession session = DBUtil.getSession();
		PetMapper pm = session.getMapper(PetMapper.class);
		CatBean cat = new CatBean(null, "大脸猫");
		cat.setFish(10);
		pm.saveCat(cat);
		session.commit();
	}
}