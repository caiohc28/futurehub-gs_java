package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// A interface estende JpaRepository e usa a chave primária Long
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    // ✅ CORRETO: usuarioId é Long, Periodo é String
    Optional<Ranking> findByUsuarioIdAndPeriodo(Long usuarioId, String periodo);

    // ✅ CORRETO: Periodo é String, a ordenação é feita pelo JPA.
    List<Ranking> findByPeriodoOrderByPontuacaoTotalDesc(String periodo);
}