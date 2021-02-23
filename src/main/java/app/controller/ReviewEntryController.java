package app.controller;

import java.util.List;
import java.util.Map;

import app.controller.paths.*;
import app.controller.utils.*;
import app.dao.*;
import io.javalin.http.Handler;
import app.model.*;

public class ReviewEntryController {
	private static Entry entry;

	public static Handler serveReviewEntryList = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);

		List<Entry> entries = EntryDAO.getShowEntries();

		String htmlCode = createEntriesListHTML(entries);

		model.put("Code", htmlCode);
		ctx.render(Template.REVIEW_ENTRY_LIST, model);

	};

	public static Handler serveEntryPage = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);

		String entryId = ctx.pathParam("entryId");

		entry = EntryDAO.getSpecificEntry(entryId);

		String htmlCode = createHTMLCode(entry);

		model.put("Info", htmlCode);
		ctx.render(Template.ENTRY, model);
	};

	public static Handler serveEntryDecidedPage = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);

		String decision = ctx.formParam("submit");
		String htmlCode = "";

		if (decision.matches("approveEntry")) {
			// add entry to show table and remove from entry table
			// New entry's show_id
			int showId = EntryDAO.getNumberOfShows() + 1;
			boolean isMovie = false;
			boolean isSeries = false;
			if (entry.getShowType().toLowerCase().matches("movie")) {
				isMovie = true;
			} else if (entry.getShowType().toLowerCase().matches("series")) {
				isSeries = true;
			}
			// get procId from database
			int procIdForShow = EntryDAO.getProcId(entry);
			// Create new Show from entry
			Show newShow = new Show(showId, entry.getShowTitle(), Double.parseDouble(entry.getShowLength()), isMovie,
					isSeries, entry.getGenre(), procIdForShow, Integer.parseInt(entry.getYearOfProduction()),
					entry.getDescription());
			TitleDAO.insertShow(newShow);
			EntryDAO.deleteEntry(entry.getUsername(), entry.getShowTitle());
			htmlCode = "<h3>Entry has been approved!</h3><br><br>";
			htmlCode += "<p>Entry has been added to imbd as a show</p>";
		} else if (decision.matches("rejectEntry")) {
			// make entry rejected = true
			EntryDAO.RejectEntry(entry);
			// Delete duplicated entry
			EntryDAO.deleteDuplicate(entry);
			htmlCode = "<h3>Entry has been rejected.</h3><br><br>";
			htmlCode += "<p>Entry has been marked as rejected</p>";
		}else {
			htmlCode = "<p>An unexpected error has occured</p>";
		}

		model.put("decision", htmlCode);
		ctx.render(Template.ENTRY_DECIDED, model);
	};

	private static String createEntriesListHTML(List<Entry> entries) {
		String htmlCode = "";

		if (entries != null) {
			htmlCode += "<h3>LIST OF ENTRIES:</h3>";
			htmlCode += "<ul>";
			for (Entry e : entries) {
				if (e.isRejected() == 0)
					htmlCode += "<li><a href='/entry/" + e.getEntryId() + "'>" + e.getShowTitle() + " - Entry by "
							+ e.getUsername() + "</a></li>";
			}
			htmlCode += "</ul>";

		} else {
			htmlCode += "<p>No entries have been submitted</p>";
		}

		return htmlCode;
	}

	public static String createHTMLCode(Entry entry) {
		String htmlCode = "";

		if (entry != null) {
			htmlCode += "<h3> Entry by " + entry.getUsername() + ":</h3>";
			htmlCode += "<p>" + "Title: " + entry.getShowTitle() + "<br><br>" + "Genre: " + entry.getGenre()
					+ "<br><br>" + "Length: " + entry.getShowLength() + "<br><br>" + "Type: " + entry.getShowType()
					+ "<br><br>" + "Production Company: " + entry.getProdCompany() + "<br><br>" + "Year: "
					+ entry.getYearOfProduction() + "<br><br>" + "Description:<br>" + entry.getDescription()
					+ "</p><br>" + "<form action='/entryDecided' method='post' id='decideEntry'>"
					+ "<button type='submit' name='submit' value='approveEntry'>Approve Entry</button>"
					+ "<button type='submit' name='submit' value='rejectEntry'>Reject Entry</button>" + "</form>";

		} else {
			htmlCode += "<p>Invalid Entry</p>";
		}

		return htmlCode;
	}
}
