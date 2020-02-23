package com.alfredbase.utils;

import java.lang.reflect.Method;

public class CopyBeanUtils {
  public static void copyBean(Object sourceBean, Object targetBean) {

    if (sourceBean == null || targetBean == null) {
 //       new RuntimeException("传入对象为空！");
    	targetBean=null;
    	return;
    }

    Method[] mthsSource = sourceBean.getClass().getMethods();

    Method[] mthsTarget = targetBean.getClass().getMethods();
    for (Method method : mthsTarget) {
        if (method.getName().startsWith("set")) {
            // 复制成员变量值
            copyValue(method, mthsSource, sourceBean, targetBean);
        }
    }
  }
  
  private static void copyValue(Method methodTarget, Method[] mthsSource,
      Object sourceBean, Object targetBean) {

  String name = "get" + methodTarget.getName().substring(3);
  try {
      for (Method method : mthsSource) {
          if (name.equals(method.getName())) {
              methodTarget.invoke(targetBean, method.invoke(sourceBean));
              break;
          }
      }
  } catch (Exception e) {
      throw new RuntimeException(e);
  }

}
}
