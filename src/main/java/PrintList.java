import javafx.application.Application;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

import java.util.Optional;

public class PrintList extends Application {
    public String print(Node node) {
        ChoiceDialog dialog = new ChoiceDialog(Printer.getDefaultPrinter(), Printer.getAllPrinters());
        dialog.setHeaderText("Choose the printer!");
        dialog.setContentText("Choose a printer from available printers");
        dialog.setTitle("Printer Choice");
        boolean justForWhile =true;
        while (justForWhile){
            Optional<Printer> opt = dialog.showAndWait();
        if (opt.isPresent()) {
            Printer printer = opt.get();
            String nameOfPrinter = printer.getName();
            if (nameOfPrinter.contains("PDF") || nameOfPrinter.contains("OneNote") || nameOfPrinter.contains("Fax") || nameOfPrinter.contains("Pdf") || nameOfPrinter.contains("pdf")) {
                Dialog errorDialog = new Dialog();
                errorDialog.setHeaderText("This printer is not available!");
                errorDialog.setTitle("Error choosing printer");
                errorDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                errorDialog.showAndWait();

            } else {
                return nameOfPrinter;

            }

        }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Node node=null;
        String name = print(node);
        Testing.main(name);
    }
}
