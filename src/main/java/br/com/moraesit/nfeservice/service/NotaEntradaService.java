package br.com.moraesit.nfeservice.service;

import br.com.moraesit.nfeservice.api.models.notaEntrada.FiltroNota;
import br.com.moraesit.nfeservice.data.entities.NotaEntrada;
import br.com.moraesit.nfeservice.data.repositories.NotaEntradaRepository;
import br.com.moraesit.nfeservice.data.repositories.projection.ResumoNotaEntrada;
import br.com.moraesit.nfeservice.service.impressao.ImpressaoService;
import br.com.moraesit.nfeservice.utils.ArquivoUtil;
import br.com.moraesit.nfeservice.utils.ImpressaoUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.persistence.EntityNotFoundException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class NotaEntradaService {
    private final NotaEntradaRepository notaEntradaRepository;
    private final ImpressaoService impressaoService;

    public NotaEntradaService(NotaEntradaRepository notaEntradaRepository,
                              ImpressaoService impressaoService) {
        this.notaEntradaRepository = notaEntradaRepository;
        this.impressaoService = impressaoService;
    }

    public NotaEntrada buscar(Long id) {
        return notaEntradaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota não encontrada."));
    }

    public Page<ResumoNotaEntrada> resumir(final FiltroNota filtroNota, final Pageable pageable) {
        return notaEntradaRepository.resumir(filtroNota, pageable);
    }

    @Transactional
    public void salvar(List<NotaEntrada> notas) {
        notaEntradaRepository.saveAll(notas);
    }

    public byte[] bytesDanfe(Long id) throws IOException, JRException, ParserConfigurationException, SAXException {
        final var nota = buscar(id);
        final var impressao = ImpressaoUtil.impressaoPadraoNFe(ArquivoUtil.descompactaXml(nota.getXml()));
        return impressaoService.impressaoPdfByte(impressao);
    }

    public void htmlDanfe(Long id) throws IOException, JRException, ParserConfigurationException, SAXException {
        final var nota = buscar(id);
        final var impressao = ImpressaoUtil.impressaoPadraoNFe(ArquivoUtil.descompactaXml(nota.getXml()));
        impressaoService.impressaoHtml(impressao, "C:\\Users\\joaop\\OneDrive\\Área de Trabalho\\notas");
    }
}
