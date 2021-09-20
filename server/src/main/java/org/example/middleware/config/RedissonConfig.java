package org.example.middleware.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RedissonConfig {
    @Autowired
    private Environment env;

    @Bean
    public RedissonClient config(){
        Config config = new Config();
        //设置传输方式
        config.setTransportMode(TransportMode.NIO);
        //设置结点部署模式，他叫基于redis的驻内存网格中间件
        // config.useClusterServers().addNodeAddress(env.getProperty("redisson.host.config"),env.getProperty("redisson.host.config"));
        config.useSingleServer().setAddress(env.getProperty("redisson.host.config")).setKeepAlive(true);
        return Redisson.create(config);

    }
}
