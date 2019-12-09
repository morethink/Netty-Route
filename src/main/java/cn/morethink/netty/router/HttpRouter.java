package cn.morethink.netty.router;

import cn.morethink.netty.util.GeneralResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李文浩
 * @date 2018/9/23
 */

@Slf4j
public class HttpRouter extends ClassLoader {

    private Map<HttpLabel, Action<GeneralResponse>> httpRouterAction = new HashMap<>();

    private String classpath = this.getClass().getResource("").getPath();

    private Map<String, Object> controllerBeans = new HashMap<>();

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = classpath + name.replaceAll("\\.", "/");
        byte[] bytes;
        try (InputStream ins = new FileInputStream(path)) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024 * 5];
                int b;
                while ((b = ins.read(buffer)) != -1) {
                    out.write(buffer, 0, b);
                }
                bytes = out.toByteArray();
            }
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    public void addRouter(String controllerClass) {
        try {
            Class<?> cls = loadClass(controllerClass);
            Method[] methods = cls.getDeclaredMethods();
            for (Method invokeMethod : methods) {
                Annotation[] annotations = invokeMethod.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == RequestMapping.class) {
                        RequestMapping requestMapping = (RequestMapping) annotation;
                        String uri = requestMapping.uri();
                        String httpMethod = requestMapping.method().toUpperCase();
                        // 保存Bean单例
                        if (!controllerBeans.containsKey(cls.getName())) {
                            controllerBeans.put(cls.getName(), cls.newInstance());
                        }
                        Action action = new Action(controllerBeans.get(cls.getName()), invokeMethod);
                        //如果需要FullHttpRequest，就注入FullHttpRequest对象
                        Class[] params = invokeMethod.getParameterTypes();
                        if (params.length == 1 && params[0] == FullHttpRequest.class) {
                            action.setInjectionFullhttprequest(true);
                        }
                        // 保存映射关系
                        httpRouterAction.put(new HttpLabel(uri, new HttpMethod(httpMethod)), action);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("{}", e);
        }
    }

    public Action getRoute(HttpLabel httpLabel) {
        return httpRouterAction.get(httpLabel);
    }
}
