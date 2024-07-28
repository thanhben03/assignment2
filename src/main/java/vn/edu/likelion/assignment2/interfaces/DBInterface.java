package vn.edu.likelion.assignment2.interfaces;

import java.sql.SQLException;

public interface DBInterface<T> {
    void create (T t) throws SQLException;
    T find (int id) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
}