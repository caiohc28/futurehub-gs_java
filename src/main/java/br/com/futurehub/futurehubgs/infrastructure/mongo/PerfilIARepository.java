package br.com.futurehub.futurehubgs.infrastructure.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PerfilIARepository extends MongoRepository<PerfilIA, String> {

    Optional<PerfilIA> findByEmail(String email);
}
