package cn.morethink.netty.router;

import cn.morethink.netty.router.util.GeneralResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

/**
 * @author 李文浩
 * @date 2019/11/29
 */
@Data
public class MyRuntimeException extends RuntimeException {

    private GeneralResponse generalResponse;

    public MyRuntimeException(String message) {
        generalResponse = new GeneralResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, message);
    }

    public MyRuntimeException(HttpResponseStatus status, String message) {
        generalResponse = new GeneralResponse(status, message);
    }

    public MyRuntimeException(GeneralResponse generalResponse) {
        this.generalResponse = generalResponse;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
