package cn.morethink.netty.router.util;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 李文浩
 * @date 2018/9/5
 */
@Data
public class GeneralResponse implements Serializable {
    private transient HttpResponseStatus status = HttpResponseStatus.OK;
    private String message = "SUCCESS";
    private Object data;

    public GeneralResponse(Object data) {
        this.data = data;
    }

    public GeneralResponse(HttpResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public GeneralResponse(HttpResponseStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
