package com.dingding.seckill.dao;

import com.dingding.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author liudingding
 */
@Mapper
public interface SeckillDAO {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @Param version
     * @return 如果受影响的行数 > 1, 表示更新记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime, @Param("version") int version);

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param start
     * @param size
     * @return
     */
    List<Seckill> queryAll(@Param("start") int start, @Param("size") int size);
}
