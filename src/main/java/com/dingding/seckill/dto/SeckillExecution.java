package com.dingding.seckill.dto;

import com.dingding.seckill.entity.SuccessKilled;
import com.dingding.seckill.enums.SeckillStateEnum;

/**
 * 封装秒杀结束后的结果
 *
 * @author liudingding
 */
public class SeckillExecution {

    /**
     * 成功秒杀的id
     */
    private long seckillId;

    /**
     * 秒杀执行结果状态
     */
    private int setate;

    /**
     * 状态表示
     */
    private String stateInfo;

    /**
     * 成功秒杀对象
     */
    private SuccessKilled successKilled;

    /**
     * 成功秒杀后的执行结果
     *
     * @param seckillId
     * @param stateEnum
     * @param successKilled
     */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.setate = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    /**
     * 失败秒杀后的执行结果
     *
     * @param seckillId
     * @param stateEnum
     */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.setate = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getSetate() {
        return setate;
    }

    public void setSetate(int setate) {
        this.setate = setate;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", setate=" + setate +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
