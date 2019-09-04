package com.andy.strategy;

/**
 * @author lianhong
 * @description 策略模式
 * @date 2019/8/7 0007上午 9:24
 */
public abstract class InspectionSolver {
    public abstract void solve(Long orderId,long userId);
    public abstract String[] supports();
}
