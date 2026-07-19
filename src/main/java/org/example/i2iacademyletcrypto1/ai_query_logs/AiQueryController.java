package org.example.i2iacademyletcrypto1.ai_query_logs;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.ai_query_logs.dto.AiQueryRequestDto;
import org.example.i2iacademyletcrypto1.ai_query_logs.dto.AiQueryResponseDto;
import org.example.i2iacademyletcrypto1.users.UsersService;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiQueryController {

    private final AiQueryService aiQueryService;
    private final UsersService usersService;

    @PostMapping("/query")
    public ResponseEntity<AiQueryResponseDto> query(@RequestHeader("Authorization") String token, @Valid @RequestBody AiQueryRequestDto request) {
        UsersTable user =
                usersService.findUserById(token);

        AiQueryResponseDto response =
                aiQueryService.query(user, request);

        return ResponseEntity.ok(response);
    }
}
