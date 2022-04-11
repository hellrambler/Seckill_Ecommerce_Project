package com.seckillproject.service.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserModel {
    private Integer id;

    @NotBlank(message = "Username cannot be empty")
    private String name;

    @NotNull(message = "Gender cannot be omitted")
    private Byte gender;

    @NotNull(message = "Age cannot be omitted")
    @Min(value = 0, message = "Age cannot be less than 0")
    @Max(value = 150, message = "Age may not be over 150")
    private Integer age;

    @NotBlank(message = "Mobile number cannot be empty")
    private String mobile;
    private String registerMode;
    private String thirdPartyId;

    @NotBlank(message = "Password cannot be empty")
    private String encryptPassword;

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }
}
