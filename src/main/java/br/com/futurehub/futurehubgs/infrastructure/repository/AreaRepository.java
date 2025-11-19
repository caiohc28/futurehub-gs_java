package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository; // Adicionado
import java.util.Optional;

// A interface estende JpaRepository e usa a chave primária Long
public interface AreaRepository extends JpaRepository<Area, Long> {

    // Exemplo de método que pode ser útil no contexto de "Area"
    Optional<Area> findByNome(String nome);
}