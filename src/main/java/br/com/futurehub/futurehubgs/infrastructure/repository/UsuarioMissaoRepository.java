package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.UsuarioMissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioMissaoRepository extends JpaRepository<UsuarioMissao, UsuarioMissao.PK> {
}
