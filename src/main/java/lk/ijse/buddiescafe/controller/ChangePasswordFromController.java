package lk.ijse.buddiescafe.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.buddiescafe.db.DbConnection;
import lk.ijse.buddiescafe.util.Regex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePasswordFromController {

    @FXML
    private JFXButton cancel;

    @FXML
    private TextField employeeId;

    @FXML
    private TextField newPassword;

    @FXML
    private JFXButton save;

    @FXML
    private TextField userName;

    public void initialize(){
        userName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                employeeId.requestFocus();
            }
        });
        employeeId.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                newPassword.requestFocus();
            }
        });

    }
    @FXML
    void cancelOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String uname = userName.getText();
        String eid = employeeId.getText();
        String newPw = newPassword.getText();

        if(isValied()) {
            savePassword(uname, eid, newPw);
        }else {
            new Alert(Alert.AlertType.ERROR, "Password update failed. User not found").show();
        }
    }

    private void savePassword(String uname, String eid, String newPw) {
        try {
            String sql = "UPDATE User SET password = ? WHERE userName = ?";

            // Update based on userName only (assuming unique constraint)
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, newPw);
            pstm.setString(2, uname);

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "New Password Saved!").show();
            } else {
                // No rows updated, handle potential errors (e.g., user not found)
                new Alert(Alert.AlertType.ERROR, "Password update failed. User not found.").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.buddiescafe.util.TextField.password,newPassword)) return false;
        if (!Regex.setTextColor(lk.ijse.buddiescafe.util.TextField.useName,userName)) return false;
        //  if (!) return false;
        // if (!) return false;

        return true;
    }
    @FXML
    void txtEidOnKeyReleased(KeyEvent event) {
        //Regex.setTextColor(lk.ijse.buddiescafe.util.TextField.,userName);
    }

    @FXML
    void txtNewPwOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.buddiescafe.util.TextField.password,newPassword);
    }

    @FXML
    void txtUnameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.buddiescafe.util.TextField.useName,userName);
    }
}
