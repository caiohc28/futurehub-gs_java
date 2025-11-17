package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.IdeiaCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.IdeiaResponse;
import br.com.futurehub.futurehubgs.application.dto.IdeiaUpdateRequest;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.domain.Missao;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.MissaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.UsuarioRepository;
import br.com.futurehub.futurehubgs.messaging.IdeaEventPublisher;
import br.com.futurehub.futurehubgs.application.service.IdeiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdeiaServiceImpl implements IdeiaService {

    private final IdeiaRepository ideiaRepo;
    private final UsuarioRepository usuarioRepo;
    private final MissaoRepository missaoRepo;
    private final IdeaEventPublisher publisher;

    private IdeiaResponse toResp(Ideia i) {
        return new IdeiaResponse(
                i.getId(),
                i.getTitulo(),
                i.getDescricao(),
                i.getAutor() != null ? i.getAutor().getId() : null,
                i.getAutor() != null ? i.getAutor().getNome() : null,
                i.getMissao() != null ? i.getMissao().getId() : null,
                i.getMediaNotas(),
                i.getTotalAvaliacoes(),
                i.getCreatedAt()
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse criar(IdeiaCreateRequest req) {
        var autor = usuarioRepo.findById(req.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("usuario not found"));

        Missao missao = null;
        if (req.idMissao() != null) {
            missao = missaoRepo.findById(req.idMissao())
                    .orElseThrow(() -> new IllegalArgumentException("missao not found"));
        }

        var ideia = Ideia.builder()
                .titulo(req.titulo())
                .descricao(req.descricao())
                .autor(autor)
                .missao(missao)
                .mediaNotas(0.0)
                .totalAvaliacoes(0)
                .createdAt(LocalDateTime.now())
                .build();

        ideia = ideiaRepo.save(ideia);
        publisher.publishCreated(ideia);

        return toResp(ideia);
    }

    @Override
    @Cacheable(
            value = "ideiasPorArea",
            key = "#areaId + '-' + #q + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public Page<IdeiaResponse> listar(Long areaId, String q, Pageable pageable) {
        Page<Ideia> page;

        if (areaId != null) {
            page = ideiaRepo.findByAutor_AreaInteresse_Id(areaId, pageable);
        } else if (q != null && !q.isBlank()) {
            page = ideiaRepo.findByTituloContainingIgnoreCase(q, pageable);
        } else {
            page = ideiaRepo.findAll(pageable);
        }

        return page.map(this::toResp);
    }

    @Override
    public IdeiaResponse buscar(Long id) {
        var ideia = ideiaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ideia not found"));

        return toResp(ideia);
    }

    @Override
    @Transactional
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse atualizar(Long id, IdeiaUpdateRequest req) {
        var ideia = ideiaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ideia not found"));

        ideia.setTitulo(req.titulo());
        ideia.setDescricao(req.descricao());

        return toResp(ideia);
    }

    @Override
    @Transactional
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void deletar(Long id) {
        ideiaRepo.deleteById(id);
    }
}
