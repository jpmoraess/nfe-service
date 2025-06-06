package br.com.moraesit.nfeservice.data.specs;

import br.com.moraesit.nfeservice.data.entities.ConhecimentoTransporte;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Objects;

public class ConhecimentoTransporteSpecs {

    public static Specification<ConhecimentoTransporte> filtrar(final Long empresaId, ConhecimentoTransporte filtro) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(empresaId)) {
                predicates.add(builder.equal(root.get("empresa").get("id"), empresaId));
            }

            if (Objects.nonNull(filtro.getSerie())) {
                predicates.add(builder.like(
                        builder.lower(root.get("serie")), "%" + filtro.getSerie().toLowerCase() + "%"));
            }

            if (Objects.nonNull(filtro.getNumero())) {
                predicates.add(builder.like(
                        builder.lower(root.get("numero")), "%" + filtro.getNumero().toLowerCase() + "%"));
            }

            if (Objects.nonNull(filtro.getChave())) {
                predicates.add(builder.like(
                        builder.lower(root.get("chave")), "%" + filtro.getChave().toLowerCase() + "%"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
