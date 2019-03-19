package com.dingding.seckill.service.impl;

import com.dingding.seckill.dao.SeckillDAO;
import com.dingding.seckill.dao.SuccessKillDAO;
import com.dingding.seckill.dto.Exposer;
import com.dingding.seckill.dto.SeckillExecution;
import com.dingding.seckill.entity.Seckill;
import com.dingding.seckill.entity.SuccessKilled;
import com.dingding.seckill.enums.SeckillStateEnum;
import com.dingding.seckill.exception.RepeatKillException;
import com.dingding.seckill.exception.SeckillCloseException;
import com.dingding.seckill.exception.SeckillException;
import com.dingding.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author liudingding
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 设置秒杀redis缓存的key
     */
    private final String KEY = "seckill";

    @Resource
    private SeckillDAO seckillDAO;
    @Resource
    private SuccessKillDAO successKillDAO;
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 盐值
     */
    private final String slat = "dfa123EDdasSA,./";
    /**
     * md5加密
     */
    private String md5 = null;

    @Override
    public List<Seckill> getSeckillList() {
        //1、首页全部加载进缓存
        List<Seckill> seckillList = redisTemplate.boundHashOps(KEY).values();
        if (seckillList == null || seckillList.size() == 0) {
            //2、如果缓存中没有数据、从数据库中取出来添加到缓存
            seckillList = seckillDAO.queryAll(0, 4);
            seckillList.forEach(seckills -> {
                redisTemplate.boundHashOps(KEY).put(seckills.getSeckillId(), seckills);
            });
        }
        return seckillList;
    }

    @Override
    public Seckill getById(long seckillId) {
        Seckill seckill = (Seckill) redisTemplate.boundHashOps(KEY).get(seckillId);
        if (seckill == null) {
            seckill = seckillDAO.queryById(seckillId);
        }
        return seckill;
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //首先到缓存中去找
        Seckill seckill = (Seckill) redisTemplate.boundHashOps(KEY).get(seckillId);
        //没有找到秒杀对象
        if (seckill == null) {
            seckill = seckillDAO.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }
            redisTemplate.boundHashOps(KEY).put(seckill.getSeckillId(), seckill);
        }
        //获取数据库的时间字段
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        //秒杀未开启或秒杀已结束
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 生成md5
     *
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    public synchronized SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存+记录购买行为
        Date now = new Date();
        Seckill seckill = null;
        try {
            //查询当前版本号
            seckill = seckillDAO.queryById(seckillId);
            int version = seckill.getVersion();
            logger.info("版本号：" + version);
            //减库存
            int updateCount = seckillDAO.reduceNumber(seckillId, now, version);

            if (updateCount <= 0) {
                //没有更新记录
                throw new SeckillCloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKillDAO.insertSuccessKilled(seckillId, userPhone);
                //主键冲突、重复秒杀
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKillDAO.queryByIdWithSeckill(seckillId, userPhone);
                    //更新缓存
                    seckill = (Seckill) redisTemplate.boundHashOps(KEY).get(seckillId);
                    if (seckill == null) {
                        seckill = seckillDAO.queryById(seckillId);
                    }
                    seckill.setNumber(seckill.getNumber() - 1);
                    redisTemplate.boundHashOps(KEY).put(seckillId, seckill);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e) {
            throw e;
        }
    }
}
