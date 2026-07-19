package org.example.i2iacademyletcrypto1.ai_query_logs.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiQueryRequestDto {

    @NotBlank(message = "Query cannot be empty")
    @Size(
            max = 1000,
            message = "Query cannot exceed 1000 characters"
    )
    private String query;
}
