package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.AvaliacaoCreateRequest;
import br.com.futurehub.futurehubgs.domain.Avaliacao;
import br.com.futurehub.futurehubgs.infrastructure.repository.AvaliacaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.application.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService {

    private final AvaliacaoRepository repo;
    private final IdeiaRepository ideiaRepo;

    @Override
    @Transactional
    public void avaliar(AvaliacaoCreateRequest req) {
        var ideia = ideiaRepo.findById(req.idIdeia())
                .orElseThrow(() -> new IllegalArgumentException("ideia not found"));

        repo.save(Avaliacao.builder()
                .ideia(ideia)
                .nota(req.nota())
                .dataAvaliacao(LocalDateTime.now())
                .build());

        // recalcula média simples
        int total = ideia.getTotalAvaliacoes() + 1;
        double soma = ideia.getMediaNotas() * ideia.getTotalAvaliacoes() + req.nota();

        ideia.setTotalAvaliacoes(total);
        ideia.setMediaNotas(soma / total);
    }
}
