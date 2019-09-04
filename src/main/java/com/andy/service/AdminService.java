package com.andy.service;

import com.andy.domain.TourAdmin;
import com.andy.domain.common.PageVo;

import java.util.List;

public interface AdminService {
    /**
     * 查找所有用户
     * @return
     */
    PageVo findAll(TourAdmin tourAdmin);

    void testStrategyPattern(String taskType);
}
