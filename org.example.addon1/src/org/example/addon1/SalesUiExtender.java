package org.example.addon1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.example.server.api.IAddonUiExtender;
import org.example.server.api.IDatasourceProvider;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.DefaultSQLGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@org.osgi.service.component.annotations.Component(property = { "type=sales" })
public class SalesUiExtender implements IAddonUiExtender {

	private String SQL_DROP_TABLE = "drop table CUSTOMER";
	private String SQL_CREATE_TABLE = "create table CUSTOMER (CustomerID int, Customer varchar(255), Salary varchar(255), primary key (CustomerID))";
	private boolean dbCreated;
	private Table table;
	private IDatasourceProvider provider;

	@Override
	public String getCaption() {
		return "Sales";
	}

	@Override
	public Component createComponent() {

		// create a demo database
		executeCreateSQL();

		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();

		table = new Table();
		table.setSelectable(true);
		vl.addComponent(table);
		vl.setExpandRatio(table, 1.0f);

		try {
			DataSource ds = provider.createDatasource();
			Connection con = ds.getConnection();
			SQLContainer container = new SQLContainer(
					new TableQuery("customer", new SimpleConnectionPool(con), new DerbySQLGenerator()));
			table.setContainerDataSource(container);
		} catch (SQLException e1) {
		}

		return vl;
	}

	@SuppressWarnings("serial")
	public static class DerbySQLGenerator extends DefaultSQLGenerator {

		public DerbySQLGenerator() {
		}

		public DerbySQLGenerator(String quoteStart, String quoteEnd) {
			super(quoteStart, quoteEnd);
		}

		protected StringBuffer generateLimits(StringBuffer sb, int offset, int pagelength) {
			sb.append(" OFFSET ").append(offset).append(" ROWS").append(" FETCH NEXT ").append(pagelength)
					.append(" ROWS ONLY");
			return sb;
		}
	}

	/**
	 * Creates a demo table.
	 * 
	 * @return
	 */
	private Object executeCreateSQL() {
		if (!dbCreated) {
			try {
				DataSource ds = provider.createDatasource();
				Connection con = ds.getConnection();

				try {
					con.prepareStatement(SQL_DROP_TABLE, Statement.NO_GENERATED_KEYS).execute();
				} catch (Exception e) {
					// for demo it is fine to catch "already existing exception"
					e.printStackTrace();
				}
				try {
					con.prepareStatement(SQL_CREATE_TABLE, Statement.NO_GENERATED_KEYS).execute();
				} catch (Exception e) {
					// for demo it is fine to catch "already existing exception"
					e.printStackTrace();
				}
				dbCreated = true;

				con.createStatement().execute(
						"insert into CUSTOMER (CustomerID, Customer, Salary) VALUES(1, '12348', '10.234,78') ");
				con.createStatement().execute(
						"insert into CUSTOMER (CustomerID, Customer, Salary) VALUES(2, '66778', '11.234,78') ");
				con.createStatement().execute(
						"insert into CUSTOMER (CustomerID, Customer, Salary) VALUES(3, '36987', '12.234,78') ");
				con.createStatement()
						.execute("insert into CUSTOMER (CustomerID, Customer, Salary) VALUES(4, '4711', '13.234,78') ");
				con.createStatement()
						.execute("insert into CUSTOMER (CustomerID, Customer, Salary) VALUES(5, '0815', '14.234,78') ");

				con.close();
			} catch (SQLException e) {
				Notification.show("SQL Exception", Type.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}

		return null;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	protected void bindDatasourceProvider(IDatasourceProvider provider) {
		this.provider = provider;
	}

	public static class SimpleConnectionPool implements JDBCConnectionPool {

		private transient Connection connection;

		public SimpleConnectionPool(Connection connection) throws SQLException {
			this.connection = connection;
		}

		@Override
		public synchronized Connection reserveConnection() throws SQLException {
			return connection;
		}

		@Override
		public synchronized void releaseConnection(Connection conn) {
		}

		@Override
		public void destroy() {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
