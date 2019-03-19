package com.dingding.seckill.dao;

import com.dingding.seckill.entity.Seckill;
import com.dingding.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SuccessKillDAOTest {

    @Resource
    private SuccessKillDAO successKillDAO;
    @Test
    public void insertSuccessKilled() {
        long id = 1000L;
        long phone = 15586531956L;
        int insertCount = successKillDAO.insertSuccessKilled(id, phone);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1000L;
        long phone = 15586531956L;
        SuccessKilled successKilled = successKillDAO.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}