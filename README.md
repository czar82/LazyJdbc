## LazyJdbc

A tool to easily call pl/sql functions and procedures from Java, using a jdbc connection.

## Installation & Usage

Import as Maven project. You can use JitPack: https://jitpack.io/#czar82/LazyJdbc/java1.8-SNAPSHOT 
Use LazyJdbc subclasses, and implements in your bean the interface that you need (Have a look to VO example). 
Done? Now you can call your PL/SQL:

	//This method will return a list from a pl/sql function that returns an OracleTypes.ARRAY.
	LazyFunction4OracleType func = LazyFunction4OracleType.builder()
			.connection(jdbcConnectionToYourDatasource)
			.functionName("yourSchema.yourPackage.yourPLSQLFunction")
			.oracleTypeName("yourSchema.yourCustomType")
			.queryParam(yourInputValueForPLSQL).build();
	List<WorkExperience> workExperiences = func.genericFunction4arrayType(WorkExperience.class);

	//This method will return an object (that contains the data structure that you choose), from a 
	//pl/sql function that returns an OracleTypes.ARRAY.
	//In this example we use 2 strings and a number as input: "PlanetEarth!", "100", 15:
	LazyFunction4OracleType func = LazyFunction4OracleType.builder()
			.connection(jdbcConnectionToYourDatasource)
			.functionName("yourSchema.yourPackage.yourPLSQLFunction")
			.oracleTypeName("yourSchema.yourCustomType")
			.queryParam("PlanetEarth!").queryParam("100").queryParam(15).build();
	WorldMap worldMap = func.genericFunction4multiLevelObject(WorldMap.class);
			
	//This method will return a list from a pl/sql function that returns an OracleTypes.CURSOR.
	LazyFunction func = LazyFunction.builder()
			.connection(jdbcConnectionToYourDatasource)
			.functionName("yourSchema.yourPackage.yourPLSQLFunction")
			.queryParam(yourInputForPLSQL).build();
	List<Person> people = func.genericFunction4refCursor(Person.class);

	//This method will return an Object from a pl/sql function.
	//in this example the PL/SQL function haven't any parameter, so we'll not use .queryParam:
	LazyFunction func = LazyFunction.builder()
			.connection(jdbcConnectionToYourDatasource)
			.functionName("yourSchema.yourPackage.yourPLSQLFunction")
			.build();
	String s = func.genericFunction4Object(String.class);

	//This method will call a pl/sql procedure.
	//in this example the procedure have a numeric input, an Oracle.ARRAY and 
	//a text description as output, and a date as input. Respectively in this order.
	//You must respect the order of your plsql parameters.
	List<Foo> listItem = new ArrayList<>();
	LazyProcedure proc = LazyProcedure.builder()
			.connection(jdbcConnectionToYourDatasource)
			.functionName("yourSchema.yourPackage.yourPLSQLProcedure")
			.queryParam(new InPlsqlParameter<Integer>(1234))
			.queryParam(new OutPlsqlParameter(listItem))
			.queryParam(new OutPlsqlParameter(description))
			.queryParam(new InPlsqlParameter<java.sql.Date>(today))
			.build();
	proc.genericProcedure();
	

## Contacts

* ivan-dot-dipaola[at[gmail-dot-com
* Stack Overflow profile: [![Imgur](http://stackoverflow.com/users/flair/1878854.png?theme=dark)](http://stackoverflow.com/users/1878854/accollativo)
* LinkedIn profile: https://www.linkedin.com/in/ivandipaola/
