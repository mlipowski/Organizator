package ch.makery.address.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AboutDialogController {

	private Stage dialogStage;

	@FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleZamknij() {
            dialogStage.close();
        }


}


