package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.IdeiaCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.IdeiaResponse;
import br.com.futurehub.futurehubgs.application.dto.IdeiaUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IdeiaService {

    IdeiaResponse criar(IdeiaCreateRequest req);

    // ✅ areaId agora é Long
    Page<IdeiaResponse> listar(Long areaId, String q, Pageable pageable);

    // ✅ ID da Ideia agora é Long
    IdeiaResponse buscar(Long id);

    // ✅ ID da Ideia agora é Long
    IdeiaResponse atualizar(Long id, IdeiaUpdateRequest req);

    // ✅ ID da Ideia agora é Long
    void deletar(Long id);
}