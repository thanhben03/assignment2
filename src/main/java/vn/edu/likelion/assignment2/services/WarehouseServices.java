package vn.edu.likelion.assignment2.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.edu.likelion.assignment2.model.Product;
import vn.edu.likelion.assignment2.model.Warehouse;
import vn.edu.likelion.assignment2.utils.Auth;
import vn.edu.likelion.assignment2.utils.DB;
import vn.edu.likelion.assignment2.utils.Helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WarehouseServices extends Services<Warehouse> {
    private DB db = new DB();
    private Connection connect;
    private ResultSet resultSet;

    @Override
    public void create(Warehouse warehouse) throws SQLException {
        String query = "INSERT INTO warehouses (warehouse_name, user_id) VALUES (?, ?)";
        PreparedStatement preparedStatement;
        connect = db.openConnect();

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, warehouse.getWarehouseName());
        preparedStatement.setInt(2, warehouse.getUserId());
        preparedStatement.execute();

        connect.close();
        preparedStatement.close();
    }

    @Override
    public void update(Warehouse warehouse) throws SQLException {
        String query = "UPDATE warehouses SET warehouse_name = ?, user_id = ? WHERE id = ?";
        connect = db.openConnect();
        PreparedStatement preparedStatement;

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, warehouse.getWarehouseName());
        preparedStatement.setInt(2, warehouse.getUserId());
        preparedStatement.setInt(3, warehouse.getID());

        preparedStatement.executeUpdate();

        connect.close();
        preparedStatement.close();
        resultSet.close();
    }

    @Override
    public Warehouse find(int id) throws SQLException {
        String query = "SELECT * FROM warehouses WHERE id = ?";
        connect = db.openConnect();
        PreparedStatement preparedStatement;

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        Warehouse warehouse = null;
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                if (warehouse == null) {
                    warehouse = new Warehouse();
                    warehouse.setWarehouseName(resultSet.getString("warehouse_name"));
                    warehouse.setUserId(resultSet.getInt("user_id"));
                    warehouse.setID(resultSet.getInt("id"));
                }

            }
            return warehouse;
        }
        return null;
    }

    public Warehouse findByUserID(int userId) throws SQLException {
        String query = "SELECT * FROM warehouses WHERE user_id = ?";
        connect = db.openConnect();
        PreparedStatement preparedStatement;

        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        resultSet = preparedStatement.executeQuery();
        Warehouse warehouse = null;
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                if (warehouse == null) {
                    warehouse = new Warehouse();
                    warehouse.setWarehouseName(resultSet.getString("warehouse_name"));
                    warehouse.setUserId(resultSet.getInt("user_id"));
                    warehouse.setID(resultSet.getInt("id"));
                }

            }
            return warehouse;
        }
        return null;
    }

    public ArrayList<Warehouse> findAll() throws SQLException {
        ArrayList<Warehouse> listWarehouses = new ArrayList<>();
        String query = "SELECT * FROM warehouses";
        connect = db.openConnect();
        PreparedStatement preparedStatement;

        preparedStatement = connect.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setWarehouseName(resultSet.getString("warehouse_name"));
                warehouse.setUserId(resultSet.getInt("user_id"));
                warehouse.setID(resultSet.getInt("id"));

                listWarehouses.add(warehouse);

            }
        }

        return listWarehouses;
    }

    @Override
    public void delete(Warehouse warehouse) throws SQLException {
        moveProductToWarehouse(warehouse);
        String query = "DELETE FROM warehouses WHERE id = ?";
        connect = db.openConnect();
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, warehouse.getID());

        preparedStatement.execute();
        System.out.println("Deleted warehouse (" + warehouse.getWarehouseName() + ") successfully !" );
        preparedStatement.close();
        connect.close();


    }

    public void moveProductToWarehouse(Warehouse warehouse) throws SQLException {
        //Check exist product in warehouse
        String query = "SELECT * FROM products WHERE warehouse_id = ?";
        PreparedStatement preparedStatement;
        connect = db.openConnect();
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, warehouse.getID());
        resultSet = preparedStatement.executeQuery();

        // if the product not empty
        if (resultSet.next()) {
            System.out.println("There are products in stock, please move to a new warehouse to delete!");
            query = "UPDATE products SET warehouse_id = ? WHERE warehouse_id = ?";
            System.out.println("Enter new warehouse ID: ");
            int warehouseId = 0;

            // Move all product to new warehouse
            warehouseId = Helper.getUserInput();

            Warehouse newWarehouse;
            newWarehouse = find(warehouseId);

            // warehouse not found
            if (newWarehouse == null) {
                throw new SQLException("New warehouse does not exist, please check again!");
            }

            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, warehouseId);
            preparedStatement.setInt(2, warehouse.getID());

            preparedStatement.execute();
        }

        preparedStatement.close();
        resultSet.close();
        connect.close();
    }

    public void handleExcelFile() throws SQLException {
        ProductServices productServices = new ProductServices();
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();


        // Handle export excel for admin
        if (Auth.user().getRoleId() == 1) {
            //init first row for label
            Row row = sheet.createRow(0);
            // init first cell value for label
            Cell cell = row.createCell(1);
            cell.setCellValue("Warehouse Name");
            Cell cell2 = row.createCell(2);
            cell2.setCellValue("Total Product");

            ArrayList<Warehouse> warehouses;
            ArrayList<Product> listProducts = null;
            warehouses = findAll();
            int rowCount = 1;
            int cellCount = 1;
            int totalProduct = 0;
            for (Warehouse w : warehouses) {
                listProducts = productServices.findAllByWarehouseId(w);
                int totalProductInWarehouse = listProducts.size();

                // create row inside sheet
                row = sheet.createRow(rowCount);

                // create cell and insert value
                cell = row.createCell(cellCount);
                cell.setCellValue(w.getWarehouseName());
                cell2 = row.createCell(++cellCount);
                cell2.setCellValue(totalProductInWarehouse);
                rowCount++;
                cellCount = 1;
                totalProduct += totalProductInWarehouse;
            }
            row = sheet.createRow(rowCount);
            cell = row.createCell(0);
            cell.setCellValue("Total");
            cell2 = row.createCell(1);
            cell2.setCellValue(warehouses.size());
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(totalProduct);
            // tạo ra 1 file excel vật lý
            exportToExcel(workbook, "admin.xlsx");
        }
        else { // handle excel for user
            //init first row for label
            Row row = sheet.createRow(0);
            // init first cell value for label

            Cell cell = row.createCell(0);
            cell.setCellValue("Warehouse Name");
            Cell cell2 = row.createCell(1);
            cell2.setCellValue("Total Product");

            Warehouse warehouse = findByUserID(Auth.user().getId());
            if (warehouse == null) {
                throw new SQLException("Warehouse not found !");
            }
            ArrayList<Product> products = productServices.findAllByWarehouseId(warehouse);

            // create row inside sheet
            row = sheet.createRow(1);

            // create cell and insert value
            cell = row.createCell(0);
            cell.setCellValue(warehouse.getWarehouseName());
            cell2 = row.createCell(1);
            cell2.setCellValue(products.size());
            exportToExcel(workbook, "user.xlsx");
        }
    }

    private void exportToExcel(Workbook workbook, String nameFile) {
        try {
            FileOutputStream fos = new FileOutputStream(nameFile);
            workbook.write(fos);
            System.out.println("Exported excel file successfully !");

            workbook.close();
            fos.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }
}
