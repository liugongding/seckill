package com.dingding.seckill.entity;

import java.util.Date;

/**
 * @author liudingding
 * 成功秒杀明细实体
 */
public class SuccessKilled {

    /**
     * 对应哪个秒杀的id
     */
    private long seckillId;

    /**
     * 用户电话
     */
    private long userPhone;

    /**
     * 秒杀状态
     */
    private short state;

    /**
     * 秒杀成功的创建时间
     */
    private Date createTime;

    /**
     * 一个秒杀记录对应多个实体
     */
    private Seckill seckill;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
