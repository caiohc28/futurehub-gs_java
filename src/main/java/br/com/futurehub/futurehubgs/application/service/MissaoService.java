package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.MissaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MissaoService {

    // ✅ areaId agora é Long
    MissaoResponse gerarMissaoPorArea(Long areaId);

    // ✅ areaId agora é Long
    Page<MissaoResponse> listarPorArea(Long areaId, Pageable pageable);
}