package br.com.pedr0limpio.samples;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//TODO[#8]: Delete/remove this file and package samples
@QuarkusTest
public class HelloGraphQLResourceTest {

    @Test
    public void sayHelloTest() {
        HelloGraphQLResource resource = new HelloGraphQLResource();
        Greeting result = resource.sayHello("Renan", "Silva");
        assertNotNull(result);
        assertEquals("Hello Renan Silva", "Hello " + result.getName() + " " + result.getSurname());
    }
}
