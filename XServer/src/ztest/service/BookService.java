package ztest.service;

import ztest.aspect.Book;

public interface BookService {
	public Book getMerchantById(Integer id);

	public Book getMerchantById(Book entity, Boolean isFull);

	public Book insert(Book entity);

	public Integer delete(Integer id);
}
