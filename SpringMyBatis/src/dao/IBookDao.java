package dao;

import org.apache.ibatis.annotations.Param;

import entity.Book;

public interface IBookDao {
	public void addBook(@Param("book") Book book);
}
