package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*; // Importações do JPA
import lombok.*;
// import org.springframework.data.annotation.Id; // Removido
// import org.springframework.data.mongodb.core.mapping.Document; // Removido

import java.time.LocalDateTime;

@Entity // Anotação JPA para mapear para uma tabela
@Table(name = "missoes") // Define o nome da tabela no Azure SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missao {

    @Id // Anotação JPA para Chave Primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incremento (SQL Server)
    private Long id; // ID numérico para o banco de dados SQL

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(nullable = false, length = 255)
    private String objetivo;

    @Column(length = 255)
    private String moral;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    // --- Chave Estrangeira ---
    // Usamos Long assumindo que Area migrou para Long ID
    @Column(name = "area_id", nullable = false)
    private Long areaId;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "gerada_por_ia", nullable = false)
    private boolean geradaPorIa;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}