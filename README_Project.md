## Basic Information

This is the base code for your project. This means that this has a basic wireframe only, and that you'll need to complete it and change it a little bit. This file will document some basic stuff as well.

**To Run:** Simply open the Maven project in your IDE. Once Maven imported all dependencies, run the `Main` class. The system is available in the URL: _https://localhost:7000/_.
- You need Java 1.8 or above!



### General
The project is structured with Maven. **You are required to use Maven to continue structuring the project**. 
- If you need extra libraries, you can find them in [https://mvnrepository.com/](https://mvnrepository.com/) 
- You need to copy the text provided by Maven Repository and paste it between the tags `<dependencies></dependencies>` in the `pom.xml` file. This was explained in the Lectorial and Tutorials.
- You should _not_ touch the rest of the `pom.xml`.


This project has a MySQL database. There is a page in Canvas with information in how to set up MySQL in your computer. Please check it out, and remember the instructions given in the tutorial.

- You can find the dump at `resources/databasedump.sql`. You should delete this file when you are pushing and working in the project, but keep a copy.
- The database has some mock-up data loaded, but you need to complete it (for example, images for all people and movies), adding more people, movies and accounts.
- The images are stored in `resources\img\people` or `resources\img\shows` with the id of the person/show as a name.
- Existing user in the database is "caramel6" with password "password".
- _You may need to alter this a little bit when you add the account roles, though. Keep this in mind_.
- You will have to add more DAOs to keep up with all the database tables.




### View

According to Javalin, the framework we are using, the _view_ is stored in the folder `main/resources/velocity`. There are the following considerations:

- Everytime you want to create a new URL modifier (i.e. `/login`), you create a new folder.
- The files inside those folders will have the body _only_. Check `login/login.vm` as an example.
- Once you added it here, you need to add the corresponding information to the classes `paths\Template` and `paths\Web`. Look the current examples.
- Velocity has some tags that work as conditionals `if-elseif-else` to show different parts of the UI depending in some constant. Check `login.vm` and `layout.vm` to get some examples. 



### Controller

Controllers are in `main\java\app\controller`. 

- There should be one controller per view (at least!). 
- Use `LoginController`, `IndexController` and `UserController` as base examples.
- Remember that if you add a view, just "seeing" is a "get" in the `Main` class, and sending a form is a "post", that will be handled by a Controller. This is discussed during the tutorial. Check the `Main` class to understand what is going on.


### Model

Roughly, the model needs to be similar to what you have in the database (if you use it as DTO as well), but it is more related to what you are showing in the View. Remember the MVC.

Currently, there are incomplete methods, things are missing, and other elements are different from the class diagram. Lectorial #5 will explain more of this, and also help you.

Remember that nothing is fixed -everything evolves. That is the essence of agile!



