package com.andy.service.impl;

import com.andy.dao.AdminDao;
import com.andy.domain.TourAdmin;
import com.andy.domain.common.PageVo;
import com.andy.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;
    @Override
    public PageVo findAll(TourAdmin tourAdmin) {
        PageVo vo = new PageVo();
        Integer size = tourAdmin.getSize();
        if(size==null){
            size=100;
        }
        Integer page = tourAdmin.getPage()==null?0:(tourAdmin.getPage()-1)*size;
        List<TourAdmin> list = adminDao.findAll(page,size);
        if(list==null) return vo;
        vo.setRows(list);
        Integer count = adminDao.getCount();
        vo.setTotal(count);
        return vo;
    }

    @Override
    public void testStrategyPattern(String taskType){
        if ("BATCH_CHANGE_WAREHOUSE".equals(taskType)) {
            //批量转仓逻辑
        } else if ("BATCH_CHANGE_SHIPPING".equals(taskType)) {
            //批量转快递逻辑
        } else if ("BATCH_REPLACE_ORDER_GOODS".equals(taskType)) {
            //批量替换订单商品逻辑
        } else if ("BATCH_DELETE_ORDER_GOODS".equals(taskType)) {
            //批量删除订单商品逻辑
        } else if ("BATCH_ADD_MEMO".equals(taskType)) {
            //批量添加备注逻辑
        } else {
            //任务类型未知
            System.out.println("任务类型无法处理");
        }
    }
}
