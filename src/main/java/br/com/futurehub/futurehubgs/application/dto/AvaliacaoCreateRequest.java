package br.com.futurehub.futurehubgs.application.dto;

import jakarta.validation.constraints.*;

public record AvaliacaoCreateRequest(
        @NotNull Long idIdeia,  // Agora ID é String no Mongo
        @Min(1) @Max(5) int nota
) {}
