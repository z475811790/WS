package ztest.service;

import org.springframework.stereotype.Service;

import ztest.aspect.Book;
import ztest.cache.CacheDelete;
import ztest.cache.CacheGet;
import ztest.cache.CacheSet;
import ztest.cache.CacheSetType;

@Service
public class BookServiceImpl implements BookService {

	@CacheGet(cache = "myCache", preKey = "entity")
	public Book getMerchantById(Integer id) {
		System.out.println("开始查询数据库");
		Book book = new Book();
		book.setId(id);
		book.setName("xYzDl-Book");
		return book;
	}

	@CacheGet(cache = "myCache", preKey = "entity")
	public Book getMerchantById(Book entity, Boolean isFull) {
		System.out.println("开始查询数据库");
		return entity;
	}

	@CacheSet(cache = "myCache", preKey = "entity", type = CacheSetType.REF_ID)
	public Book insert(Book entity) {
		System.out.println("开始插入数据库");
		entity.setId(entity.getId() + 1);
		return entity;
	}

	@CacheDelete(cache = "myCache", preKey = "entity")
	public Integer delete(Integer id) {
		return id;
	}
}
