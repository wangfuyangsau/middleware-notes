package org.example.middleware.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
public class LoginEvent extends ApplicationEvent implements Serializable {
    private String userName;
    private String loginTime;
    private String loginIp;

    public LoginEvent(Object source, String userName, String loginTime, String loginIp) {

        super(source);
        this.loginIp=loginIp;
        this.loginTime=loginTime;
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Override
    public String toString() {
        return "loginEvent{" +
                "source=" + source +
                '}';
    }
}
