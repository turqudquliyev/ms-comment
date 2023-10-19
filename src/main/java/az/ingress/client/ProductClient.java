package az.ingress.client;

import az.ingress.client.decoder.CustomErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Profile("!local")
@FeignClient(name = "ms-product",
        url = "${client.urls.ms-product}",
        configuration = CustomErrorDecoder.class)
public interface ProductClient {
    @GetMapping("/{id}")
    void getProductIfExist(@PathVariable Long id);
}