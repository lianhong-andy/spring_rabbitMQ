package com.andy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
@Service
public class TestServiceImpl implements ServletContextAware, InitializingBean {
    private ServletContext servletContext;
    private Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("xxxxxxxxxxxxxxxxxxxxxxxx");
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
