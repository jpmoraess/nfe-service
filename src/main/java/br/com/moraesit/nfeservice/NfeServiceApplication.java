package br.com.moraesit.nfeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NfeServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(NfeServiceApplication.class, args);
    }

//    private final NFeApplicationService nFeApplicationService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        final var path = "C:\\Users\\joaop\\OneDrive\\Área de Trabalho\\certificados\\wordsports\\empresa3.pfx";
//        final var senha = "PHD0535";
//
//        final var configuracoes = nFeApplicationService.configuracoesNfe(path, senha);
//
//        final var cnpj = "41490133000170";
//        final var nsu = "000000000000008";
//
//        nFeApplicationService.downloadPorNSU(cnpj, nsu, configuracoes);
//    }
}
