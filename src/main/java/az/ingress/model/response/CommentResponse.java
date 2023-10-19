package az.ingress.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = PRIVATE)
public class CommentResponse {
    Long id;
    Long userId;
    Long productId;
    String message;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime createdAt;
}