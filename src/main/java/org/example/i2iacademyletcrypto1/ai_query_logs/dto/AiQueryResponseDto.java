package org.example.i2iacademyletcrypto1.ai_query_logs.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiQueryResponseDto {

    private UUID queryId;
    private String response;
    private LocalDateTime createdAt;
}
