package cn.morethink.netty.controller;

import cn.morethink.netty.model.User;
import cn.morethink.netty.router.RequestMapping;
import cn.morethink.netty.util.GeneralResponse;
import cn.morethink.netty.util.JsonUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoController {

    @RequestMapping(uri = "/login", method = "POST")
    public GeneralResponse login(FullHttpRequest request) {
        User user = JsonUtil.fromJson(request, User.class);
        log.info("/login called,user: {}", user);
        return new GeneralResponse(null);
    }

    public void logout() {
        System.out.println("/logout called");
    }
}
