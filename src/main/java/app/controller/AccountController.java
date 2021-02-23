package app.controller;

import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.AccountDAO;
import app.model.Account;
import io.javalin.http.Handler;
import java.util.Map;



public class AccountController {

    public static Handler serveAccountPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        String username = ctx.sessionAttribute("currentUser");
        
        Account currentUser = AccountDAO.getAccountByUsername(username);
        
        
        model.put("user", currentUser);
        ctx.render(Template.ACCOUNT, model);
    };




}
