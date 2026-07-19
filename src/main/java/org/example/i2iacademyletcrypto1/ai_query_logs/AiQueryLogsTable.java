package org.example.i2iacademyletcrypto1.ai_query_logs;

import jakarta.persistence.*;
import lombok.*;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_query_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiQueryLogsTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersTable user;

    @Column(name = "query", columnDefinition = "TEXT")
    private String query;

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}