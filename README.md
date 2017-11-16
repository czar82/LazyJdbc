## LazyJdbc

A tool to easily call pl/sql functions and procedures from Java, using a jdbc connection.

## Installation & Usage

Import as Maven project. Use LazyJdbcDAO or extend it with your DAO class, and implements in your bean the interface that you need (Have a look to VO example). 
Done? Now you can call your PL/SQL:

	//This method will return a list from a pl/sql function that returns an OracleTypes.ARRAY.
	List<WorkExperience> workExperiences = yourDAO.genericFunction4arrayType(
			jdbcConnectionToYourDatasource, 
			"yourSchema.yourPackage.yourPLSQLFunction", 
			"yourSchema.yourCustomType", WorkExperience.class,
			yourInputForPLSQL, otherOptionalInput);

	//This method will return an object (that contains the data structure that you choose), from a 
	//pl/sql function that returns an OracleTypes.ARRAY.
	//In this example we use 3 real input: "PlanetEarth!", "100", 15:
	WorldMap worldMap = yourDAO.genericFunction4multiLevelObject(
			jdbcConnectionToYourDatasource, 
			"yourSchema.yourPackage.yourPLSQLFunction", 
			"yourSchema.yourCustomType", WorldMap.class,
			"PlanetEarth!", "100", 15);
			
	//This method will return a list from a pl/sql function that returns an OracleTypes.CURSOR.
	List<Person> people = yourDAO.genericFunction4refCursor(
			jdbcConnectionToYourDatasource, 
			"yourSchema.yourPackage.yourPLSQLFunction", Person.class,
			yourInputForPLSQL, otherOptionalInput);

	//This method will return an Object from a pl/sql function.
	//in this example no input parameter will be passed to PL/SQL:
	String s = yourDAO.genericFunction4Object("yourSchema.yourPackage.yourPLSQLFunction", functionName, 
			String.class, null);

	//This method will call a pl/sql procedure.
	//in this example the procedure have a numeric input, an Oracle.ARRAY and 
	//a text description as output, and a date as input. Respectively in this order.
	//You must respect the order of your plsql parameters.
	ListItem<WorkExperience> listItem = 
			new ListItem<WorkExperience>(WorkExperience.class, "yourSchema.yourCustomType");
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