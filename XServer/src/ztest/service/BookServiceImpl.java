package ztest.service;

import org.springframework.stereotype.Service;

import ztest.aspect.Book;
import ztest.cache.KeyType;
import ztest.cache.MultiCache;

@Service
public class BookServiceImpl implements BookService {

	// 使用hset的方式，getMerchantById用作key，fieldKey当做map中的key
	@MultiCache(cache = "myCache", key = "entity", keyType = KeyType.STR_VAL)
	public Book getMerchantById(Integer id) {
		System.out.println("开始查询数据库");
		Book book = new Book();
		book.setId(id);
		book.setName("xYzDl-Book");
		return book;
	}

	@MultiCache(cache = "myCache", key = "entity", keyType = KeyType.REF_ID)
	public Book getMerchantById(Book entity, Boolean isFull) {
		System.out.println("开始查询数据库");
		return entity;
	}
}
