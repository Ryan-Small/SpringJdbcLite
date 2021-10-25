package tech.harbinger.spring.data.sqlite;


import org.springframework.data.jdbc.repository.config.DialectResolver;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;


public class SqliteDialectProvider implements DialectResolver.JdbcDialectProvider {
    @Override
    public Optional<Dialect> getDialect(JdbcOperations operations) {
        return Optional.ofNullable(operations.execute(this::createDialectIfConnectedToSqliteElseNull));
    }

    private Dialect createDialectIfConnectedToSqliteElseNull(Connection connection) throws SQLException {
        return is(connection).connectedToSqlite() ? SqliteDialect.INSTANCE : null;
    }

    private static ConnectionWrapper is(Connection connection) {
        return new ConnectionWrapper(connection);
    }

    private record ConnectionWrapper(Connection connection) {
        private static final String PRODUCT_NAME_SQLITE = "sqlite";

        private boolean connectedToSqlite() throws SQLException {
            return getProductName().contains(PRODUCT_NAME_SQLITE);
        }

        private String getProductName() throws SQLException {
            DatabaseMetaData metaData = connection.getMetaData();
            return metaData.getDatabaseProductName().toLowerCase(Locale.ENGLISH);
        }
    }
}
