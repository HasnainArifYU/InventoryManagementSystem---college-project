package husnain.ims.app.ui.controllers.utils;

import javafx.scene.control.Label;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class PlaceholderLabel extends Label {

    public PlaceholderLabel(String details) {
        super(details);
        //Give the text a pale shade of gray
        //Then, increase the text size by a little bit
        super.setStyle("-fx-text-fill: #D3D3D3; -fx-font-size: 1.2em");
    }

}
