package br.com.futurehub.futurehubgs.messaging;

import br.com.futurehub.futurehubgs.application.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.futurehub.futurehubgs.config.RabbitConfig.AVALIACOES_QUEUE;

/**
 * Listener de eventos de avaliação.
 * Responsabilidade: ler mensagem da fila e delegar para RankingService.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AvaliacaoEventListener {

    private final RankingService rankingService;

    @RabbitListener(queues = AVALIACOES_QUEUE)
    public void onMessage(String payload) {
        // Agora o produtor do evento (IdeaEventPublisher) deve garantir que
        // o payload seja enviado no formato: "ID_LONGO;NOTA"
        try {
            String[] parts = payload.split(";");

            // ✅ CORREÇÃO 1: O ID ainda vem como String no payload, mas será convertido para Long
            String ideiaIdString = parts[0];

            // ✅ CORREÇÃO 2: Conversão explícita para Long para alinhar com o Serviço
            Long ideiaId = Long.parseLong(ideiaIdString);

            int nota = Integer.parseInt(parts[1]);

            log.info("Evento de avaliação recebido: ideiaId={}, nota={}", ideiaId, nota);

            // ✅ CORREÇÃO 3: O serviço processarEventoAvaliacao agora recebe Long
            rankingService.processarEventoAvaliacao(ideiaId, nota);

        } catch (NumberFormatException e) {
            log.error("Erro de formato! O ID da ideia ou a nota não são numéricos. Payload: {}", payload, e);
        } catch (Exception e) {
            log.error("Erro ao processar evento de avaliação. Payload: {}", payload, e);
        }
    }
}