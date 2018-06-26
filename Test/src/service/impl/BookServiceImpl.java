package service.impl;

import dao.IBookDao;
import service.IBookService;

public class BookServiceImpl implements IBookService {
	private IBookDao dao;
	public IBookDao getDao() {
		return dao;
	}
	public void setDao(IBookDao dao) {
		this.dao = dao;
	}
	@Override
	public void addBook() {

	}
}
