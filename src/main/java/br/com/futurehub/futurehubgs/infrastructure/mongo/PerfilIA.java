package br.com.futurehub.futurehubgs.infrastructure.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("dataset_ia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilIA {

    @Id
    private String id;

    private Integer usuario_id;
    private String nome;
    private String email;
    private Integer pontos;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdeiaPub {
        private String titulo;
        private Integer missao_id;
        private Double media_notas;
        private Integer total_avaliacoes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MissaoConc {
        private String objetivo;
        private String area;
    }

    private List<IdeiaPub> ideias_publicadas;
    private List<MissaoConc> missoes_concluidas;
}
