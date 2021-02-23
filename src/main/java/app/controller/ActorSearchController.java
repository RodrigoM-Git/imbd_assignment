package app.controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;
import java.util.Map;
import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.ActorDAO;
import app.model.Person;

public class ActorSearchController {

	public static Handler serveActorSearch = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);

		String userInput = getQuerySearch(ctx);

		
		// Index for person in people should be same as index for show in showsForPerson
		// Eg index 0 in people should have its shows in index 0 of showsForPerson
		List<Person> people = ActorDAO.getActorQueryResult(userInput);
	
		String htmlCode = createResultHTML(people, userInput);

		model.put("Code", htmlCode);
		ctx.render(Template.TITLE_SEARCH, model);

		
	};

	public static String getQuerySearch(Context ctx) {
		return ctx.formParam("showActorSearch");
	}

	public static void printResult(String userInput) {
		List<Person> queryResult = ActorDAO.getActorQueryResult(userInput);
		String showDetails = "";

		for (Person p : queryResult) {
			showDetails += p.getDetails() + "\n \n";
		}
		System.out.println(showDetails);
	}

	public static String createResultHTML(List<Person> people, String userInput) {
		String htmlCode = "";

		if (!people.isEmpty()) {
			htmlCode += "<h3> RESULTS FOR " + userInput + ":</h3>";
			htmlCode += "<ul>";
			
			for(Person p : people) {
				htmlCode += "<li><a href='actor/" + p.getPersonId() + "'>" + p.getFullName() +" </a></li>";
			}
			htmlCode += "</ul>";

		} else {
			htmlCode += "<p> There are no results for " + userInput + ".</p>";
		}

		return htmlCode;
	}
}
