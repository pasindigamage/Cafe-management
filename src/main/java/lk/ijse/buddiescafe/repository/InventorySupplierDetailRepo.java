package lk.ijse.buddiescafe.repository;

import lk.ijse.buddiescafe.db.DbConnection;
import lk.ijse.buddiescafe.model.InventorySupplierDetail;
import lk.ijse.buddiescafe.model.OrderDetail;
import lk.ijse.buddiescafe.model.TM.InventorySupplierDetailTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventorySupplierDetailRepo {
    public static boolean save(InventorySupplierDetail inventoryDetail) throws SQLException {
        String sql = "INSERT INTO inventorySupplier VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, inventoryDetail.getId());
            pstm.setString(2, inventoryDetail.getSupplierId());
            pstm.setString(3, inventoryDetail.getInventoryId());
            pstm.setDate(4, java.sql.Date.valueOf(String.valueOf(inventoryDetail.getDate())));
            pstm.setDouble(5, inventoryDetail.getUnitPrice());  // Corrected to setDouble for unitPrice
            pstm.setInt(6, inventoryDetail.getQty());
              // Assuming getDate returns LocalDate
            return pstm.executeUpdate() > 0;
        }
    }

    public static boolean update(InventorySupplierDetail inventoryDetail) throws SQLException {
        String sql = "UPDATE inventorySupplier SET supplierId = ?, inventoryId = ?, unitPrice = ?, qty = ?, date = ? WHERE id = ?";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, inventoryDetail.getSupplierId());
            pstm.setString(2, inventoryDetail.getInventoryId());
            pstm.setDouble(3, inventoryDetail.getUnitPrice());  // Corrected to setDouble for unitPrice
            pstm.setInt(4, inventoryDetail.getQty());
            pstm.setDate(5, java.sql.Date.valueOf(String.valueOf(inventoryDetail.getDate())));  // Assuming getDate returns LocalDate
            pstm.setString(6, inventoryDetail.getId());
            return pstm.executeUpdate() > 0;
        }
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM inventorySupplier WHERE id = ?";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, id);
            return pstm.executeUpdate() > 0;
        }
    }

    public static InventorySupplierDetail searchByID(String id) throws SQLException {
        String sql = "SELECT * FROM inventorySupplier WHERE id = ?";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, id);
            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    return new InventorySupplierDetail(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getDouble(5),
                            resultSet.getInt(6)
                            );
                }
            }
        }
        return null;
    }

    public static List<InventorySupplierDetailTM> getAll() throws SQLException {
        String sql = "SELECT inventorySupplier.id, Inventory.description, Supplier.name," +
                " inventorySupplier.date, inventorySupplier.unitPrice, inventorySupplier.qty" +
                " FROM Inventory " +
                "Join inventorySupplier " +
                "ON Inventory.id = inventorySupplier.inventoryId " +
                "JOIN Supplier" +
                " ON inventorySupplier.supplierId = Supplier.id";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery()) {
            List<InventorySupplierDetailTM> inventoryTMList = new ArrayList<>();
            while (resultSet.next()) {
                InventorySupplierDetailTM inventoryTM = new InventorySupplierDetailTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),  // Assuming getDate returns LocalDate
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                );
                inventoryTMList.add(inventoryTM);
            }
            return inventoryTMList;
        }
    }

    public static boolean updateQty(List<OrderDetail> odList) {
        try (Connection connection = DbConnection.getInstance().getConnection()) {
            String sql = "UPDATE inventorySupplier SET qty = qty - ? WHERE id = ?";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                for (OrderDetail od : odList) {
                    pstm.setInt(1, od.getQty());
                    pstm.setString(2, od.getItemCode());
                    pstm.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String currentId() throws SQLException {
        String sql = "SELECT id FROM inventorySupplier ORDER BY id desc LIMIT 1";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        }
    }

    public static InventorySupplierDetail searchByDescription(String ingrediansIDValue) throws SQLException {
        String sql = "SELECT * FROM inventorySupplier WHERE description = ?";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, ingrediansIDValue);
            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    return new InventorySupplierDetail(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getDouble(5),
                            resultSet.getInt(6)
                    );
                }
            }
        }
        return null;
    }
}