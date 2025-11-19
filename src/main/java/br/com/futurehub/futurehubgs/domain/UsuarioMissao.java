package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*; // Importações do JPA
import lombok.*;
// import org.springframework.data.annotation.Id; // Removido
// import org.springframework.data.mongodb.core.mapping.Document; // Removido

import java.time.LocalDateTime;

@Entity // Anotação JPA para mapear para uma tabela
@Table(name = "usuarios_missoes") // Define o nome da tabela no Azure SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioMissao {

    @Id // Anotação JPA para Chave Primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incremento (SQL Server)
    private Long id; // ID numérico para o banco de dados SQL

    // --- Chaves Estrangeiras ---
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "missao_id", nullable = false)
    private Long missaoId;

    @Column(name = "data_conclusao") // Pode ser nulo se a missão não estiver concluída
    private LocalDateTime dataConclusao;

    @Column(nullable = false, length = 50)
    private String status;

    // NOTA: Para garantir que um usuário só possa ter um registro de missão por vez,
    // você poderia adicionar um @UniqueConstraint na anotação @Table,
    // mas isso depende da sua regra de negócio.
}