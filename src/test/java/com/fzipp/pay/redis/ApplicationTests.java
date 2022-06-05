package com.fzipp.pay.redis;

import com.fzipp.pay.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis连接测试
     * @throws Exception
     */
    @Test
    public void test_001() throws Exception{
        //保存字符
        stringRedisTemplate.opsForValue().set("haha","123");
        Assert.assertEquals("123", stringRedisTemplate.opsForValue().get("haha"));
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 存储对象
     */
    @Test
    public void test_002() throws Exception{
        User user = new User();
        user.setRealname("我没有三颗心脏");
        user.setBirthday(new Date());

        redisTemplate.opsForValue().set("user_1", user);
        User user1 = (User) redisTemplate.opsForValue().get("user_1");

        System.out.println(user1.getRealname());
    }

    /**
     * 在Redis中操作List
     */
    // list数据类型适合于消息队列的场景:比如12306并发量太高，而同一时间段内只能处理指定数量的数据！必须满足先进先出的原则，其余数据处于等待
    @Test
    public void listPushResitTest() {
        // rightPush依次由右边添加
        stringRedisTemplate.opsForList().rightPush("myList", "1");
        stringRedisTemplate.opsForList().rightPush("myList", "2");
        stringRedisTemplate.opsForList().rightPush("myList", "A");
        stringRedisTemplate.opsForList().rightPush("myList", "B");
        // leftPush依次由左边添加
        stringRedisTemplate.opsForList().leftPush("myList", "0");
    }

    @Test
    public void listGetListResitTest() {
        // 查询类别所有元素
        List<String> listAll = stringRedisTemplate.opsForList().range("myList", 0, -1);
        logger.info("list all {}", listAll);
        // 查询前3个元素
        List<String> list = stringRedisTemplate.opsForList().range("myList", 0, 2);
        logger.info("list limit {}", list);
    }

    @Test
    public void listRemoveOneResitTest() {
        // 删除先进入的B元素
        stringRedisTemplate.opsForList().remove("myList", 1, "B");
    }

    @Test
    public void listRemoveAllResitTest() {
        // 删除所有A元素
        stringRedisTemplate.opsForList().remove("myList", 0, "A");
    }

    /**
     * 在Redis中操作Hash
     */
    @Test
    public void hashPutResitTest() {
        // map的key值相同，后添加的覆盖原有的
        stringRedisTemplate.opsForHash().put("banks:12600000", "a", "b");
    }

    @Test
    public void hashGetEntiresResitTest() {
        // 获取map对象
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("banks:12600000");
        logger.info("objects:{}", map);
    }

    @Test
    public void hashGeDeleteResitTest() {
        // 根据map的key删除这个元素
        stringRedisTemplate.opsForHash().delete("banks:12600000", "c");
    }

    @Test
    public void hashGetKeysResitTest() {
        // 获得map的key集合
        Set<Object> objects = stringRedisTemplate.opsForHash().keys("banks:12600000");
        logger.info("objects:{}", objects);
    }

    @Test
    public void hashGetValueListResitTest() {
        // 获得map的value列表
        List<Object> objects = stringRedisTemplate.opsForHash().values("banks:12600000");
        logger.info("objects:{}", objects);
    }

    @Test
    public void hashSize() { // 获取map对象大小
        long size = stringRedisTemplate.opsForHash().size("banks:12600000");
        logger.info("size:{}", size);
    }
}
