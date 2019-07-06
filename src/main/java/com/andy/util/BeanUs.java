package com.andy.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author lianhong
 * @description
 * @date 2019/6/19 0019下午 8:09
 */
public class BeanUs extends BeanUtils {
    /**
     * orig 的属性值不为空才copy到dest中
     *
     * @param target
     * @param source
     */
    public static void copyProsNotNull(Object target, Object source) {
        try {
            Assert.notNull(source, "Source must not be null");
            Assert.notNull(target, "Target must not be null");
            Class<?> actualEditable = target.getClass();

            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
            PropertyDescriptor[] arr = targetPds;
            int len = targetPds.length;

            for(int var = 0; var < len; ++var) {
                PropertyDescriptor targetPd = arr[var];
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null) {
                    PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if (sourcePd != null) {
                        Method readMethod = sourcePd.getReadMethod();
                        if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }

                                Object value = readMethod.invoke(source);
                                if (value == null ){
                                    continue;
                                }
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }

                                writeMethod.invoke(target, value);
                            } catch (Throwable var15) {
                                throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", var15);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("BeanUs拷贝" + target.getClass() +"to"+ source.getClass() +"属性异常");
        }
    }
}
