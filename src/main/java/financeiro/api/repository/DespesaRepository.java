package financeiro.api.repository;

import financeiro.api.model.despesa.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa,Long> {
    Optional<Despesa> getReferenceByDescricao(String descricao);

    Page<Despesa> findAllByAtivoTrue(Pageable paginacao);
}
