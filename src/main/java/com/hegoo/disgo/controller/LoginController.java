package com.hegoo.disgo.controller;

import com.hegoo.disgo.model.User;
import com.hegoo.disgo.utils.Result;
import com.hegoo.disgo.utils.ResultCode;
import com.hegoo.disgo.utils.TokenUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class LoginController {

    private static CopyOnWriteArraySet<String> userNameList = new CopyOnWriteArraySet<>();

    private final static String USER_NAME_CLAIMED_MSG = "This user name has been claimed!";
    private final static String SYSTEM_NAME = "System";

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        Result result = null;
        String name = user.getName();
        if (userNameList.contains(name) || SYSTEM_NAME.equals(name)) {
            result = new Result(ResultCode.UNAUTHORIZED);
            result.setData(USER_NAME_CLAIMED_MSG);
            return result;
        }
        userNameList.add(name);
        String token = TokenUtils.generateToken(user.getName());
        result = new Result(ResultCode.SUCCESS_WITH_TOKEN);
        result.setData(token);
        return result;
    }

}
