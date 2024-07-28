package vn.edu.likelion.assignment2.services;

import vn.edu.likelion.assignment2.model.User;
import vn.edu.likelion.assignment2.model.Warehouse;
import vn.edu.likelion.assignment2.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserServices extends Services<User> {
    private DB db = new DB();

    public User login(String username, String password) throws SQLException {
        // Init query string
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * from USERS");
        sqlQuery.append(" where username = ? and password = ? ");

        PreparedStatement preparedStatement;
        Connection connect = db.openConnect();
        ResultSet resultSet;
        try {
            preparedStatement = connect.prepareStatement(sqlQuery.toString());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, new String(password));
            resultSet = preparedStatement.executeQuery();
            User user = null;

            if (resultSet.isBeforeFirst()) {

                while (resultSet.next()) {
                    user = new User();
                    user.setUsername(resultSet.getString("username"));
                    user.setRoleId(resultSet.getInt("role_id"));
                    user.setId(resultSet.getInt("id"));

                }
            } else {
                throw new SQLException("Username or password is incorrect !");
            }
            // after get user, add permission for user by role_id
            connect.close();
            preparedStatement.close();
            resultSet.close();
            return user;

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }


    public User find(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement;
        Connection connect = db.openConnect();
        ResultSet resultSet;

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                if (user == null) {
                    user = new User();
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRoleId(resultSet.getInt("role_id"));
                    user.setId(resultSet.getInt("id"));
                }

            }
            return user;
        }
        return null;
    }

    public ArrayList<User> findAll() throws SQLException {
        ArrayList<User> listUsers = new ArrayList<>();
        String query = "SELECT * FROM users";
        PreparedStatement preparedStatement;
        Connection connect = db.openConnect();
        ResultSet resultSet;

        preparedStatement = connect.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                User user1 = new User();
                user1.setUsername(resultSet.getString("username"));
                user1.setRoleId(resultSet.getInt("role_id"));
                user1.setId(resultSet.getInt("id"));

                listUsers.add(user1);

            }
        }

        return listUsers;
    }

    public void create(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, role_id) VALUES (?,?,?)";
        PreparedStatement preparedStatement;
        Connection connect = db.openConnect();
        ResultSet resultSet;
        connect = db.openConnect();

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setInt(3, user.getRoleId());
        preparedStatement.executeUpdate();


    }


    public void update(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        PreparedStatement preparedStatement;
        Connection connect = db.openConnect();
        ResultSet resultSet;

        connect = db.openConnect();
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setInt(3, user.getId());

        preparedStatement.executeUpdate();

        connect.close();
        preparedStatement.close();
    }

    public void delete(User user) throws SQLException {
        WarehouseServices warehouseServices = new WarehouseServices();
        Warehouse warehouse = warehouseServices.findByUserID(user.getId());
        PreparedStatement preparedStatement;
        Connection connect = db.openConnect();
        ResultSet resultSet;

        connect = db.openConnect();
        String query = null;

        //Check exist warehouse
        if (warehouse != null) {
            warehouseServices.moveProductToWarehouse(warehouse);
            warehouseServices.delete(warehouse);
        }

        query = "DELETE FROM users WHERE id = ?";
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, user.getId());
        preparedStatement.execute();

        connect.close();
        preparedStatement.close();

    }

}
