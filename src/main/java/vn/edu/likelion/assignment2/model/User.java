package vn.edu.likelion.assignment2.model;

import vn.edu.likelion.assignment2.services.UserServices;

import java.util.ArrayList;

public class User extends UserServices {
    private int id;
    private String username;
    private String password;
    private int roleId;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String value) {
        this.password = value;
    }


    public void setRoleId(int role_id) {
        this.roleId = role_id;
    }

    public int getRoleId() {
        return this.roleId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
