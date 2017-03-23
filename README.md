## LazyJdbc

A tool to easily call pl/sql function from Java using a jdbc connection.

## Installation & Usage

Import as Maven project. Extend with your DAO class the LazyJdbcDAO, and implements in your bean the interface that you need (Take a look at the two VO example). Now you can call your PL/SQL:

			List<WorkExperience> workExperiences = yourDAO.genericFunction4arrayType(
					jdbcConnectionToYourDatasource, 
					"yourDataBaseUser.yourPackage.yourPLSQLFunction", 
					"yourDataBaseUser.yourCustomType", WorkExperience.class,
					yourInputForPLSQL, otherOptionalInput);
					
			List<Person> people = yourDAO.genericFunction4refCursor(
					jdbcConnectionToYourDatasource, 
					"yourDataBaseUser.yourPackage.yourPLSQLFunction", Person.class,
					yourInputForPLSQL, otherOptionalInput);
			
			//in this example no input parameter will be passed to PL/SQL:
			String s = yourDAO.genericFunction4Object(jdbcConnectionToYourDatasource, functionName, String.class, null);


## Contacts

* ivan-dot-dipaola[at[gmail-dot-com
* Stack Overflow profile: [![Imgur](http://stackoverflow.com/users/flair/1878854.png?theme=dark)](http://stackoverflow.com/users/1878854/accollativo)
* LinkedIn profile: https://www.linkedin.com/in/ivandipaola/