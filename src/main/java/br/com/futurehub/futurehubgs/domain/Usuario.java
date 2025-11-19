package br.com.futurehub.futurehubgs.domain;

import jakarta.persistence.*; // Importações do JPA
import lombok.*;
// import org.springframework.data.annotation.Id; // Removido
// import org.springframework.data.mongodb.core.mapping.Document; // Removido

@Entity // Anotação JPA para mapear para uma tabela
@Table(name = "usuarios") // Define o nome da tabela no Azure SQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id // Anotação JPA para Chave Primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura o ID para ser auto-incremento (padrão do SQL Server)
    private Long id; // Recomendado usar Long ou Integer para IDs auto-incremento em SQL

    @Column(nullable = false, length = 100) // Exemplo de restrição de coluna (opcional, mas recomendado)
    private String nome;

    @Column(nullable = false, unique = true) // Email deve ser único no DB
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 60)
    private String senhaHash;

    @Enumerated(EnumType.STRING) // Garante que a enumeração seja salva como String ('ROLE_USER'/'ROLE_ADMIN') no DB
    private Role role;

    @Column(name = "area_interesse_id")
    private String areaInteresseId;

    // NOTA: Se 'areaInteresseId' fosse uma chave estrangeira para outra tabela,
    // usaríamos @ManyToOne aqui, em vez de apenas String.

    @Builder.Default
    private Integer pontos = 0;

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }
}