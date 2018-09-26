package cn.morethink.netty.router;

import io.netty.handler.codec.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通过uri及http method进行路由
 *
 * @author 李文浩
 * @date 2018/9/23
 */

@Data
@AllArgsConstructor
public class HttpLabel {
    private String uri;
    private HttpMethod method;
}
