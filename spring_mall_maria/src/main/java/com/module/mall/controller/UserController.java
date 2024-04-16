package com.module.mall.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.module.mall.common.ApiRestResponse;
import com.module.mall.common.Constant;
import com.module.mall.exception.MallException;
import com.module.mall.exception.MallExceptionEnum;
import com.module.mall.model.pojo.User;
import com.module.mall.service.EmailService;
import com.module.mall.service.UserService;
import com.module.mall.utils.EmailUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.module.mall.filter.UserFilter.currentUser;


/**
 * 描述：用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;


    @PostMapping("/register")
    @ApiOperation("注册")
    public ApiRestResponse register(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            //@RequestParam("manager_name")  特殊参数描述, 动态注入到name
            //@RequestParam(value="username",defaultValue = "默认用户") String name
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("verificationCode") String verificationCode
    ) throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        if (StringUtils.isEmpty(emailAddress)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_EMAIL_ADDRESS);
        }
        if (StringUtils.isEmpty(verificationCode)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_VERIFICATION_CODE);
        }
        //如果邮箱已注册，则不允许再次注册
        boolean emailPassed = userService.checkEmailRegistered(emailAddress);
        if (!emailPassed) {
            return ApiRestResponse.error(MallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
        }
        //校验邮箱和验证码是否匹配
        Boolean passEmailAndCode = emailService.checkEmailAndCode(emailAddress, verificationCode);
        if (!passEmailAndCode) {
            return ApiRestResponse.error(MallExceptionEnum.WRONG_VERIFICATION_CODE);
        }
        userService.register(userName, password, emailAddress);
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    @ApiOperation("登陆（弃用）")
    public ApiRestResponse login(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            HttpSession httpSession
    ) throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        user.setPassword(null);
        // 这里保存的时候，尽管看着key都是一个，但是当多用户的时候，实际是独立的每个session域，sessionId各不相同
        // 会触发浏览器Set-Cookie方法，返回sessionId
        // session的缺点：扩展性差，需要服务端存储，当用户量大或者后期使用分布式服务器的时候会出现问题。
        // 优点简单方便
        httpSession.setAttribute(Constant.MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    @PostMapping("/updateSignature")
    @ApiOperation("更新个性签名")
    public ApiRestResponse updateUserSignature(
//            HttpSession httpSession,
            // @RequestParam 必须传入否则404，默认值
            @RequestParam(value = "signature", required = true, defaultValue = "默认签名") String signature
    ) throws MallException {
//        User sessionUser = (User) httpSession.getAttribute(Constant.MALL_USER);
//        if (sessionUser == null) {
//            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
//        }
        User user = new User();
//        user.setId(sessionUser.getId());
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateUserSignature(user);
        Map<String, String> res = new HashMap<>();
        res.put("newSignature", signature);
        return ApiRestResponse.success(res);
    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    public ApiRestResponse logout(
            HttpSession httpSession
    ) throws MallException {
        httpSession.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }

    @PostMapping("/adminLogin")
    @ApiOperation("管理员登陆（弃用）")
    public ApiRestResponse adminLogin(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            HttpSession httpSession
    ) throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        // 是否是管理员
        if (userService.checkAdminRole(user)) {
            user.setPassword(null);
            httpSession.setAttribute(Constant.MALL_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }


    @PostMapping("/getUserById")
    @ApiOperation("根据id查找用户")
    public ApiRestResponse getUserById(
            String id //接收formdata
    ) throws MallException {
        String Id = id;
        if (StringUtils.isEmpty(Id)) {
            return ApiRestResponse.error("请输入id");
        }
        User user = userService.getUserById(Id);
        user.setPassword(null);
        if (user == null) {
            return ApiRestResponse.error("无此用户");
        } else {
            return ApiRestResponse.success(user);
        }
    }

    @PostMapping("/getUserByNameJdbc")
    @ApiOperation("idbc方法根据用户名查找")
    public ApiRestResponse getUserByNameJdbc(
            String name //接收formdata
    ) throws MallException {
        if (StringUtils.isEmpty(name)) {
            return ApiRestResponse.error("请输入用户名");
        }
        List<Map<String, Object>> user = userService.getUserByNameJdbc(name);
        user.get(0).put("password", null);
        if (user == null) {
            return ApiRestResponse.error("无此用户");
        } else {
            return ApiRestResponse.success(user);
        }
    }

    @PostMapping("/sendEmail")
    @ApiOperation("发送验证码邮件")
    public ApiRestResponse sendEmail(@RequestParam("emailAddress") String emailAddress)
            throws MallException {
        //检查邮件地址是否有效，检查是否已注册
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(emailAddress);
        if (validEmailAddress) {
            boolean emailPassed = userService.checkEmailRegistered(emailAddress);
            if (!emailPassed) {
                return ApiRestResponse.error(MallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
            } else {
                String verificationCode = EmailUtil.genVerificationCode();
                Boolean saveEmailToRedis = emailService.saveEmailToRedis(emailAddress, verificationCode);
                if (saveEmailToRedis) {
                    emailService.sendSimpleMessage(emailAddress, Constant.EMAIL_SUBJECT, "欢迎注册，您的验证码是" + verificationCode);
                    return ApiRestResponse.success();
                } else {
                    return ApiRestResponse.error(MallExceptionEnum.EMAIL_ALREADY_BEEN_SEND);
                }
            }
        } else {
            return ApiRestResponse.error(MallExceptionEnum.WRONG_EMAIL);
        }
    }

    // JWT
    // 组成： 头部 信息体 签名。前面两个通过base64转码而成，签名是根据前面两个信息绑定的东西，当前面两个被篡改之后，会导致签名不一致，从而校验失败。
    // 优点： 减少存储开销，可扩展性强（比如分布式时候，每个服务器都可以解析信息），同时用于认证和交换信息，防止被伪造和篡改
    // 缺点： 默认不加密（不适合保存敏感信息），无法临时废止 登出需要额外处理（可设置有效期），有效期不易评估，网络开销高（要传的东西多）

    @PostMapping("/loginWithJwt")
    @ApiOperation("JWT方式登陆")
    public ApiRestResponse loginWithJwt(@RequestParam String userName, @RequestParam String password) throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create()
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                //过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
        return ApiRestResponse.success(token);
    }


    @PostMapping("/adminLoginWithJwt")
    @ApiOperation("JWT管理员登录")
    public ApiRestResponse adminLoginWithJwt(@RequestParam("userName") String userName,
                                             @RequestParam("password") String password)
            throws MallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //校验是否是管理员
        if (userService.checkAdminRole(user)) {
            //是管理员，执行操作
            //保存用户信息时，不保存密码
            user.setPassword(null);
            Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
            String token = JWT.create()
                    .withClaim(Constant.USER_NAME, user.getUsername())
                    .withClaim(Constant.USER_ID, user.getId())
                    .withClaim(Constant.USER_ROLE, user.getRole())
                    //过期时间
                    .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                    .sign(algorithm);
            return ApiRestResponse.success(token);
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

}
