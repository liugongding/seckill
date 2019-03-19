package com.dingding.seckill.redis;

import com.dingding.seckill.dao.SeckillDAO;
import com.dingding.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SeckillDAO seckillDAO;
    private final String KEY = "seckill";
    @Test
    public void getSeckillList() {
        //1、首页全部加载进缓存
        List<Seckill> seckillList = redisTemplate.boundHashOps(KEY).values();
        if (seckillList == null || seckillList.size() == 0) {
            //2、如果缓存中没有数据、从数据库中取出来添加到缓存
            seckillList = seckillDAO.queryAll(0,4);
            seckillList.forEach(seckills -> {
                redisTemplate.boundHashOps(KEY).put(seckills.getSeckillId(), seckills);
            });
        }
    }
}
