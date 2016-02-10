package org.example.server.vaadin.jdbc;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.example.server.api.IDatasourceProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jdbc.DataSourceFactory;

@Component()
public class DatasourceProvider implements IDatasourceProvider {

	private DataSourceFactory factory;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "unbindDataSourceFactory", target = "(osgi.jdbc.driver.class=org.apache.derby.jdbc.EmbeddedDriver)")
	protected void bindDataSourceFactory(DataSourceFactory factory) {
		this.factory = factory;
	}

	protected void unbindDataSourceFactory(DataSourceFactory factory) {
		this.factory = null;
	}

	@Override
	public DataSource createDatasource() throws SQLException {
		Properties props = new Properties();
		// props.put(JNDIConstants, ");
		props.put(DataSourceFactory.JDBC_URL, "jdbc:derby:testDB;create=true");
		return factory.createDataSource(props);
	}
}
