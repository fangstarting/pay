package com.fzipp.pay.redis;

import com.fzipp.pay.common.redis.cache.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CacheServiceTests {
    @Autowired
    private CacheService cacheService;
    /**
     * 功能描述：添加字符串到redis
     */
    @Test
    public void add() {
        cacheService.add("test", 1234);
    }

}
