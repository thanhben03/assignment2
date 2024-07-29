package vn.edu.likelion.assignment2;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import vn.edu.likelion.assignment2.model.Product;
import vn.edu.likelion.assignment2.model.User;
import vn.edu.likelion.assignment2.model.Warehouse;
import vn.edu.likelion.assignment2.services.ProductServices;
import vn.edu.likelion.assignment2.services.UserServices;
import vn.edu.likelion.assignment2.services.WarehouseServices;
import vn.edu.likelion.assignment2.utils.Auth;
import vn.edu.likelion.assignment2.utils.Helper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private static final Scanner sc = new Scanner(System.in);
    private static final UserServices userServices = new UserServices();
    private static final WarehouseServices warehouseServices = new WarehouseServices();
    private static final ProductServices productServices = new ProductServices();
    private static Auth auth = new Auth();

    public static void main(String[] args) throws SQLException {
        int choose = 0;
        while (true) {

            // Check login before into menu
            try {
                if (!Auth.check()) {
                    System.out.println("Please login to use menu !");
                    auth.login();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                continue;
            }

            // call main menu
            mainMenu();
            try {
                choose = Helper.getUserInput();
            } catch (NumberFormatException e) {
                System.err.println("Invalid choose, please enter again !");
                continue;
            }
            switch (choose) {
                // Manage User
                case 1:
                    //show submenu manage user
                    int chooseSubMenu;
                    try {
                        manageUserMenu();
                        chooseSubMenu = Helper.getUserInput();
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                        continue;
                    }

                    switch (chooseSubMenu) {
                        // Create User
                        case 1:
                            // show form create user
                            formCreateUser();
                            break;
                        case 2:
                            // Show all user in db
                            showAllUser();
                            // show form edit user
                            formEditUser();
                            break;
                        case 3:
                            // Show all user
                            showAllUser();

                            // delete user
                            try {
                                System.out.println("Enter user ID: ");
                                int chooseUserID = Helper.getUserInput();
                                User user = userServices.find(chooseUserID);
                                if (user == null)
                                    throw new SQLException("User not found !");
                                userServices.delete(user);

                            } catch (SQLException | NumberFormatException e) {
                                System.err.println(e.getMessage());
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    int chooseWareHouseMenu;
                    try {
                        // Manage Warehouse
                        manageWarehouseMenu();
                        chooseWareHouseMenu = Helper.getUserInput();
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                        continue;
                    }
                    switch (chooseWareHouseMenu) {
                        // Form create warehouse
                        case 1:
                            formCreateWarehouse();
                            sc.nextLine();
                            break;
                        // Form edit warehouse
                        case 2:
                            try {
                                formEditWarehouse();
                            } catch (SQLException | NumberFormatException e) {
                                System.err.println(e.getMessage());
                            }
                            break;
                        // Delete warehouse
                        case 3:
                            // Show all warehouse
                            showAllWarehouse();

                            // delete

                            try {
                                System.out.print("Enter warehouse ID: ");
                                int warehouseID = Helper.getUserInput();
                                Warehouse warehouse = warehouseServices.find(warehouseID);
                                warehouseServices.delete(warehouse);

                            } catch (SQLException | NumberFormatException e) {
                                System.err.println(e.getMessage());
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:
                    // Manage Product
                    manageProductMenu();
                    int chooseProductMenu = 0;
                    try {
                        chooseProductMenu = Helper.getUserInput();
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid choose, please enter again !");
                        continue;
                    }
                    switch (chooseProductMenu) {
                        case 1:
                            readFileExcelToDB();
                            break;
                        case 2, 3:
                            System.out.println("Function under construction, come back later!");
                            break;
//                        case 4:
//                            showAllProduct();
//                            break;
                        default:
                            break;
                    }
                    break;
                case 4:
                    // Statics
                    try {
                        warehouseServices.handleExcelFile();
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case 5:
                    auth.logout();
                    break;
                case 6:
                    System.exit(1);
                    break;
                case 7:
                    showInfoWarehouse();
                    break;
                default:
                    break;
            }
        }
    }

    private static void mainMenu() {


        System.out.println("========Menu========");
        if (Auth.user().getRoleId() == 1) {
            System.out.println("1. Manage User");
            System.out.println("2. Manage Warehouse");

        }
        System.out.println("3. Manage Product");
        System.out.println("4. Statistic");
        System.out.println("5. Logout");
        System.out.println("6. Exit");
        System.out.println("7. View Info Warehouse");
        System.out.print("----------------------\nEnter your choose:");
    }

    private static void manageUserMenu() {
        if (!Auth.isAdmin()) {
            throw new NumberFormatException("You do not have permission to use this function!");
        }
        System.out.println("========Manage User========");
        System.out.println("1. Create User");
        System.out.println("2. Edit User");
        System.out.println("3. Delete User");
        System.out.print("----------------------\nEnter your choose:");

    }

    private static void manageWarehouseMenu() {
        if (!Auth.isAdmin()) {
            throw new NumberFormatException("You do not have permission to use this function!");
        }
        System.out.println("========Manage Warehouse========");
        System.out.println("1. Create Warehouse");
        System.out.println("2. Edit Warehouse");
        System.out.println("3. Delete Warehouse");
        System.out.print("----------------------\nEnter your choose:");

    }
    private static void manageProductMenu() {
        System.out.println("========Manage Product========");
        if (Auth.user().getRoleId() == 1) {
            System.out.println("1. Create Product");
            System.out.println("2. Edit Product");
            System.out.println("3. Delete Product");
        } else {
            System.out.println("1. Create Product");

        }
        System.out.print("----------------------\nEnter your choose:");

    }

    private static void formCreateUser() {
        if (!Auth.isAdmin()) {
            System.out.println("You do not have permission to use this function!");
            return;
        }
        System.out.println("=========Create User========");
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        while (username.isEmpty()) {
            System.err.println("Username cannot empty, please enter again !");
            username = sc.nextLine();
        }
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        while (password.trim().isEmpty()) {
            System.err.println("Password cannot empty, please enter again !");
            password = sc.nextLine();
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(Helper.encodingStringBase64(password));
            user.setRoleId(2); // role user

            userServices.create(user);
            System.out.println("Create User Success !");

        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                System.err.println("username is already taken!");
            } else {
                System.out.println(e.getMessage());
            }
        }

    }
    private static void formCreateWarehouse() {
        System.out.println("=========Create Warehouse========");

        System.out.print("Enter warehouse name: ");
        String warehouseName = sc.nextLine();

        Warehouse warehouse = new Warehouse();

        while (warehouseName.isEmpty()) {
            System.out.println("Warehouse name cannot empty, please enter again !");
            warehouseName = sc.nextLine();
        }
        warehouse.setWarehouseName(warehouseName);

        System.out.print("Enter User ID (-1 skip): ");
        int userId = sc.nextInt();

        User user = null;
        try {
            if (userId != -1) {
                user = userServices.find(userId);
                if (user != null) {
                    Warehouse warehouseExist = warehouseServices.findByUserID(user.getId());
                    if (warehouseExist != null) {
                        throw new SQLException("This user has been assigned a warehouse !");
                    }
                    warehouse.setUserId(userId);
                }
            }
            warehouseServices.create(warehouse);
            StringBuilder message = new StringBuilder("Assign warehouse (" + warehouseName + ")");

            if (user != null) {
                message.append(" for user (" + user.getUsername() + ")");
            }

            System.out.println(message);
            sc.nextLine();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                System.err.println("Warehouse name is already taken!");
            } else {
                System.out.println(e.getMessage());
            }
        }


    }

    private static void formEditUser() {
        System.out.println("========Edit User========");

        try {
            System.out.print("Enter ID User: ");
            int userId = Helper.getUserInput();
            User user = new User();
            user = userServices.find(userId);
            if (user == null) {
                throw new SQLException("User not found !");
            }

            System.out.print("Edit username(" + user.getUsername() + "): ");
            String username = sc.nextLine();
            if (!username.trim().isEmpty()) {
                user.setUsername(username);
            }

            System.out.print("Edit password: ");
            String password = sc.nextLine();

            //If the user does not enter a password
            if (password.trim().isEmpty()) {
                user.setPassword(user.getPassword());
            }
            else { // encrypt the password
                user.setPassword(Helper.encodingStringBase64(password));
            }

            userServices.update(user);
            System.out.println("Updated Successfully !");

        } catch (SQLException | NumberFormatException e) {
            System.err.println(e.getMessage());
            return;
        }

    }


    private static void formEditWarehouse() throws SQLException, NumberFormatException {
        System.out.println("========Edit Warehouse========");

        // Show all warehouse in db
        ArrayList<Warehouse> listWarehouses;
        listWarehouses = warehouseServices.findAll();
        for (Warehouse w : listWarehouses) {
            System.out.println("Warehouse ID: " + w.getID() + "\t\t Warehouse Name: " + w.getWarehouseName());
        }

        System.out.print("Enter ID Warehouse: ");
        int warehouseId = Helper.getUserInput(); // get warehouse id
        Warehouse warehouse = null;
        try {
            warehouse = warehouseServices.find(warehouseId); // get warehouse instant
            if (warehouse == null)
                throw new NullPointerException("Warehouse not found !");
        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Edit warehouse name (" + warehouse.getWarehouseName() + "): ");
        String warehouseName = sc.nextLine();

        // if the warehouse name not empty
        if (!warehouseName.trim().isEmpty()) {
            warehouse.setWarehouseName(warehouseName);
        }

        System.out.print("Edit User ID: (-1 to skip)");
        int userId = Helper.getUserInput();

        if (userId != -1 ) {
            Warehouse warehouseExist = warehouseServices.findByUserID(userId);
            if (warehouseExist != null) {
                throw new SQLException("This user has been assigned a warehouse !");
            }
            warehouse.setUserId(userId);
        }

        try {
            warehouseServices.update(warehouse);
            System.out.println("Updated Successfully !");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1)
                System.err.println("Warehouse name is already taken!");
        }
    }

    private static void showAllUser() {
        try {
            ArrayList<User> users = userServices.findAll();

            for (User u : users) {
                System.out.println("ID: " + u.getId() + "\t\t" + "Username: " + u.getUsername());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void showAllWarehouse() {
        try {
            ArrayList<Warehouse> warehouses = warehouseServices.findAll();

            for (Warehouse w : warehouses) {
                System.out.println("ID: " + w.getID() + "\t\t" + "Warehouse name: " + w.getWarehouseName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void showAllProduct() {
        try {
            ArrayList<Product> products = productServices.findAll();
            System.out.println("There are " + products.size() + " product in warehouse");
            for (Product p : products) {
                System.out.println("ID: " + p.getId() + "\n"
                        + "Product name: " + p.getProductName() + "\n"
                        + "Product description: " + p.getProductDesc() + "\n"
                        + "Product quantity: " + p.getProductQuantity() + "\n"
                        + "Product price: " + p.getProductPrice())
                ;
                System.out.println("-----------------------------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readFileExcelToDB() {
        System.out.print("Enter file name: ");
        String nameFile = sc.nextLine();

        Warehouse warehouse = null;

        // If you are an admin, you can choose the warehouse to import
        if (Auth.isAdmin()) {
            System.out.println("Enter warehouse ID: ");
            int warehouseId = Helper.getUserInput();
            try {
                warehouse = warehouseServices.find(warehouseId);
                if (warehouse.getUserId() == 0) {
                    System.err.println("This warehouse has not been assigned a User");
                    return;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            if (warehouse == null) {
                System.out.println("Warehouse not found !");
                return;
            }
        }
        else { // Otherwise, just get the current inventory that the user has
            try {
                warehouse = warehouseServices.findByUserID(Auth.user().getId());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Importing product into warehouse (" + warehouse.getWarehouseName() + "):");
        try {
            Sheet sheet = Helper.getSheet(nameFile);
            StringBuilder str = new StringBuilder();
            for (int i = 5; ;i++) {
                // Get first cell
                Cell firstCellOfRow = sheet.getRow(i).getCell(0);

                // check if row empty
                if (firstCellOfRow.toString().isEmpty()) {
                    break;
                }

                String productName = sheet.getRow(i).getCell(1).toString(); // get product name
                String desc = sheet.getRow(i).getCell(2).toString(); // get desc
                String quantity = sheet.getRow(i).getCell(3).toString(); // get quantity product
                String price = sheet.getRow(i).getCell(4).toString(); // get price product

                Product product = new Product();
                product.setProductName(productName);
                product.setProductDesc(desc);
                product.setProductQuantity(Integer.parseInt(quantity.split("\\.")[0]));
                product.setProductPrice(Integer.parseInt(price.split("\\.")[0]));
                product.setWarehouseId(warehouse.getID());

                productServices.create(product);

            }

            System.out.println("Imported successfully !");
        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void showInfoWarehouse() {

        try {
            // show all info warehouse if admin
            if (Auth.isAdmin()) {
                showInfoAllWarehouse();
                return;
            }

            // Show only warehouse of user
            Warehouse warehouse = warehouseServices.findByUserID(Auth.user().getId());
            System.out.println("--------Info Warehouse (" + warehouse.getWarehouseName() + ")--------");
            ArrayList<Product> listProducts = productServices.findAllByWarehouseId(warehouse);
            for (Product p : listProducts) {
                System.out.println("ID" + p.getId()
                        + "\t | \tProduct Name: " + p.getProductName()
                        + "\t | \tProduct Quantity: " + p.getProductQuantity()
                        + "\t | \tProduct Price: " + p.getProductPrice())
                ;
                System.out.println("------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showInfoAllWarehouse() throws SQLException {
        ArrayList<Warehouse> listWarehouses = warehouseServices.findAll();
        for (Warehouse w : listWarehouses) {
            ArrayList<Product> listProducts = productServices.findAllByWarehouseId(w);
            System.out.println("--------Info Warehouse(" + w.getWarehouseName() + ")--------");
            if (!listProducts.isEmpty()) {
                for (Product p : listProducts) {
                    System.out.println("ID" + p.getId()
                            + "\t | \tProduct Name: " + p.getProductName()
                            + "\t | \tProduct Quantity: " + p.getProductQuantity()
                            + "\t | \tProduct Price: " + p.getProductPrice())
                    ;
                    System.out.println("------------------------------------------");

                }
            } else {
                System.err.println("There are no product in this warehouse !");
            }

        }
    }

}
