package app.persistence;

import static org.junit.Assert.assertEquals;
import app.controllers.CarportController;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;
import org.junit.Test;

public class UtilitiesTest {


        private static final String USER = "postgres";
        private static final String PASSWORD = "postgres";
        private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
        private static final String DB = "fog";

        private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


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

            mapper.calculateFinalPrice(width, length, rafterWoodQuantity, postQuantity, strapQuantity, connectionPool);

            double actualTotalPrice = mapper.getTotalPrice();

            assertEquals(expectedTotalPrice, actualTotalPrice, 0.01); // Delta value is 0.01

            // Explanation: The delta value (0.01) specifies the maximum allowable difference
            // between the expected and actual values.
        }
    }

