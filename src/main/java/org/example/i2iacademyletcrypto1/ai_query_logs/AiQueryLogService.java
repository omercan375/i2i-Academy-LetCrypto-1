package org.example.i2iacademyletcrypto1.ai_query_logs;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.example.i2iacademyletcrypto1.zcommon.exception.SaveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiQueryLogService {

    private final AiQueryLogsRepo aiQueryLogsRepo;

    @Transactional
    public AiQueryLogsTable save(
            UsersTable user,
            String query,
            String response
    ) {
        AiQueryLogsTable log = AiQueryLogsTable.builder()
                .user(user)
                .query(query)
                .response(response)
                .build();

        try {
            return aiQueryLogsRepo.save(log);
        } catch (Exception exception) {
            throw new SaveException(
                    "AI query log could not be saved"
            );
        }
    }
}
