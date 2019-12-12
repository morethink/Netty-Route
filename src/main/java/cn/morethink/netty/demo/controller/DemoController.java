package cn.morethink.netty.demo.controller;

import cn.morethink.netty.demo.model.User;
import cn.morethink.netty.router.RequestBody;
import cn.morethink.netty.router.RequestMapping;
import cn.morethink.netty.router.util.GeneralResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoController {

    @RequestMapping(uri = "/login", method = "POST")
    public GeneralResponse login(@RequestBody User user, FullHttpRequest request,
                                 String test, Integer test1, int test2,
                                 long[] test3, Long test4, String[] test5, int[] test6) {
        System.out.println(test2);
        log.info("/login called,user: {} ,{} ,{} {} {} {} {} {} {} {} ", user, test, test1, test2, test3, test4, test5, test6);
        return new GeneralResponse(null);
    }

    public void logout() {
        System.out.println("/logout called");
    }
}
