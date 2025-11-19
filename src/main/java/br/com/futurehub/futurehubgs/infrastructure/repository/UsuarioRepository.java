package br.com.futurehub.futurehubgs.infrastructure.repository;

import br.com.futurehub.futurehubgs.domain.Usuario;
// import org.springframework.data.mongodb.repository.MongoRepository; // Removido
import org.springframework.data.jpa.repository.JpaRepository; // Adicionado

import java.util.Optional;

// A interface agora estende JpaRepository e usa a chave primária Long
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Estes métodos funcionam exatamente da mesma forma no JPA (Query Methods)
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}