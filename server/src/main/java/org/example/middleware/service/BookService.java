package org.example.middleware.service;

import netscape.security.UserTarget;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.example.middleware.entity.BookEntity;
import org.example.middleware.mapper.ItemMapper;
import org.example.middleware.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BookService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private CuratorFramework client;

    private static final String zklock="/zklock";


    public void insertBook(Item entity) throws Exception{
        //加入分布式锁控制，防止同样标号的书重复插入，逻辑同红包一样

        //创建zk lock节点的,需要和想要控制的资源联系上，所以要精心设计
        InterProcessMutex mutex = new InterProcessMutex(client,zklock+entity.getCode()+"-lock");
        //重试时间10s
        try {
            if(mutex.acquire(10L, TimeUnit.SECONDS)){
                //核心业务，会受到并发影响的逻辑
                Item item = itemMapper.selectByCode(entity.getCode());
                if(item==null){
                    itemMapper.insert(entity);
                }else{
                    System.out.println("书已经存在");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取分布式锁失败");
        }finally {
            mutex.release();
        }


    }

}
