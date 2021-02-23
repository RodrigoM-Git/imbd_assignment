package app.controller;

import java.util.List;
import java.util.Map;

import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.AccountDAO;
import io.javalin.http.Handler;
import app.model.Account;

public class ReviewRegistrationController {
	
	private static Account user;

	public static Handler serveReviewRegistrationList = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);

		List<Account> entries = AccountDAO.getRegistrationEntries();
		String htmlCode = "";
		
		//get all the registration 
		if (entries != null) {
			htmlCode = "<h3>REGISTRATION ENTRIES:</h3>" + "<ul>";

			for (Account a : entries) {
				htmlCode += "<li><a href='/registration/" + a.getUsername() + "'>" + a.getType() + " - "
						+ a.getUsername() + "</a></li>";
			}
			htmlCode += "</ul>";
		}else {
			htmlCode = "<p>No registration entries have been submitted</p>";
		}
		
		model.put("Code", htmlCode);
		ctx.render(Template.REGISTRATION_LIST, model);
		

	};
	
	public static Handler serveRegistration = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		String usernameURL = ctx.pathParam("username");
		user = AccountDAO.getEntryByUsername(usernameURL);
		
		String htmlCode = "";
		if(user != null) {
			String userOrg = "";
			
			//check for if its a pco account
			if(user.getType().matches("PCO")) {
				String prodCo = AccountDAO.getProdCompany(user.getOrg());
				
				//if company is in database
				if(prodCo != null) {
					//add prod co description to the webpage
					userOrg = user.getOrg() + " -- > (References in database: " + prodCo + ")";
				}else {
					userOrg = user.getOrg() + " -- > (References in database: None)"; 
 				}
			}
			
			userOrg = user.getOrg();
			//shows the details of the account on the web
			htmlCode = "<h3>Registration for Account Type: " + user.getType() + "</h3><p>" +
					   "Username: " + user.getUsername() + "<br><br>" +
					   "Email: " + user.getEmail() + "<br><br>" +
					   "Name: " + user.getFirstName() + " " + user.getLastName() + "<br><br>" +
					   "Gender: " + user.getGender() + "<br><br>" +
					   "Year of Birth: " + user.getBirthYear() + "<br><br>" +
					   "Country: " + user.getCountry() + "<br><br>" +
					   "Zip/Postal Code: " + user.getZip() + "</p><br><br><br>" +
					   "<h4>Organisation Details</h4><p>" +
					   "Organisation Name: " + userOrg + "<br><br>" +
					   "Phone Number: " + user.getNum() + "</p>" +
					   "<form action='/registrationFinal' method='post' id='decideRegistration'>" + 
					   "<button type='submit' name='submit' value='accept'>Accept Registration</button>" +
					   "<button type='submit' name='submit' value='delete'>Delete Entry</button>" +
					   "</form>";
		}else {
			htmlCode = "<p>Invalid Entry!</p>";
		}
		
		model.put("Info", htmlCode);
		ctx.render(Template.REGISTRATION, model);
	};
	
	public static Handler serveRegistrationDecision = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		String decision = ctx.formParam("submit");
		String htmlCode = "";
		
		//if admin accepts the form
		if(decision.matches("accept")) {
			AccountDAO.registerUser(user);
			AccountDAO.deleteRegistration(user.getUsername());
			htmlCode = "<p>Successfully registered " + user.getType() + " account for: " + user.getUsername() + "!";
		
		//if rejected
		}else if(decision.matches("delete")) {
			AccountDAO.deleteRegistration(user.getUsername());
			htmlCode = "<p>Registration for a " + user.getType() + " account for: " +user.getUsername() + " denied." +
					   "<br><br> Rejected entry has been deleted </p>";
		}
		
		model.put("decision", htmlCode);
		ctx.render(Template.REGISTRATION_DECISION, model);
	};
}
