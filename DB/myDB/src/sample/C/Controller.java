package sample.C;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static javafx.scene.input.KeyCode.M;

public class Controller {

    @FXML
    private TextField syntax;

    @FXML
    private Button button;
    private sample.M.DBManager myManager =new sample.M.DBManager();

    @FXML
    void push(ActionEvent event) {
        myManager.manager(syntax.getText());

    }

}