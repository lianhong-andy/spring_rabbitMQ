package com.andy.strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lianhong
 * @description 策略模式
 * @date 2019/8/7 0007上午 9:24
 */
public class InspectionSolverChooser implements ApplicationContextAware {
    Map<String, InspectionSolver> chooserMap = new HashMap<>();

    public InspectionSolver choose(String type) {
        return chooserMap.get(type);
    }

    @PostConstruct
    public void register() {
        Map<String, InspectionSolver> solverMap = applicationContext.getBeansOfType(InspectionSolver.class);
        for (InspectionSolver inspectionSolver : solverMap.values()) {
            for (String support : inspectionSolver.supports()) {
                chooserMap.put(support,inspectionSolver);
            }
        }
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
