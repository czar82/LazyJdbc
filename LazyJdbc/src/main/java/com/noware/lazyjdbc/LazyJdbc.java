package com.noware.lazyjdbc;

import java.sql.Connection;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Abstract entry class to call plsql function and procedure from java. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
@Getter
@SuperBuilder
public abstract class LazyJdbc {
	/**
	 * Database Oracle jdbc connection.
	 */
	private final Connection connection;
	/**
	 * Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 */
	private final String functionName;
}