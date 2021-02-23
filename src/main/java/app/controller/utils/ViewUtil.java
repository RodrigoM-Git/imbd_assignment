package app.controller.utils;


import app.controller.paths.Template;
import app.dao.AccountDAO;
import io.javalin.http.Context;
import io.javalin.http.ErrorHandler;
import java.util.HashMap;
import java.util.Map;





public class ViewUtil {


    public static Map<String, Object> baseModel(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        String username = RequestUtil.getSessionCurrentUser(ctx);
        model.put("currentUser", username);
        model.put("currentUserType", AccountDAO.getAccType(username));
        return model;
    }

    public static ErrorHandler notFound = ctx -> {
        ctx.render(Template.NOT_FOUND, baseModel(ctx));
    };

}
