package com.dingding.seckill.service;

import com.dingding.seckill.dto.Exposer;
import com.dingding.seckill.dto.SeckillExecution;
import com.dingding.seckill.entity.Seckill;
import com.dingding.seckill.exception.RepeatKillException;
import com.dingding.seckill.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}" + list);
    }

    @Test
    public void getById() {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info(":" + seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long id = 1000;
//        long userPhone = 155
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}" + exposer);
    }

    @Test
    public void executeSeckill() {
        long id = 1000;
        long userPhone = 15586531956L;
        String md5 = "b3b1bd1b49e1c585ee444dc8cf5de24e";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage(), e);
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage(), e);
        }
    }
}