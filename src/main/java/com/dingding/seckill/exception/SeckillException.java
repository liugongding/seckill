package com.dingding.seckill.exception;

/**
 * 秒杀业务相关夜场
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
