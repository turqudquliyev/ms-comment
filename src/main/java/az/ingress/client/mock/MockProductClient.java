package az.ingress.client.mock;

import az.ingress.client.ProductClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class MockProductClient implements ProductClient {
    @Override
    public void checkStockAvailability(Long id) {

    }
}