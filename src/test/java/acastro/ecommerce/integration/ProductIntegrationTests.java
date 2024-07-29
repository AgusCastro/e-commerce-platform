package acastro.ecommerce.integration;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import acastro.ecommerce.dto.ProductRequestDto;

@IntegrationTest
public class ProductIntegrationTests extends BaseIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void given2Products_whenGetProducts_thenCheckData() {
        ProductRequestDto product1 = new ProductRequestDto("Notebook", "Notebook description here", 1200.20, 50);
        // given
        ResponseEntity<JsonNode> response1 = restTemplate.postForEntity("/products", product1, JsonNode.class);

        JsonNode body1 = assertResponse(response1);

        Long product1Id = assertProductRequestDto(body1, product1, false);

        ProductRequestDto product2 = new ProductRequestDto("Mouse", "Mouse description here", 20.50, 100);

        ResponseEntity<JsonNode> response2 = restTemplate.postForEntity("/products", product2, JsonNode.class);

        JsonNode body2 = assertResponse(response2);

        Long product2Id = assertProductRequestDto(body2, product2, false);

        // when

        ResponseEntity<JsonNode> getAllResponse = restTemplate.getForEntity("/products", JsonNode.class);

        Assertions.assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, getAllResponse.getHeaders().getContentType());
        Assertions.assertNotNull(getAllResponse.getBody());

        // then

        Assertions.assertTrue(getAllResponse.getBody().isArray());
        Assertions.assertEquals(2, getAllResponse.getBody().size());

        JsonNode product1Node = null;
        JsonNode product2Node = null;

        for (JsonNode productNode : getAllResponse.getBody()) {
            if (productNode.get("id").asLong() == product1Id) {
                product1Node = productNode;
            } else if (productNode.get("id").asLong() == product2Id) {
                product2Node = productNode;
            }
        }

        Assertions.assertNotNull(product1Node);
        Assertions.assertNotNull(product2Node);

        assertProductRequestDto(product1Node, product1, false);
        assertProductRequestDto(product2Node, product2, false);
    }

    private JsonNode assertResponse(ResponseEntity<JsonNode> response) {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Assertions.assertNotNull(response.getBody());
        return response.getBody();
    }

    private static Long assertProductRequestDto(JsonNode responseBody, ProductRequestDto product, boolean discontinued) {

        Assertions.assertNotNull(responseBody);
        Assertions.assertTrue(responseBody.has("id"));
        Assertions.assertEquals(product.name(), responseBody.get("name").asText());
        Assertions.assertEquals(product.description(), responseBody.get("description").asText());
        Assertions.assertEquals(product.price(), responseBody.get("price").asDouble());
        Assertions.assertEquals(product.stock(), responseBody.get("stock").asInt());
        Assertions.assertEquals(discontinued, responseBody.get("discontinued").asBoolean());

        return responseBody.get("id").asLong();
    }

}
