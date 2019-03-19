package com.dingding.seckill.enums;

/**
 * 使用枚举 表示 常量的数据字段
 *
 * @author liudingding
 */
public enum SeckillStateEnum {
    /**
     * 秒杀成功
     */
    SUCCESS(1, "秒杀成功"),
    /**
     * 秒杀结束
     */
    END(0, "秒杀结束"),
    /**
     * 重复秒杀
     */
    REPEAT_KILL(-1, "重复秒杀"),
    /**
     * 系统异常
     */
    INNER_ERROR(-2, "系统异常"),
    /**
     * 数据篡改
     */
    DATA_REWRITE(-3, "数据篡改");

    private int state;

    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateEnum stateOf(int index) {
        for (SeckillStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
