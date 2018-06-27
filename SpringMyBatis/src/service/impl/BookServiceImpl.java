package service.impl;

import dao.IBookDao;
import entity.Book;
import service.IBookService;

public class BookServiceImpl implements IBookService {
	private IBookDao dao;

	public IBookDao getDao() {
		return dao;
	}

	public void setDao(IBookDao dao) {
		this.dao = dao;
	}

	public void addBook(Book book) {
		dao.addBook(book);
	}
}
