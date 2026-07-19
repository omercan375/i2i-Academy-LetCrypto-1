package org.example.i2iacademyletcrypto1.ai_query_logs;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AiQueryLogsRepo extends CrudRepository<AiQueryLogsTable, UUID> {

    @Query(value = """
            SELECT *
            FROM ai_query_logs
            WHERE user_id = :userId
            ORDER BY created_at DESC
            LIMIT 5
            """, nativeQuery = true)
    List<AiQueryLogsTable> findLast5ByUserId(@Param("userId") UUID userId);
}