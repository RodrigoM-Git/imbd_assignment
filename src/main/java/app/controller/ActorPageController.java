package app.controller;

import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.ActorDAO;
import app.dao.TitleDAO;
import app.model.Person;
import app.model.Show;
import io.javalin.http.Handler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActorPageController {

    public static Handler serveActorPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        // Value that keeps track if input so far is valid
        boolean validActor = true;
        // Id of actor to be parsed from path parameter in try block
        int actor_id = -1;
        // Person (actor), to be set if id is valid
        Person person = null;
        // Shows that the person is involved with (if any)
        String htmlCode = "";
        try {
            // Parse id from path to an integer
            actor_id = Integer.parseInt(ctx.pathParam("actor_id"));
        } catch (NumberFormatException e){
            // Actor is invalid as id isn't an integer
            validActor = false;
        }
        // Checks if id was parsed as int correctly
        if (validActor){
            // Sees if person with id can be found (also checks to see if all fields are valid)
            person = ActorDAO.getActorFromId(actor_id);
            // Checks that person was set correctly
            if (person == null){
                // Sets validActor to false, as actor with id could not be found
                validActor = false;
            } else {
                // gets list of shows person is invloved with
                // shows = getShowsWithActor(actor_id);
            	Show show = null;
            	List<Show> showList = new ArrayList<>();
                List<String> shows = ActorDAO.getShowFromActor(person.getFullName());
                //Add found shows from database to the list
        		for (String s : shows) {
        			int show_id = Integer.parseInt(s);
        			show = TitleDAO.getTitleFromId(show_id);
        			showList.add(show);
        		}
        		//Create a html hyperlink to display on the web
        		htmlCode = createResultHTML(person,showList);
 
            }
        }
        // Final check for if actor is valid, everything is good
        if (validActor) {
        	//FOR birthday converting to string
        	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        	String birthDate = dateFormat.format(person.getBirthdate());
        	
            // Puts information of into model
        	model.put("birthdate", birthDate);
            model.put("person", person);
            model.put("shows", htmlCode);
            // Renders actor page
            ctx.render(Template.ACTOR_PAGE, model);
        } else {
            // Actor not found, render error page
            ctx.render(Template.NOT_FOUND, model);
        }
    };
    
	public static String createResultHTML(Person person, List<Show> showList) {
		String htmlCode = "";
		
		if (!showList.isEmpty()) {
			htmlCode += "<ul>";
				for (Show show : showList) {
					htmlCode += "<li><a href='../title/" + show.getShowID() + "'>"
							+ show.getShowTitle() + "</a></li>";
				}
			htmlCode += "</ul>";

		}else {
			htmlCode += "<p> There are no movies currently portrayed.</p>";
		}

		return htmlCode;
	}

}