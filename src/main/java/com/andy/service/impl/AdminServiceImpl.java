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
            size=10;
        }
        Integer page = tourAdmin.getPage()==null?0:(tourAdmin.getPage()-1)*size;
        List<TourAdmin> list = adminDao.findAll(page,size);
        if(list==null) return vo;
        vo.setRows(list);
        Integer count = adminDao.getCount();
        vo.setTotal(count);
        return vo;
    }
}
