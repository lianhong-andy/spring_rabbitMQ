package com.andy.strategy.handler;

import com.andy.strategy.InspectionSolver;

/**
 * @author lianhong
 * @description 订单批量转仓处理
 * @date 2019/8/7 0007上午 9:59
 */
public class ChangeWarehouseSolver extends InspectionSolver {

    @Override
    public void solve(Long orderId, long userId) {

    }

    @Override
    public String[] supports() {
        return new String[0];
    }
}
