package app.controller;

import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.TitleDAO;
import app.model.Show;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;
import java.util.Map;

public class TitleSearchController {
	public static Handler serveTitleSearch = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        
        String userInput = getQuerySearch(ctx);
        
    	List<Show> queryResult = TitleDAO.getTitleQueryResult(userInput);
    	
    	String htmlCode = createMovieResultHTML(queryResult, userInput);
        
    	model.put("Code", htmlCode);
    	ctx.render(Template.TITLE_SEARCH, model);
    	
    	
//         printResult(userInput);
       
    };
    
    //Take input from the user
    public static String getQuerySearch(Context ctx) {
    	return ctx.formParam("showTitleSearch");
    }
    //Printing out the result from the database
    //To be implemented further after task c and d are done
    public static void printResult(String userInput) {
    	List<Show> queryResult = TitleDAO.getTitleQueryResult(userInput);
    	String showDetails = "";
    	
    	for(Show s : queryResult) {
    		showDetails += s.getDetails() + "\n \n";
    	}
    	System.out.println(showDetails);
    }
    
    public static String createMovieResultHTML(List<Show> queryResult, String userInput) {
    	String htmlCode = "";
    	
    	if(!queryResult.isEmpty()) {
    		htmlCode += "<h3> RESULTS FOR " + userInput + ":</h3>";
    		htmlCode += "<ul>";
    		for(Show s : queryResult) {
        		htmlCode += "<li><a href='/title/" + s.getShowID() +"'>" + s.getShowTitle() + "</a></li>";
        	}
    		htmlCode += "</ul>";
    	}else {
    		htmlCode += "<p> There are no results for " + userInput + ".</p>";
    	}
    	
    	return htmlCode;
    }
}
