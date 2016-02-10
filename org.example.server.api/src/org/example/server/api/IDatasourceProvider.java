package org.example.server.api;

import java.sql.SQLException;

import javax.sql.DataSource;

public interface IDatasourceProvider {

	/**
	 * Returns a new instance of a datasource.
	 */
	DataSource createDatasource() throws SQLException ;

}