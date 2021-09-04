package org.example.middleware.controller;


import org.example.middleware.entity.BookEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping(value = "/info",method= RequestMethod.GET)
    public BookEntity info(Integer bookNo, String bookName){
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookNo(bookNo);
        bookEntity.setBookName(bookName);
        logger.info("我被访问了");
        return bookEntity;

    }

    @RequestMapping(value = "/test",method= RequestMethod.GET)
    public BookEntity test(){

        logger.info("我被访问了");
        return null;

    }

}
