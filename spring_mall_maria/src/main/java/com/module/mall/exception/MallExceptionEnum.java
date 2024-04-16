package com.module.mall.exception;

/**
 * 异常的枚举
 */
public enum MallExceptionEnum {
    // 10001 控制器中报错
    // 10002 服务中报错
    REQUEST_PARAM_ERROR(10003, "参数错误"),
    NEED_USER_NAME(10004, "用户名不能为空"),
    NEED_PASSWORD(10005, "密码不能为空"),
    PASSWORD_TOO_SHORT(10006, "密码长度不能小于8位"),
    NAME_EXISTED(10007, "不允许重名，注册失败"),
    WRONG_PASSWORD(10008, "密码错误"),
    NEED_LOGIN(10009, "用户未登录"),
    NEED_ADMIN(10010, "无管理员权限"),
    CREATE_FAILED(10011, "新增失败"),
    UPDATE_FAILED(10012, "更新失败"),
    DELETE_FAILED(10013, "删除失败"),
    INSERT_FAILED(10014, "插入失败，请重试"),
    SEARCH_FAILED(10015, "查询不到信息"),
    MKDIR_FAILED(10016, "文件夹创建失败"),
    UPLOAD_FAILED(10017, "上传失败"),
    NO_ENUM(10018, "未找到对应的枚举"),
    EMAIL_ALREADY_BEEN_REGISTERED(10019, "email地址已被注册"),
    EMAIL_ALREADY_BEEN_SEND(10020, "email已发送，若无法收到，请稍后再试"),
    NEED_EMAIL_ADDRESS(10021, "email不能为空"),
    NEED_VERIFICATION_CODE(10022, "验证码不能为空"),
    WRONG_VERIFICATION_CODE(10023, "验证码错误"),
    TOKEN_EXPIRED(10024, "token过期"),
    TOKEN_WRONG(10025, "token解析失败"),
    WRONG_EMAIL(10026, "非法的邮件地址"),
    SYSTEM_ERROR(20000, "系统异常，请从控制台或日志中查看具体错误信息");

    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    MallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
