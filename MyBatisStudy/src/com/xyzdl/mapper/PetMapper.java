package com.xyzdl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xyzdl.bean.CatBean;
import com.xyzdl.bean.DogBean;
import com.xyzdl.bean.PetBean;

public interface PetMapper {
	/**
	 * 添加宠物猫
	 * @param cat
	 * @return
	 */
	public int saveCat(@Param("c") CatBean cat);

	/**
	 * 添加宠物狗
	 * @param dog
	 * @return
	 */
	public int saveDog(@Param("d") DogBean dog);

	/**
	 * 查询所有的宠物
	 * @return
	 */
	public List<PetBean> findAllPet();

	/**
	 * 查询所有的宠物猫
	 * @return
	 */
	public List<CatBean> findAllCat();

}