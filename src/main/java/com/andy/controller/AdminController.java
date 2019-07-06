package com.andy.controller;

import com.andy.domain.TourAdmin;
import com.andy.domain.common.PageVo;
import com.andy.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String TRACEID = "mmtraceid";
    @Autowired
    private AdminService adminService;
    @RequestMapping("/findAll")
    @ResponseBody
    public PageVo findAll(HttpServletRequest request, TourAdmin tourAdmin){
        String requestURI = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();
        String traceId = request.getHeader(TRACEID);
        logger.info(traceId);
        logger.info("requestURI:{} " , requestURI);
        logger.info("requestURL :{}" ,requestURL.toString());
        return adminService.findAll(tourAdmin);
    }
}
