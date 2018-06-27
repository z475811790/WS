package com.xyzdl.service;

import org.apache.ibatis.session.SqlSession;

import com.xyzdl.bean.HusbandBean;
import com.xyzdl.bean.WifeBean;
import com.xyzdl.mapper.HusbandMapper;
import com.xyzdl.mapper.WifeMapper;
import com.xyzdl.util.DBUtil;

public class OneToOneService {

	public static void main(String[] args) {
		selectHusbandAndWife();
	}

	private static void selectHusbandAndWife() {
		SqlSession session = DBUtil.getSession();
		HusbandMapper hm = session.getMapper(HusbandMapper.class);
		WifeMapper wm = session.getMapper(WifeMapper.class);

		try {
			HusbandBean husband = hm.selectHusbandAndWife(1);
			System.out.println(husband);
			WifeBean wifeBean = wm.selectWifeById(1);
			System.out.println(wifeBean);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}