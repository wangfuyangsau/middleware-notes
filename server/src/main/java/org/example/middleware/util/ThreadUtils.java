package org.example.middleware.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,10,60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(5));
}
