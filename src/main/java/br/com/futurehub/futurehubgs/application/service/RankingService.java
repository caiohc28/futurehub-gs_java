package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.RankingUsuarioResponse;
import java.util.List;

public interface RankingService {

    // ✅ ideiaId agora é Long
    void processarEventoAvaliacao(Long ideiaId, int nota);

    // O período continua sendo String, pois é uma data formatada.
    List<RankingUsuarioResponse> listarPorPeriodo(String periodo);
}