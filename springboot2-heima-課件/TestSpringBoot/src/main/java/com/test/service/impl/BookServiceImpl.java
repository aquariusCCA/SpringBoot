package com.test.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mapper.BookMapper;
import com.test.pojo.Book;
import com.test.service.IBookService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {
    @Autowired
    private BookMapper bookMapper;

    private Counter counter;

    public BookServiceImpl(@Autowired MeterRegistry meterRegistry){
        counter = meterRegistry.counter("用户付费操作次数：");
    }

    @Override
    public boolean delete(Integer id) {
        //每次执行删除业务等同于执行了付费业务
        counter.increment();
        return bookMapper.deleteById(id) > 0;
    }
}