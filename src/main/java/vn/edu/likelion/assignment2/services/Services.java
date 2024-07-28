package vn.edu.likelion.assignment2.services;

import vn.edu.likelion.assignment2.interfaces.DBInterface;

import java.sql.SQLException;

public class Services<V> implements DBInterface<V> {
    @Override
    public void create(V o) throws SQLException {}

    @Override
    public V find(int id) throws SQLException {
        return null;
    }

    @Override
    public void update(V o) throws SQLException {}

    @Override
    public void delete(V o) throws SQLException {}
}
