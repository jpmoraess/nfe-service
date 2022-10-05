package br.com.moraesit.nfeservice.api.controllers;

import br.com.moraesit.nfeservice.api.mapper.ConhecimentoMapper;
import br.com.moraesit.nfeservice.api.models.conhecimentoTransporte.ConhecimentoTransporteModel;
import br.com.moraesit.nfeservice.api.models.conhecimentoTransporte.FiltroConhecimento;
import br.com.moraesit.nfeservice.service.ConhecimentoTransporteService;
import br.com.moraesit.nfeservice.service.DistribuicaoCTeService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.cte.exception.CteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@RestController
@RequestMapping("/conhecimento-transporte")
@CrossOrigin(origins = "*")
public class ConhecimentoTransporteController {

    private final ConhecimentoMapper conhecimentoMapper;
    private final DistribuicaoCTeService distribuicaoCTeService;
    private final ConhecimentoTransporteService conhecimentoTransporteService;

    public ConhecimentoTransporteController(ConhecimentoMapper conhecimentoMapper,
                                            DistribuicaoCTeService distribuicaoCTeService,
                                            ConhecimentoTransporteService conhecimentoTransporteService) {
        this.conhecimentoMapper = conhecimentoMapper;
        this.distribuicaoCTeService = distribuicaoCTeService;
        this.conhecimentoTransporteService = conhecimentoTransporteService;
    }

    @GetMapping
    public Page<ConhecimentoTransporteModel> pesquisar(@RequestParam("empresaId") final Long empresaId,
                                                       FiltroConhecimento filtroConhecimento,
                                                       Pageable pageable) {
        final var conhecimentoTransporte = conhecimentoMapper.filtroToEntity(filtroConhecimento);
        return conhecimentoTransporteService.pesquisar(empresaId, conhecimentoTransporte, pageable)
                .map(conhecimentoMapper::entityToModel);
    }

    @GetMapping("/{id}")
    public ConhecimentoTransporteModel buscar(@PathVariable Long id) {
        return conhecimentoMapper.entityToModel(conhecimentoTransporteService.buscar(id));
    }

    @GetMapping("/consultar")
    public void consultar() throws CteException, JAXBException, CertificadoException, IOException {
        distribuicaoCTeService.consultar();
    }
}
