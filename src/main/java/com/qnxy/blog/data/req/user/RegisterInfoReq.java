package com.qnxy.blog.data.req.user;

import com.qnxy.blog.core.annotations.FieldValueEqualityCheck;
import com.qnxy.blog.data.GenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * @author Qnxy
 */
@Data
@FieldValueEqualityCheck(value = "confirmPassword", targetFieldName = "password", message = "再次确认输入的密码不一致")
public class RegisterInfoReq {

    /**
     * 用户名称, 唯一
     */
    @Length(min = 5, max = 20, message = "用户名长度需要在5-20之间")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户设置的密码
     */
    @Length(min = 5, max = 18, message = "密码长度需要在5-18之间")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 再次确认输入的密码
     */
    private String confirmPassword;

    /**
     * 用户手机号
     */
    @Pattern(regexp = "^1\\d{10}$", message = "输入手机号格式不正确")
    private String phoneNun;

    /**
     * 用户头像
     */
    @NotBlank(message = "没有添加头像信息")
    private String userAvatar;

    /**
     * 性别
     */
    @NotNull(message = "输入性别错误")
    private GenderType gender;

    /**
     * 生日
     */
    @NotNull(message = "生日不能为空")
    @Past(message = "生日日期不能是以后的日期")
    private LocalDate birthday;


}
