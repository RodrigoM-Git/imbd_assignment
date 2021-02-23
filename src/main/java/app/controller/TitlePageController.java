package app.controller;

import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.AccountDAO;
import app.dao.TitleDAO;
import app.model.Account;
import app.model.CreditsRoll;
import app.model.Show;
import io.javalin.http.Handler;
import java.util.List;
import java.util.Map;

public class TitlePageController {
	
	// Show (title), to be set if id is valid
	private static Show show;
	
	private static Account user;

    public static Handler serveTitlePage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        // Value that keeps track if input so far is valid
        boolean validTitle = true;
        // Id of title to be parsed from path parameter in try block
        int title_id = -1;
      
        String currentUsername = ctx.sessionAttribute("currentUser");
        user = AccountDAO.getAccountByUsername(currentUsername);
        
        if(user != null) {
        	model.put("user", user);
        }
        
        try {
            // Parse id from path to an integer
            title_id = Integer.parseInt(ctx.pathParam("title_id"));
        } catch (NumberFormatException e){
            // Title is invalid as id isn't an integer
            validTitle = false;
        }
        // Checks if id was parsed as int correctly
        if (validTitle){
            // Sees if title with id can be found (also checks to see if all fields are valid)
            show = TitleDAO.getTitleFromId(title_id);
            // Checks that show was set correctly
            if (show == null){
                // Sets validTitle to false, as title with id could not be found
                validTitle = false;
            }
        }
        // Final check for if title is valid, everything is good
        if (validTitle) {
        	
        	List<CreditsRoll> credits_roll = show.getCreditsRolls();
        	String htmlCode = "";
        	
        	if(credits_roll != null) {
        		htmlCode = "<ul>";
        		for(CreditsRoll roll : credits_roll) {
            		htmlCode += "<li>" + roll.getCharacter() + " - <a href='../actor/" + roll.getPerson().getPersonId()
            				+ "'>" + roll.getPerson().getFullName() + "</a></li>";
            	}
            	htmlCode += "</ul>";
        	}else {
        		htmlCode += "<h6>No current information of the movie.</h6>";
        	}
        	
        	
            // Puts information of into model (commented out as getters not set up yet)
            // show contains credits roll, user reviews, production company
            model.put("show", show);
            model.put("cast", htmlCode);
            // Renders title page
            ctx.render(Template.TITLE_PAGE, model);
        } else {
            // Title not found, render error page
            ctx.render(Template.NOT_FOUND, model);
        }
    };
    
    //editing show details
    public static Handler serveTitleEditPage = ctx -> {
    	  Map<String, Object> model = ViewUtil.baseModel(ctx);
    	  
    	  model.put("show", show);
    	  model.put("user", user);
    	  
    	  ctx.render(Template.TITLE_EDIT, model);
    };
    
    //edits data of a show
    public static Handler serveEditFinal = ctx -> {
    	Map<String, Object> model = ViewUtil.baseModel(ctx);
    	
    	//All the 
    	String showTitle = ctx.formParam("showTitle");
    	String showGenre = ctx.formParam("showGenre");
    	String showLength = ctx.formParam("showLength");
    	String showYear = ctx.formParam("showYear");
    	String showDesc = ctx.formParam("showDesc");
    	
    	TitleDAO.editShow(show.getShowID(), showTitle, showGenre, showLength, showYear, showDesc);
    	
    	model.put("show", show);
    	
    	ctx.render(Template.TITLE_EDIT_FINAL, model);
    };

}