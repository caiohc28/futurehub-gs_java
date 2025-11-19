package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*; // Importações do JPA
import lombok.*;
// import org.springframework.data.annotation.Id; // Removido
// import org.springframework.data.mongodb.core.mapping.Document; // Removido

import java.time.LocalDateTime;

@Entity // Anotação JPA para mapear para uma tabela
@Table(name = "ideias") // Define o nome da tabela no Azure SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ideia {

    @Id // Anotação JPA para Chave Primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incremento
    private Long id; // ID numérico para o banco de dados SQL

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false) // Usa TEXT para descrições longas
    private String descricao;

    // --- Chaves Estrangeiras (Simples) ---
    // Usamos Long assumindo que Usuario migrou para Long ID
    @Column(name = "id_usuario", nullable = false)
    private Long autorId;

    // Usamos Long assumindo que Missao migrou para Long ID
    @Column(name = "id_missao", nullable = false)
    private Long missaoId;

    // --- Campos de Cálculo/Estatísticas ---
    @Builder.Default
    @Column(name = "media_notas")
    private Double mediaNotas = 0.0;

    @Builder.Default
    @Column(name = "total_avaliacoes")
    private Integer totalAvaliacoes = 0;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // NOTA: Para campos de data/hora, considere usar anotações como @CreatedDate se estiver usando o Spring Data Auditing.
}