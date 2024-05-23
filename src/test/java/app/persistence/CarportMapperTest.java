package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CarportMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=test";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @BeforeAll
    static void setupClass() {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                // The test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.material_list");
                stmt.execute("DROP SEQUENCE IF EXISTS test.material_list_material_id_seq CASCADE;");

                // Create tables as copy of original public schema structure
                stmt.execute("CREATE TABLE test.material_list AS (SELECT * from public.material_list) WITH NO DATA");

                // Create sequences for auto generating ids for users and orders
                stmt.execute("CREATE SEQUENCE test.material_list_material_id_seq");
                stmt.execute("ALTER TABLE test.material_list ALTER COLUMN material_id SET DEFAULT nextval('test.material_list_material_id_seq')");
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
                stmt.execute("DELETE FROM test.material_list");

                stmt.execute("INSERT INTO test.material_list (material_id, name, description, price) " +
                        "VALUES  (1, 'Rem', 'Remme til carport', 35), (2, 'Stolpe', 'Stolper graves 90 cm ned', 74), (2, 'Spær', 'Spær på tværs', 35)");


                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.material_list_material_id_seq', COALESCE((SELECT MAX(material_id) + 1 FROM test.material_list), 1), false)");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCalculateFinalPrice()
    {

        // Create an instance of CarportController
        CarportMapper mapper = new CarportMapper();

        int rafterWoodQuantity = 11;
        int postQuantity = 4;
        int strapQuantity = 4;

        double width = 300;
        double length = 600;

        double expectedTotalPrice = 3234.28;

        mapper.calculateFinalPrice("test", width, length, rafterWoodQuantity, postQuantity, strapQuantity, connectionPool);

        double actualTotalPrice = mapper.getTotalPrice();

        assertEquals(expectedTotalPrice, actualTotalPrice, 0.01); // Delta value is 0.01

        // Explanation: The delta value (0.01) specifies the maximum allowable difference
        // between the expected and actual values.
    }
}
