package br.com.futurehub.futurehubgs.web;

import br.com.futurehub.futurehubgs.application.dto.RankingUsuarioResponse;
import br.com.futurehub.futurehubgs.application.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<RankingUsuarioResponse> listar(
            @RequestParam(required = false) String periodo // Período continua sendo String
    ) {
        // A lógica de busca e os IDs (Longs) são tratados dentro do Serviço.
        return rankingService.listarPorPeriodo(periodo);
    }
}