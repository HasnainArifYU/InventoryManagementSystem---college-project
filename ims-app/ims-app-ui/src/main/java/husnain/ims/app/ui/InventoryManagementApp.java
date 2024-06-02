package husnain.ims.app.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FUTURE ENHANCEMENT: This class should be used to start the application if the
 * JavaFX modules are declared correctly as VM options.
 *
 * @author Husnain Arif
 */
public class InventoryManagementApp extends Application {

    private static final Logger LOG = Logger.getLogger(InventoryManagementApp.class.getName());

    /**
     * The main entry point for all JavaFX applications.
     * <p>
     * The start method is called after the init method has returned, and after
     * the system is ready for the application to begin running.
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param stage the primary stage for this application, onto which the
     *              application scene can be set.
     */
    @Override
    public void start(Stage stage) {
        Scene scene = null;

        try {
            scene = new Scene(this.createParent());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the application.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Parent createParent() throws IOException {
        return FXMLLoader.load(this.getClass().getResource("MainForm.fxml"));
    }

}
