package org.example.middleware.entity;

import lombok.Data;

@Data
public class BookEntity {
    public String bookName;
    public Integer bookNo;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getBookNo() {
        return bookNo;
    }

    public void setBookNo(Integer bookNo) {
        this.bookNo = bookNo;
    }
}
