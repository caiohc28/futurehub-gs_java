package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.AvaliacaoCreateRequest;
import br.com.futurehub.futurehubgs.application.service.AvaliacaoService;
import br.com.futurehub.futurehubgs.domain.Avaliacao;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.infrastructure.repository.AvaliacaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
// import br.com.futurehub.futurehubgs.messaging.AvaliacaoEventPublisher; // REMOVIDO
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService {

    private final IdeiaRepository ideiaRepo;
    private final AvaliacaoRepository avaliacaoRepo;
    // private final AvaliacaoEventPublisher publisher; // REMOVIDO

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void avaliar(AvaliacaoCreateRequest req) {

        // Assumindo que o DTO foi atualizado e req.idIdeia() retorna Long
        Long ideiaId = req.idIdeia();

        // findById do JpaRepository espera um Long
        Ideia ideia = ideiaRepo.findById(ideiaId)
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        // Cria a avaliação
        Avaliacao avaliacao = Avaliacao.builder()
                .ideiaId(ideia.getId())
                .nota(req.nota())
                .dataAvaliacao(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        avaliacaoRepo.save(avaliacao);

        // Atualiza média e total de avaliações da ideia
        int totalAnterior = ideia.getTotalAvaliacoes() != null ? ideia.getTotalAvaliacoes() : 0;
        double mediaAnterior = ideia.getMediaNotas() != null ? ideia.getMediaNotas() : 0.0;

        int totalNovo = totalAnterior + 1;
        double somaAnterior = mediaAnterior * totalAnterior;
        double mediaNova = (somaAnterior + req.nota()) / totalNovo;

        ideia.setTotalAvaliacoes(totalNovo);
        ideia.setMediaNotas(mediaNova);
        ideiaRepo.save(ideia);

        // REMOVIDO: chamada ao publisher
    }
}