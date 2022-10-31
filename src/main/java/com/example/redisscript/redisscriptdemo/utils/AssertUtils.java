package com.example.redisscript.redisscriptdemo.utils;

import com.example.redisscript.redisscriptdemo.exception.BaseException;
import org.springframework.util.StringUtils;

public interface AssertUtils {

    /**
     * 创建异常
     *
     * @param args
     * @return
     */
    BaseException newException(Object... args);

    /**
     * 创建异常
     *
     * @param t
     * @param args
     * @return
     */
    BaseException newException(Throwable t, Object... args);


    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj  待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj  待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertHasText(String obj, Object... args) {
        if (StringUtils.hasText(obj) && StringUtils.hasText(StringUtils.trimAllWhitespace(obj))) {
            throw newException(args);
        }
    }
}
