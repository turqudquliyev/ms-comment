package az.ingress.client.mock;

import az.ingress.client.ProductClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class MockProductClient implements ProductClient {
    @Override
    public void getProductIfExist(Long id) {
        // TODO: this is mock implementation
        System.out.println("Assume everything is ok");
    }
}