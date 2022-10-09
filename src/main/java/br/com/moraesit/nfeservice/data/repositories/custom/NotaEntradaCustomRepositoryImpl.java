package br.com.moraesit.nfeservice.data.repositories.custom;

import br.com.moraesit.nfeservice.api.models.notaEntrada.FiltroNota;
import br.com.moraesit.nfeservice.data.entities.NotaEntrada;
import br.com.moraesit.nfeservice.data.repositories.projection.ResumoNotaEntrada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotaEntradaCustomRepositoryImpl implements NotaEntradaCustomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public NotaEntradaCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<ResumoNotaEntrada> resumir(FiltroNota filtroNota, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResumoNotaEntrada> criteria = builder.createQuery(ResumoNotaEntrada.class);
        Root<NotaEntrada> root = criteria.from(NotaEntrada.class);

        criteria.select(builder.construct(ResumoNotaEntrada.class,
                root.get("id"),
                root.get("emissao"),
                root.get("chave"),
                root.get("numero"),
                root.get("serie"),
                root.get("nomeEmitente"),
                root.get("cnpjEmitente"),
                root.get("valor")
        ));

        Predicate[] predicates = predicates(filtroNota, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoNotaEntrada> query = entityManager.createQuery(criteria);
        paginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, totalRegistros(filtroNota));
    }

    private Predicate[] predicates(FiltroNota filtroNota, CriteriaBuilder builder, Root<NotaEntrada> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(filtroNota.getEmpresaId())) {
            predicates.add(builder.equal(root.get("empresa").get("id"), filtroNota.getEmpresaId()));
        }

        // TODO: add apache commons lang dependency [StringUtils]
        if (Objects.nonNull(filtroNota.getChave())) {
            predicates.add(builder.like(builder.lower(root.get("chave")), "%" + filtroNota.getChave().toLowerCase() + "%"));
        }

        if (Objects.nonNull(filtroNota.getNumero())) {
            predicates.add(builder.like(builder.lower(root.get("numero")), "%" + filtroNota.getNumero().toLowerCase() + "%"));
        }

        if (Objects.nonNull(filtroNota.getSerie())) {
            predicates.add(builder.like(builder.lower(root.get("serie")), "%" + filtroNota.getSerie().toLowerCase() + "%"));
        }

        if (Objects.nonNull(filtroNota.getNomeEmitente())) {
            predicates.add(builder.like(builder.lower(root.get("nomeEmitente")), "%" + filtroNota.getNomeEmitente().toLowerCase() + "%"));
        }

        if (Objects.nonNull(filtroNota.getCnpjEmitente())) {
            predicates.add(builder.like(builder.lower(root.get("cnpjEmitente")), "%" + filtroNota.getCnpjEmitente().toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[0]);
    }

    private void paginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long totalRegistros(FiltroNota filtroNota) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<NotaEntrada> root = criteria.from(NotaEntrada.class);

        Predicate[] predicates = predicates(filtroNota, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return entityManager.createQuery(criteria).getSingleResult();
    }
}
