package br.com.moraesit.nfeservice.scheduling;

import br.com.moraesit.nfeservice.service.DistribuicaoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution
public class DistribuicaoTask {
    private final long ONE_HOUR_DELAY = 3600000;
    private final long FIVE_MINUTES_DELAY = 300000;
    private final DistribuicaoService distribuicaoService;

    public DistribuicaoTask(DistribuicaoService distribuicaoService) {
        this.distribuicaoService = distribuicaoService;
    }

    @Scheduled(initialDelay = FIVE_MINUTES_DELAY, fixedDelay = ONE_HOUR_DELAY)
    public void doTask() {
        try {
            log.info("Iniciando a consulta de notas.");
            //distribuicaoService.consultar();
            log.info("Finalizado a consulta de notas.");
        } catch (Exception ex) {
            log.error("Erro ao consultar notas", ex);
        }
    }
}
