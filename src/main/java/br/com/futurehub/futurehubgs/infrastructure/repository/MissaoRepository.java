package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Missao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page; // Adicionado
import org.springframework.data.domain.Pageable; // Adicionado
import java.util.List;
import java.util.Optional;

// A interface estende JpaRepository e usa a chave primária Long
public interface MissaoRepository extends JpaRepository<Missao, Long> {

    // ✅ CORREÇÃO 1: Mudar de List para Page para suportar a paginação (Pageable)
    // ✅ O nome do método deve ser 'findByAreaId' para o Service compilar
    Page<Missao> findByAreaId(Long areaId, Pageable pageable);

    // O método de listagem simples (sem paginação) continua funcionando
    List<Missao> findAllByStatus(String status);
}