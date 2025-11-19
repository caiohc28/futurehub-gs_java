package br.com.futurehub.futurehubgs.application.dto;

public record RankingUsuarioResponse(
        Long usuarioId,
        String usuarioNome,
        Integer pontuacaoTotal,
        String periodo,
        Integer posicao
) {}
