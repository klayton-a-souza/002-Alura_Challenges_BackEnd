package financeiro.api.repository;

import financeiro.api.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita,Long> {
    Boolean existsByDescricao(String descricao);

    Optional<Receita> getReferenceByDescricao(String descricao);
}
