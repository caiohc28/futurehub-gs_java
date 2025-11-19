package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.AvaliacaoCreateRequest;
import br.com.futurehub.futurehubgs.application.service.AvaliacaoService;
import br.com.futurehub.futurehubgs.domain.Avaliacao;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.infrastructure.repository.AvaliacaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.messaging.AvaliacaoEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService {

    private final IdeiaRepository ideiaRepo;
    private final AvaliacaoRepository avaliacaoRepo;
    private final AvaliacaoEventPublisher publisher;

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void avaliar(AvaliacaoCreateRequest req) {

        // 🎯 MUDANÇA 1: O ID da ideia no DTO (req.idIdeia()) DEVE ser Long agora.
        // Se o DTO ainda usa String, você precisará convertê-lo ou atualizar o DTO.
        // Assumindo que o DTO foi atualizado ou será convertido:
        Long ideiaId;
        try {
            ideiaId = Long.valueOf(req.idIdeia()); // Conversão ou ajuste do DTO
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("erro.ideia.id.invalido");
        }


        // 🎯 MUDANÇA 2: O método findById do JpaRepository espera um Long
        Ideia ideia = ideiaRepo.findById(ideiaId)
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        // Cria a avaliação
        Avaliacao avaliacao = Avaliacao.builder()
                // 🎯 MUDANÇA 3: ideia.getId() e ideiaId são Longs
                .ideiaId(ideia.getId())
                .nota(req.nota())
                .dataAvaliacao(LocalDateTime.now()) // dataAvaliacao é necessária na sua Entidade
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

        // 🎯 MUDANÇA 4: O publisher agora dispara o ID da Ideia como Long (se o publisher aceitar Long)
        publisher.publishAvaliacao(ideia.getId(), req.nota());
    }
}