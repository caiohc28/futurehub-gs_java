package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*; // Importações do JPA
import lombok.*;
// import org.springframework.data.annotation.Id; // Removido
// import org.springframework.data.mongodb.core.mapping.Document; // Removido

import java.time.LocalDateTime;

@Entity // Anotação JPA para mapear para uma tabela
@Table(name = "avaliacoes") // Define o nome da tabela no Azure SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {

    @Id // Anotação JPA para Chave Primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incremento
    private Long id; // ID numérico para o banco de dados SQL

    // Manter como String para referenciar a Ideia, assumindo que Ideia ainda usa String ou que a Ideia foi migrada para Long e você usará o ID Long
    // NOTA: Se Ideia usar Long como ID, é altamente recomendável mudar 'ideiaId' para Long.
    @Column(name = "ideia_id", nullable = false)
    private Long ideiaId;

    @Column(nullable = false)
    private Integer nota;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // NOTA: Em JPA, muitas vezes usamos o @PrePersist para setar o campo de data de criação
}