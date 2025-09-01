package com.test.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.Book;

public interface IBookService extends IService<Book> {
    boolean delete(Integer id);
}
