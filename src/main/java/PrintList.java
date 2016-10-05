import javafx.application.Application;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class PrintList extends Application {
    public String print(Node node) {
        ChoiceDialog dialog = new ChoiceDialog(Printer.getDefaultPrinter(), Printer.getAllPrinters());
        dialog.setHeaderText("Choose the printer!");
        dialog.setContentText("Choose a printer from available printers");
        dialog.setTitle("Printer Choice");
        Optional<Printer> opt = dialog.showAndWait();
        if (opt.isPresent()) {
            Printer printer = opt.get();
            String nameOfPrinter=printer.getName();
            return nameOfPrinter;
        }
        return null;
    }
    public String nameGet(){
        Node node=null;
        String name = print(node);
        return name;
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
