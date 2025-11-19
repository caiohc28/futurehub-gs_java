package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.UsuarioMissao;
import org.springframework.data.jpa.repository.JpaRepository; // Adicionado
import java.util.List;
import java.util.Optional;

// A interface estende JpaRepository e usa a chave primária Long
public interface UsuarioMissaoRepository extends JpaRepository<UsuarioMissao, Long> {

    // Encontrar o registro de uma missão específica para um usuário
    Optional<UsuarioMissao> findByUsuarioIdAndMissaoId(Long usuarioId, Long missaoId);

    // Encontrar todas as missões associadas a um usuário
    List<UsuarioMissao> findAllByUsuarioId(Long usuarioId);

    // Encontrar todas as missões concluídas por status
    List<UsuarioMissao> findAllByStatus(String status);
}