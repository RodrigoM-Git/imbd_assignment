package app;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import static io.javalin.apibuilder.ApiBuilder.*;

import app.controller.AccountController;
import app.controller.ActorSearchController;
import app.controller.TitleSearchController;
import app.controller.UserReviewController;
import app.controller.ActorPageController;
import app.controller.IndexController;
import app.controller.LoginController;
import app.controller.RegistrationController;
import app.controller.ReviewEntryController;
import app.controller.ReviewRegistrationController;
import app.controller.ShowEntryController;
import app.controller.TitlePageController;
import app.controller.paths.Web;
import app.controller.utils.ViewUtil;





public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
        }).start(getHerokuAssignedPort());

        app.routes(() -> {
            // You will have to update this, to limit who can see the reviews
            // before(LoginController.ensureLoginBeforeViewing);

            get(Web.INDEX, IndexController.serveIndexPage);

            get(Web.LOGIN, LoginController.serveLoginPage);
            post(Web.LOGIN, LoginController.handleLoginPost);
            post(Web.LOGOUT, LoginController.handleLogoutPost);

            get(Web.ACCOUNT, AccountController.serveAccountPage);
            
            post(Web.TITLE_SEARCH, TitleSearchController.serveTitleSearch);
            post(Web.ACTOR_SEARCH, ActorSearchController.serveActorSearch);

            get(Web.TITLE, TitlePageController.serveTitlePage);
            get(Web.ACTOR, ActorPageController.serveActorPage);

            post(Web.REVIEW, UserReviewController.processUserReview);
            post (Web.REMOVE_REVIEW, UserReviewController.removeUserReview);
            
            get(Web.SHOW_ENTRY, ShowEntryController.serveShowEntry);
            post(Web.SHOW_ENTRY_TARGET, ShowEntryController.serveShowEntryTarget);
            post(Web.SHOW_ENTRY_FINAL, ShowEntryController.serveShowEntryFinal);
            
            get(Web.REVIEW_ENTRY_LIST, ReviewEntryController.serveReviewEntryList);
            get(Web.ENTRY, ReviewEntryController.serveEntryPage);
            post(Web.ENTRY_DECIDED, ReviewEntryController.serveEntryDecidedPage);
            
            
            get(Web.BEFORE_REGISTER, RegistrationController.serveBeforeRegister);
            post(Web.REGISTER, RegistrationController.serveRegister);
            post(Web.CONFIRM_REGISTER, RegistrationController.serveConfirm);
            
            get(Web.REGISTRATION_LIST, ReviewRegistrationController.serveReviewRegistrationList);
            get(Web.REGISTRATION, ReviewRegistrationController.serveRegistration);
            post(Web.REGISTRATION_DECISION, ReviewRegistrationController.serveRegistrationDecision);
            
            get(Web.TITLE_EDIT, TitlePageController.serveTitleEditPage);
            post(Web.TITLE_EDIT_FINAL, TitlePageController.serveEditFinal);
            // Add new actions here
            // Seeing pages (get) and sending information in forms (post)
        });

        app.error(404, ViewUtil.notFound);
    }






    public static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000;
    }



}
