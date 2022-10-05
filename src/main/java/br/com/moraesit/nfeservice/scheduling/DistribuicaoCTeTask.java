package br.com.moraesit.nfeservice.scheduling;

import br.com.moraesit.nfeservice.service.DistribuicaoCTeService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution
public class DistribuicaoCTeTask {
    private final long ONE_HOUR_DELAY = 3600000;
    private final long FIVE_MINUTES_DELAY = 300000;
    private final DistribuicaoCTeService distribuicaoCTeService;

    public DistribuicaoCTeTask(DistribuicaoCTeService distribuicaoCTeService) {
        this.distribuicaoCTeService = distribuicaoCTeService;
    }

    @Scheduled(initialDelay = FIVE_MINUTES_DELAY, fixedDelay = ONE_HOUR_DELAY)
    public void doTask() {
        try {
            log.info("Iniciando a consulta de conhecimentos.");
            //distribuicaoCTeService.consultar();
            log.info("Finalizado a consulta de conhecimentos.");
        } catch (Exception ex) {
            log.error("Erro ao consultar conhecimentos.", ex);
        }
    }
}
