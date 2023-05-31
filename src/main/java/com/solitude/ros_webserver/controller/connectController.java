package com.solitude.ros_webserver.controller;

import com.solitude.ros_webserver.pojo.Connect;
import com.solitude.ros_webserver.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("connect")
public class connectController {
    @PostMapping("connect")
    @CrossOrigin
    public Result connect(@RequestBody Connect connect, HttpSession session) {
        Result result = new Result();
        if (connect != null) {
            result.setFlag(true);
            session.setAttribute("target", connect.getTargetAddress());
        } else {
            result.setFlag(false);
            result.setMessage("Connect Error");
        }
        log.info(result.toString());
        return result;
    }
}
