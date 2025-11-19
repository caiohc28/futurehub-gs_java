package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.IdeiaCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.IdeiaResponse;
import br.com.futurehub.futurehubgs.application.dto.IdeiaUpdateRequest;
import br.com.futurehub.futurehubgs.application.service.IdeiaService;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.domain.Missao;
import br.com.futurehub.futurehubgs.domain.Usuario;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.MissaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.UsuarioRepository;
import br.com.futurehub.futurehubgs.messaging.IdeaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IdeiaServiceImpl implements IdeiaService {

    private final IdeiaRepository ideiaRepo;
    private final UsuarioRepository usuarioRepo;
    private final MissaoRepository missaoRepo;
    private final IdeaEventPublisher publisher;

    private IdeiaResponse toResp(Ideia i) {

        String autorNome = null;
        if (i.getAutorId() != null) {
            // ✅ findById recebe Long
            autorNome = usuarioRepo.findById(i.getAutorId())
                    .map(Usuario::getNome)
                    .orElse(null);
        }

        return new IdeiaResponse(
                i.getId(),
                i.getTitulo(),
                i.getDescricao(),
                i.getAutorId(),
                autorNome,
                i.getMissaoId(),
                i.getMediaNotas(),
                i.getTotalAvaliacoes(),
                i.getCreatedAt()
        );
    }

    // --- Criação (POST) ---
    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse criar(IdeiaCreateRequest req) {

        // ✅ req.idUsuario() deve ser Long
        Usuario autor = usuarioRepo.findById(req.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("erro.usuario.nao.encontrado"));

        Missao missao = null;
        if (req.idMissao() != null) {
            // ✅ req.idMissao() deve ser Long
            missao = missaoRepo.findById(req.idMissao())
                    .orElseThrow(() -> new IllegalArgumentException("erro.missao.nao.encontrada"));
        }

        Ideia ideia = Ideia.builder()
                .titulo(req.titulo())
                .descricao(req.descricao())
                // ✅ autor.getId() e missao.getId() agora são Longs
                .autorId(autor.getId())
                .missaoId(missao != null ? missao.getId() : null)
                .mediaNotas(0.0)
                .totalAvaliacoes(0)
                .createdAt(LocalDateTime.now())
                .build();

        ideia = ideiaRepo.save(ideia);

        // ✅ Publisher precisa aceitar Long ID ou a entidade Ideia
        publisher.publishCreated(ideia);

        return toResp(ideia);
    }

    // --- Listagem (GET ALL) ---
    @Override
    @Cacheable(
            value = "ideiasPorArea",
            key = "#areaId + '-' + (#q == null ? '' : #q.trim()) + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    // ✅ Assinatura corrigida para Long areaId
    public Page<IdeiaResponse> listar(Long areaId, String q, Pageable pageable) {

        // ⚠️ ATENÇÃO: LÓGICA INEFICIENTE MANTIDA PARA COMPILAR.
        // IDEAL É USAR REPOSITÓRIOS JPA!

        List<Ideia> todas = ideiaRepo.findAll();
        String query = (q == null || q.isBlank()) ? null : q.trim().toLowerCase();

        List<Ideia> filtradas = todas.stream()
                .filter(i -> {
                    boolean matchArea = true;
                    // Lógica de filtragem atualizada para usar Long areaId
                    if (areaId != null) {

                        // findById recebe Long
                        Usuario autor = usuarioRepo.findById(i.getAutorId()).orElse(null);

                        // O Autor.getAreaInteresseId() agora deve ser Long para comparação
                        // Usamos Long.toString(areaId) para manter compatibilidade com String autor.getAreaInteresseId()
                        // No entanto, é melhor garantir que autor.getAreaInteresseId() seja Long.
                        matchArea = autor != null
                                && autor.getAreaInteresseId() != null
                                && areaId.equals(autor.getAreaInteresseId()); // ✅ Comparação Long com Long (assumindo a migração da Entidade Usuario)
                    }

                    boolean matchTitulo = true;
                    if (query != null) {
                        matchTitulo = i.getTitulo() != null
                                && i.getTitulo().toLowerCase().contains(query);
                    }

                    return matchArea && matchTitulo;
                })
                .sorted(Comparator.comparing(Ideia::getCreatedAt).reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtradas.size());
        if (start > end) {
            return new PageImpl<>(List.of(), pageable, filtradas.size());
        }

        List<IdeiaResponse> content = filtradas.subList(start, end)
                .stream()
                .map(this::toResp)
                .toList();

        return new PageImpl<>(content, pageable, filtradas.size());
    }

    // --- Busca por ID (GET) ---
    @Override
    public IdeiaResponse buscar(Long id) {
        Ideia ideia = ideiaRepo.findById(id) // ✅ findById recebe Long
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));
        return toResp(ideia);
    }

    // --- Atualização (PUT) ---
    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse atualizar(Long id, IdeiaUpdateRequest req) {

        Ideia ideia = ideiaRepo.findById(id) // ✅ findById recebe Long
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        ideia.setTitulo(req.titulo());
        ideia.setDescricao(req.descricao());

        ideia = ideiaRepo.save(ideia);

        return toResp(ideia);
    }

    // --- Deleção (DELETE) ---
    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void deletar(Long id) {

        if (!ideiaRepo.existsById(id)) { // ✅ existsById recebe Long
            throw new IllegalArgumentException("erro.ideia.nao.encontrada");
        }

        ideiaRepo.deleteById(id); // ✅ deleteById recebe Long
    }
}