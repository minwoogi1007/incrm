package com.eincrm.model;


import java.util.Date;

public class Menu {


    private Long menuCd;
    private String userId; //여기 user_id 는 직원인 경우는 권한 레벨 10~99 , 업체 당담자일 경우 프로젝트 코드
    private String authority;
    private Date instDt;
    private String instUser;
    private Date updDt;
    private String updUser;
    // Standard getters and setters


    public Long getMenuCd() {
        return menuCd;
    }

    public void setMenuCd(Long menuCd) {
        this.menuCd = menuCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Date getInstDt() {
        return instDt;
    }

    public void setInstDt(Date instDt) {
        this.instDt = instDt;
    }

    public String getInstUser() {
        return instUser;
    }

    public void setInstUser(String instUser) {
        this.instUser = instUser;
    }

    public Date getUpdDt() {
        return updDt;
    }

    public void setUpdDt(Date updDt) {
        this.updDt = updDt;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }
}
