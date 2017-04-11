/*
 * 文 件 名:  EventUtil.java
 * 描    述:  <描述>
 * 修 改 人:   袁立位
 * 修改时间:  2015年9月8日
 */
package com.ylw.common.utils.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.requestviewer.MainApp;

/**
 * @description 通知
 * @author 袁立位
 * @date 2015年9月8日 下午2:15:43
 */
public class EventUtil {
	
	private static Log log = LogFactory.getLog(MainApp.class);

    private static ConcurrentNavigableMap<String, List<InvOkModel>> map = new ConcurrentSkipListMap<String, List<InvOkModel>>();

    /**
     * @description 注册通知接收者<br/>
     *              收通知的方法以：<b>onEvent</b>开头
     * @param obj
     * @author 袁立位
     * @date 2015年9月8日 下午2:55:47
     */
    public static void register(Object obj) {
        unregister(obj);
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
//            log.debug(method.getName());
            if (method.getName().startsWith("onEvent")) {
                Class<?> pType = method.getParameterTypes()[0];
                List<InvOkModel> list = map.get(pType.getName());
                if (list == null) {
                    list = new ArrayList<InvOkModel>();
                    map.put(pType.getName(), list);
                }
                list.add(new InvOkModel(obj, method));
            }
        }
    }

    public static void unregister(Object obj) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("onEvent")) {
                log.debug(method.getName());
                Class<?> pType = method.getParameterTypes()[0];
                List<InvOkModel> list = map.get(pType.getName());
                if (list == null) {
                    continue;
                } else {
                    for (InvOkModel m : list) {
                        if (m.obj.equals(obj)) {
                            list.remove(m);
                        }
                    }
                }
            }
        }
    }

    public static void post(Object obj) {
        if (obj == null)
            return;
        List<InvOkModel> list = map.get(obj.getClass().getName());
        for (InvOkModel m : list) {
            try {
                m.method.invoke(m.obj, obj);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    static class InvOkModel {
        Object obj;
        Method method;

        public InvOkModel(Object obj, Method method) {
            super();
            this.obj = obj;
            this.method = method;
        }
    }
}
