package app.controller;

import java.util.List;
import java.util.Map;

import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.EntryDAO;
import app.model.Entry;
import io.javalin.http.Handler;

public class ShowEntryController {
	
	private static Entry entry;
	
	public static Handler serveShowEntry = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		ctx.render(Template.SHOW_ENTRY, model);
	};
	
	public static Handler serveShowEntryTarget = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		String username = ctx.formParam("username");
		String showTitle = ctx.formParam("showTitle");
		String showGenre = ctx.formParam("showGenre");
		String showLength = ctx.formParam("showLength");
		String showType = ctx.formParam("showType");
		String showPCO = ctx.formParam("showPCO");
		String showYear = ctx.formParam("showYear");
		String showDesc = ctx.formParam("showDesc");
		
		String output = "";
		
		int entryId = 0;
		List<Entry> es = EntryDAO.getShowEntries();
		if (es != null) {
			entryId = es.get(es.size()-1).getEntryId() + 1;
		}else {
			 entryId = 1;
		}
		
		entry = new Entry(entryId,username, showTitle, showGenre, showLength, showType, showPCO, showYear, showDesc, 0);
			
		output += "<p>" +
							"Username: " + username + "<br><br>" +
							"Title: " + showTitle + "<br><br>" +
						    "Genre: " + showGenre + "<br><br>" +
						    "Length: " + showLength + "<br><br>" +
						    "Type: " + showType + "<br><br>" + 
						    "Production Company: " + showPCO + "<br><br>" + 
						    "Year: " + showYear + "<br><br>" +
						    "Description:<br>" + showDesc + 
						    "</p><br>" +
						    "<form action='/showEntryFinal' method='post' id='confirmEntry'>" +
						    "<button type='submit' 'name='submit' value='confirmEntry'>Confirm Entry</button>" +
						    "</form><br>" +
						    "<h6><a href='/showEntry'>or click here to return to form</a></h6>";
		
		model.put("Info", output);
		ctx.render(Template.SHOW_ENTRY_TARGET, model);
	};
	
	public static Handler serveShowEntryFinal = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		EntryDAO.addEntryToDatabase(entry);
		
		ctx.render(Template.SHOW_ENTRY_FINAL, model);
	};
}
