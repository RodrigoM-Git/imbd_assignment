package app.controller;

import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import app.controller.paths.Template;
import app.controller.utils.ViewUtil;
import app.dao.AccountDAO;
import app.model.Account;
import io.javalin.http.Handler;

public class RegistrationController {

	private static Account user;

	public static Handler serveBeforeRegister = ctx -> {
		
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		ctx.render(Template.BEFORE_REGISTER, model);
		
	};

	public static Handler serveRegister = ctx -> {
		
		Map<String, Object> model = ViewUtil.baseModel(ctx);

		String accountType = ctx.formParam("accountType");
		model.put("accountType", accountType);
		ctx.render(Template.REGISTER, model);
	};

	public static Handler serveConfirm = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		String htmlCode = "";
		String submitType = ctx.formParam("submit");
		
		//Gets all the information that we need
		if(submitType.matches("submit")) {
			String accountType = ctx.formParam("accountType");
			String firstName = ctx.formParam("firstName");
			String lastName = ctx.formParam("lastName");
			String gender = ctx.formParam("gender");
			String birthYear = ctx.formParam("birthYear");
			String country = ctx.formParam("country");
			String zip = ctx.formParam("postcode");
			String username = ctx.formParam("username");
			String email = ctx.formParam("email");
			String password = ctx.formParam("password");
			String confirmPass = ctx.formParam("confirmPassword");
			String orgName = ctx.formParam("orgName");
			String orgPh = ctx.formParam("orgPhone");
			
			//checking if the user exists
			boolean userExists = AccountDAO.checkIfUserExists(username);
			
			//if does not exists
			if(!userExists) {
				//check for both input password
				if(password.matches(confirmPass)) {
					//hash the password
					String hashedPassword = BCrypt.hashpw(password, AccountDAO.SALT);
					//post it on the page
					htmlCode += "<h2>Registration Details:</h2>" +
								"<ul>" +
								"<li>Name: " + firstName + " " + lastName + "</li>" +
								"<li>Gender: " + gender + "</li>" +
								"<li>Year of Birth: " + birthYear + "</li>" +
								"<li>Country: " + country + "</li>" +
								"<li>Zip/Postal Code: " + zip + "</li>" +
								"<li>Email: " + email + "</li>";
					
					//Verifying which account type to match values and variables
					if(accountType.matches("User")) {
						htmlCode += "</ul><br><br>" +
									"<form action='/confirmRegistration' method='post' id='confirmRego'>" +
									"<input type='text' name='accountType' value='" + accountType +"' readonly hidden>" +
									"<input type='submit' name='submit' value='confirm'>" +
									"</form>";
						
						user = new Account(accountType, firstName, lastName, gender, birthYear, country,
								zip, username, email, hashedPassword);
					}else {
						
						htmlCode += "<li>Organisation Name: " + orgName + "</li>" +
									"<li>Organisation Number: " + orgPh + "</li>" +
									"</ul><br><br>" +
									"<form action='/confirmRegistration' method='post' id='confirmRego'>" +
									"<input type='text' name='accountType' value='" + accountType +"' readonly hidden>" +
									"<input type='submit' name='submit' value='confirm'>" +
									"</form>";
						
						user = new Account(accountType, firstName, lastName, gender, birthYear, country,
								zip, username, email, hashedPassword, orgName, orgPh);
					}
								
				}else {
					htmlCode += "<p>Registration Failed: Passwords did not match</p>";
				}
			}else {
				htmlCode += "<p>Registration Failed: Username already taken</p>";
			}
			
		}else if(submitType.matches("confirm"))
	{
		String accountType = ctx.formParam("accountType");

		if (accountType.matches("User")) {
			htmlCode += "<p>Registration Successful!</p>";

			AccountDAO.registerUser(user);
		} else {
			htmlCode += "<p>Registration submitted for account type: " + accountType + "</p>"
					+ "<p>Please wait for administrator review</p>";
			//Add account to database if confirmed
			AccountDAO.addEntryToDatabase(user);
		}
	}

	model.put("Code",htmlCode);ctx.render(Template.CONFIRM_REGISTER,model);

};}
