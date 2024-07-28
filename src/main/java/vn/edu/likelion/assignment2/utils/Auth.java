package vn.edu.likelion.assignment2.utils;

import vn.edu.likelion.assignment2.model.User;
import vn.edu.likelion.assignment2.services.UserServices;

import java.sql.SQLException;
import java.util.Scanner;

public class Auth {
    private static final Scanner sc = new Scanner(System.in);
    private static User user = null;
    private UserServices userServices = null;

    public Auth() {
        userServices = new UserServices();
    }

    public void login() throws SQLException {
        System.out.print("Username: \n");
        String username = sc.nextLine();

        //validation username
        if (username.isEmpty()) {
            System.out.println("Username cannot empty.");
            return;
        }

        //Validation password
        System.out.print("Password: \n");
        String password = sc.nextLine();
        if (password.isEmpty()) {
            System.out.println("Mật khẩu không được để trống.");
            return;
        }

        //Encode password to base64
        String passwordEncoded = Helper.encodingStringBase64(password);
        user = userServices.login(username, passwordEncoded);


    }

    public void logout() throws SQLException {
        user = null;
    }

    public static Boolean check() {
        return user != null;
    }

    public static User user() {
        return user;
    }

    public static boolean isAdmin() {
        if (user == null)
            return false;
        return user.getRoleId() == 1;
    }


}
