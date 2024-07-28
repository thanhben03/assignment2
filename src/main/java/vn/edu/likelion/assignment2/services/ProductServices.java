package vn.edu.likelion.assignment2.services;

import vn.edu.likelion.assignment2.interfaces.DBInterface;
import vn.edu.likelion.assignment2.model.Product;
import vn.edu.likelion.assignment2.model.Warehouse;
import vn.edu.likelion.assignment2.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductServices extends Services<Product> {
    private DB db = new DB();

    @Override
    public void create(Product product) throws SQLException {
        String query = "INSERT INTO products " +
                "(product_name, product_desc, product_quantity, product_price, warehouse_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement;
        Connection connect;
        connect = db.openConnect();

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, product.getProductName());
        preparedStatement.setString(2, product.getProductDesc());
        preparedStatement.setInt(3, product.getProductQuantity());
        preparedStatement.setInt(4, product.getProductPrice());
        preparedStatement.setInt(5, product.getWarehouseId());
        preparedStatement.execute();

        connect.close();
        preparedStatement.close();
    }

    @Override
    public Product find(int id) throws SQLException {
        String query = "SELECT * FROM products WHERE id = ?";
        Connection connect = db.openConnect();
        PreparedStatement preparedStatement;
        ResultSet resultSet = null;

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        Product product = new Product();

        // if warehouse exist
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                product.setId(resultSet.getInt("id"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductDesc(resultSet.getString("product_desc"));
                product.setProductQuantity(resultSet.getInt("product_quantity"));
                product.setProductPrice(resultSet.getInt("product_price"));
                product.setWarehouseId(resultSet.getInt("warehouse_id"));


            }
            return product;
        }
        return null;
    }

    // return all product in db
    public ArrayList<Product> findAll() throws SQLException {
        ArrayList<Product> listProducts = new ArrayList<>();
        String query = "SELECT * FROM products";
        Connection connect = db.openConnect();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        preparedStatement = connect.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        // if warehouse exist
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductDesc(resultSet.getString("product_desc"));
                product.setProductQuantity(resultSet.getInt("product_quantity"));
                product.setProductPrice(resultSet.getInt("product_price"));
                product.setWarehouseId(resultSet.getInt("warehouse_id"));

                listProducts.add(product);

            }
        }

        return listProducts;
    }

    // return all product in db by warehouse
    public ArrayList<Product> findAllByWarehouseId(Warehouse warehouse) throws SQLException {
        ArrayList<Product> listProducts = new ArrayList<>();
        String query = "SELECT * FROM products WHERE warehouse_id = ?";
        Connection connect = db.openConnect();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, warehouse.getID());
        resultSet = preparedStatement.executeQuery();

        // if warehouse exist
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductDesc(resultSet.getString("product_desc"));
                product.setProductQuantity(resultSet.getInt("product_quantity"));
                product.setProductPrice(resultSet.getInt("product_price"));
                product.setWarehouseId(resultSet.getInt("warehouse_id"));

                listProducts.add(product);

            }
        }

        return listProducts;
    }



}
