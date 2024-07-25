package acastro.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//TODO: Change the connection to Postgresql when implement Testcontainers
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ECommerceApplicationTests {

	@Test
	void contextLoads() {
	}

}
