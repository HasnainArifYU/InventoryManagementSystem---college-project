package husnain.ims.app.ui.controllers.utils;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class RegexCheck {

    private final String text;
    private final String regex;

    public RegexCheck(String text, String regex) {
        this.text = text;
        this.regex = regex;
    }

    public boolean doesntMatch() {
        return !text.matches(regex);
    }
}
