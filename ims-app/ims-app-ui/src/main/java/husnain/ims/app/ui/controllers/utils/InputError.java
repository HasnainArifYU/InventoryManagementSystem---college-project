package husnain.ims.app.ui.controllers.utils;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public record InputError(String title, String detail) {

    public InputError(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", title, detail);
    }
}
