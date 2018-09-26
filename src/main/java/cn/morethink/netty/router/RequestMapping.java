package cn.morethink.netty.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 运行时可以反射类方法得到注解信息
 *
 * @author 李文浩
 * @date 2018/9/23
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    /**
     * 路由的uri
     *
     * @return
     */
    String uri();

    /**
     * 路由的方法
     *
     * @return
     */
    String method();
}
