## LazyJdbc

A tool to easily call pl/sql function from Java using a jdbc connection.

## Installation & Usage

Import as Maven project. Extend with your DAO class the LazyJdbcDAO, and implements in your bean the interface that you need (Take a look at the two VO example). Now you can call your PL/SQL:

	List<WorkExperience> workExperiences = yourDAO.genericFunction4arrayType(
			jdbcConnectionToYourDatasource, 
			"yourSchema.yourPackage.yourPLSQLFunction", 
			"yourSchema.yourCustomType", WorkExperience.class,
			yourInputForPLSQL, otherOptionalInput);
			
	List<Person> people = yourDAO.genericFunction4refCursor(
			jdbcConnectionToYourDatasource, 
			"yourSchema.yourPackage.yourPLSQLFunction", Person.class,
			yourInputForPLSQL, otherOptionalInput);

	//in this example no input parameter will be passed to PL/SQL:
	String s = yourDAO.genericFunction4Object("yourSchema.yourPackage.yourPLSQLFunction", functionName, String.class, null);

	//in this example this procedure have one numeric input, an Oracle.ARRAY and a text description as output and a date as input.
	//you must respect the order of your plsql parameter
	ListItem<WorkExperience> listItem = new ListItem<WorkExperience>(WorkExperience.class, "yourSchema.yourCustomType");
	SingleItem<String> description = new SingleItem<String>(String.class);
	yourDAO.genericProcedure(conn, "yourSchema.yourPackage.yourPLSQLProcedure", 
			new InPlsqlParameter<Integer>(1234),
			new OutPlsqlParameter(listItem),
			new OutPlsqlParameter(description),
			new InPlsqlParameter<java.sql.Date>(today)
			);
	

## Contacts

* ivan-dot-dipaola[at[gmail-dot-com
* Stack Overflow profile: [![Imgur](http://stackoverflow.com/users/flair/1878854.png?theme=dark)](http://stackoverflow.com/users/1878854/accollativo)
* LinkedIn profile: https://www.linkedin.com/in/ivandipaola/