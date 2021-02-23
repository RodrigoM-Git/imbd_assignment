package app.controller;

import io.javalin.http.Handler;
import app.controller.utils.RequestUtil;
import app.dao.AccountDAO;
import app.dao.ReviewDAO;
import app.model.Account;
import app.model.UserReview;

public class UserReviewController {
    
    public static Handler processUserReview = ctx -> {
        String reviewText = ctx.formParam("reviewText");
        String rating = ctx.formParam("rating");
        int titleId = Integer.parseInt(ctx.formParam("titleId"));
        String userName = RequestUtil.getSessionCurrentUser(ctx);
        //Get current user acoount
        Account user = AccountDAO.getAccountByUsername(userName);

        // Adds empty review into database if no existing found, else returns current review for user
        UserReview review = ReviewDAO.addMovieReviewBase(titleId, userName, user);

        // Changes review text to what user inserted, or removes it if empty
        if (reviewText != null){
            ReviewDAO.changeMovieReviewText(review.getReviewID(), reviewText);
        }

        if (rating != null){
            ReviewDAO.changeMovieRating(review.getReviewID(), rating);
        }

        //Goes back to page of title
        ctx.redirect("/title/" + titleId);
    };

    public static Handler removeUserReview = ctx -> {
        int reviewID = Integer.parseInt(ctx.formParam("reviewID"));
        int titleID = Integer.parseInt(ctx.formParam("titleID"));

        ReviewDAO.removeReview(reviewID);

        //Goes back to page of title
        ctx.redirect("/title/" + titleID);
    };
}