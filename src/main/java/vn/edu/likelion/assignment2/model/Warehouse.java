package vn.edu.likelion.assignment2.model;

public class Warehouse {
    private int ID;
    private String warehouseName;
    private int userId;

    public Warehouse() {
        warehouseName = null;
        userId = 0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
