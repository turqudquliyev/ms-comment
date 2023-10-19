package az.ingress.client.decoder;

import az.ingress.exception.CustomFeignException;
import com.fasterxml.jackson.databind.JsonNode;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static az.ingress.mapper.ObjectMapperFactory.OBJECT_MAPPER;
import static az.ingress.model.constant.JsonNodeFieldName.CODE;
import static az.ingress.model.enums.ExceptionMessage.CLIENT_EXCEPTION;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        var errorMessage = CLIENT_EXCEPTION.toString();
        var status = response.status();
        JsonNode jsonNode;
        try (var body = response.body().asInputStream()) {
            jsonNode = OBJECT_MAPPER.getInstance().readValue(body, JsonNode.class);
        } catch (Exception e) {
            throw new CustomFeignException(errorMessage, status);
        }
        if (jsonNode.has(CODE)) errorMessage = jsonNode.get(CODE).asText();
        log.error("ActionLog.decode.error Message: {}, Method: {} ", errorMessage, methodKey);
        throw new CustomFeignException(errorMessage, status);
    }
}