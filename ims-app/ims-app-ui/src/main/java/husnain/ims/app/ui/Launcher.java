package husnain.ims.app.ui;

//import husnain.ims.app.sample.data.Generate;
/**
 * FUTURE ENHANCEMENT: This class shouldn't be used if the JavaFX modules are
 * declared correctly as VM options.
 * <p>
 * This class starts the application.
 * <p>
 * It also contains a commented out line that could be used to generate test
 * data for the application.
 *
 * @author Husnain Arif
 */
public class Launcher {

    /**
     * The applications main method.
     * <p>
     * Find the aggregated JavaDocs in {@code /ims-app/javadoc}.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        //Uncomment this line if you want to generate test data
        /*
         * new Generate().sampleData();
         */

        InventoryManagementApp.main(args);
    }
}
