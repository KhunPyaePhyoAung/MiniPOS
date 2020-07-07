package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.views.controllers.TaxConfigController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class TaxConfigWindow extends PosWindowStage{
    public TaxConfigWindow(Runnable onEditTax){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/tax_config_view.fxml"));
            Parent view = fxmlLoader.load();
            TaxConfigController controller = fxmlLoader.getController();
            controller.setTaxListener(onEditTax);
            setScene(new Scene(view));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

