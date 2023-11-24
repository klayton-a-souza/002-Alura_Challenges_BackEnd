package financeiro.api.repository;

import financeiro.api.model.receita.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ReceitaRepository extends JpaRepository<Receita,Long> {
    Boolean existsByDescricao(String descricao);

    Optional<Receita> getReferenceByDescricao(String descricao);

    Page<Receita> findAllByAtivoTrue(Pageable paginacao);
}
