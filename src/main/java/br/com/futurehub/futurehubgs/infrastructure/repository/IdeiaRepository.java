package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Ideia;
import org.springframework.data.jpa.repository.JpaRepository; // Adicionado
import java.util.List;
import java.util.Optional;

// A interface estende JpaRepository e usa a chave primária Long
public interface IdeiaRepository extends JpaRepository<Ideia, Long> {

    // Exemplo de Query Method que pode ser útil
    Optional<Ideia> findByTitulo(String titulo);

    // Encontrar todas as ideias de um autor (chave estrangeira)
    List<Ideia> findAllByAutorId(Long autorId);

    // Encontrar todas as ideias de uma missão (chave estrangeira)
    List<Ideia> findAllByMissaoId(Long missaoId);
}