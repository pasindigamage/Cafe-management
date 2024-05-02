package lk.ijse.buddiescafe.repository;

import lk.ijse.buddiescafe.db.DbConnection;
import lk.ijse.buddiescafe.model.Employee;
import lk.ijse.buddiescafe.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {
    public static boolean save(Supplier supplier) throws SQLException {
        String sql ="INSERT INTO Supplier VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, supplier.getId());
        pstm.setObject(2, supplier.getNic());
        pstm.setObject(3, supplier.getName());
        pstm.setObject(4, supplier.getCompanyAddress());
        pstm.setObject(5, supplier.getEmail());
        pstm.setObject(6, supplier.getContact());

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Supplier supplier) throws SQLException {
        String sql ="UPDATE Supplier set nic = ?, name = ?, companyAddress = ?, email = ?, contact = ? where id =? ";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setObject(1, supplier.getNic());
        pstm.setObject(2, supplier.getName());
        pstm.setObject(3, supplier.getCompanyAddress());
        pstm.setObject(4, supplier.getEmail());
        pstm.setObject(5, supplier.getContact());
        pstm.setObject(6, supplier.getId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Supplier WHERE id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;

    }

    public static Supplier searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Supplier WHERE id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Supplier supplier = null;

        if (resultSet.next()) {
            String sid = resultSet.getString(1);
            String snic = resultSet.getString(2);
            String name = resultSet.getString(3);
            String companyAddress = resultSet.getString(4);
            String email = resultSet.getString(5);
            String contact = resultSet.getString(6);


            supplier = new Supplier(sid,snic,name,companyAddress,email,contact);
        }
        return supplier;
    }

    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM Supplier";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supplierList = new ArrayList<>();
        while (resultSet.next()) {
            String sId = resultSet.getString(1);
            String sNic = resultSet.getString(2);
            String name = resultSet.getString(3);
            String copmanyAddress = resultSet.getString(4);
            String email = resultSet.getString(5);
            String contact = resultSet.getString(6);

            Supplier supplier = new Supplier(sId,sNic,name,copmanyAddress,email,contact);
            supplierList.add(supplier);
        }
        return supplierList;

    }


    public static List<String> getIds() throws SQLException {
        String sql = "SELECT id FROM Supplier";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> IdList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while(resultSet.next()) {
            IdList.add(resultSet.getString(1));
        }
        return IdList;
    }
}
