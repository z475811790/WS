package ztest.service;

import org.springframework.stereotype.Service;

import ztest.cache.MultiCache;

@Service
public class BookServiceImpl implements BookService {

	// 使用hset的方式，getMerchantById用作key，fieldKey当做map中的key
	@MultiCache(key = "getMerchantById", fieldKey = "#name")
	public Object getMerchantById(String name) {
		System.out.println("开始查询数据库");
		return name + "xyzdl";
	}
}
