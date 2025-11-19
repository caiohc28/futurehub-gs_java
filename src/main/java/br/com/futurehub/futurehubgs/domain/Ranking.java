package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*; // Importações do JPA
import lombok.*;
// import org.springframework.data.annotation.Id; // Removido
// import org.springframework.data.mongodb.core.mapping.Document; // Removido

@Entity // Anotação JPA para mapear para uma tabela
@Table(name = "rankings") // Define o nome da tabela no Azure SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id // Anotação JPA para Chave Primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incremento (SQL Server)
    private Long id; // ID numérico para o banco de dados SQL

    // --- Chave Estrangeira ---
    // Usamos Long assumindo que Usuario migrou para Long ID
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "pontuacao_total", nullable = false)
    private Integer pontuacaoTotal;

    @Column(nullable = false, length = 50)
    private String periodo;
}