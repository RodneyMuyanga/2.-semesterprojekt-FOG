package app.persistence;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class OrderMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @BeforeAll
    static void setupClass() {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                // The test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.user");
                stmt.execute("DROP TABLE IF EXISTS test.order");
                stmt.execute("DROP SEQUENCE IF EXISTS test.user_user_id_seq CASCADE;");
                stmt.execute("DROP SEQUENCE IF EXISTS test.order_order_id_seq CASCADE;");

                // Create tables as copy of original public schema structure
                stmt.execute("CREATE TABLE test.user AS (SELECT * from public.user) WITH NO DATA");
                stmt.execute("CREATE TABLE test.order AS (SELECT * from public.order) WITH NO DATA");

                // Create sequences for auto generating ids for users and orders
                stmt.execute("CREATE SEQUENCE test.user_user_id_seq");
                stmt.execute("ALTER TABLE test.user ALTER COLUMN user_id SET DEFAULT nextval('test.user_user_id_seq')");
                stmt.execute("CREATE SEQUENCE test.order_order_id_seq");
                stmt.execute("ALTER TABLE test.order ALTER COLUMN order_id SET DEFAULT nextval('test.order_order_id_seq')");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                // Remove all rows from all tables
                stmt.execute("DELETE FROM test.order");
                stmt.execute("DELETE FROM test.user");

                stmt.execute("INSERT INTO test.user (\n" +
                        "\tuser_id, user_name, user_adress, user_phonenumber, user_zipcode, user_email, role, user_number, password) " +
                        "VALUES  (1, 'jon', 'jons vej 22', 12343526, 'customer'), (2, 'benny', '1234', 'admin')");

                stmt.execute("INSERT INTO test.order (order_id, width, length, approved, price, user_number) " +
                        "VALUES (1, 600, 780, false, 20000, 1), (2, 540, 700, true, 15000, 2), (3, 480, 600, false, 14000, 1)");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.order_order_id_seq', COALESCE((SELECT MAX(order_id) + 1 FROM test.order), 1), false)");
                stmt.execute("SELECT setval('test.user_user_id_seq', COALESCE((SELECT MAX(user_id) + 1 FROM test.user), 1), false)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllOrders() {
        // Test implementation for getAllOrders
    }

}
