package cn.morethink.netty.demo.controller;

import cn.morethink.netty.demo.model.User;
import cn.morethink.netty.router.RequestBody;
import cn.morethink.netty.router.RequestMapping;
import cn.morethink.netty.router.util.GeneralResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DemoController {

    @RequestMapping(uri = "/test", method = "POST")
    public GeneralResponse test(@RequestBody User user, String test, Integer test1, int test2,
                                long[] test3, Long test4, String[] test5, int[] test6) {
        log.info("user={},test={},test1={},test2={},test3={},test4={},test5={},test6={}",
                user, test, test1, test2, test3, test4, test5, test6);
        return new GeneralResponse(null);
    }

    @RequestMapping(uri = "/test1", method = "POST")
    public GeneralResponse testList(@RequestBody List<User> users) {
        log.info("users={}", users);
        return new GeneralResponse(null);
    }

    @RequestMapping(uri = "/testMap", method = "POST")
    public GeneralResponse testMap(@RequestBody Map<String, Map<String, User>> users) {
        log.info("users={}", users);
        return new GeneralResponse(null);
    }
}
