package br.com.futurehub.futurehubgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // Importação

@SpringBootApplication
// Adicione esta anotação, apontando para o pacote base dos seus Repositórios
@EnableJpaRepositories(basePackages = "br.com.futurehub.futurehubgs.infrastructure.repository")
public class FuturehubGsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuturehubGsApplication.class, args);
    }
}