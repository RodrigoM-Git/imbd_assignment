package app.controller.paths;


/**
 * his class contains the UTL fragments of the site. If you are going to login, then the
 * URL is going to be https://someurl.com/login
 *
 * If you want to add pages, you need to add that information here.
 */
public class Web {

    public static final String INDEX = "/";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String ACCOUNT = "/account";
    public static final String TITLE_SEARCH = "/titleSearch";
    public static final String ACTOR_SEARCH = "/actorSearch";
    public static final String TITLE = "/title/:title_id";
    public static final String ACTOR = "/actor/:actor_id";
    public static final String REVIEW = "/title/review";
    public static final String REMOVE_REVIEW = "/removereview";
    public static final String SHOW_ENTRY = "/showEntry";
    public static final String SHOW_ENTRY_TARGET = "/showEntryTarget";
    public static final String SHOW_ENTRY_FINAL = "/showEntryFinal";
    public static final String REVIEW_ENTRY_LIST = "/reviewEntryList";
    public static final String ENTRY = "/entry/:entryId";
    public static final String ENTRY_DECIDED = "/entryDecided";
    public static final String REGISTER = "/register";
    public static final String BEFORE_REGISTER = "/beforeRegister";
    public static final String CONFIRM_REGISTER = "/confirmRegistration";
    public static final String REGISTRATION_LIST = "/registrationList";
    public static final String REGISTRATION = "/registration/:username";
    public static final String REGISTRATION_DECISION = "/registrationFinal";
    public static final String TITLE_EDIT = "/titleEdit";
    public static final String TITLE_EDIT_FINAL = "/titleEditSuccess";
}
