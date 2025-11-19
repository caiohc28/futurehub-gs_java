package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository; // Adicionado

// A interface estende JpaRepository e usa a chave primária Long
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Exemplo de Query Method que pode ser útil
    // List<Avaliacao> findAllByIdeiaId(String ideiaId);
}