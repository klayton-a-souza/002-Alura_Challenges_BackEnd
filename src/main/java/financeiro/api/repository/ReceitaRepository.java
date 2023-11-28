package financeiro.api.repository;

import financeiro.api.dto.receita.ReceitaDto;
import financeiro.api.dto.receita.ReceitaMesDto;
import financeiro.api.model.receita.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ReceitaRepository extends JpaRepository<Receita,Long> {
    Boolean existsByDescricao(String descricao);

    Optional<Receita> getReferenceByDescricao(String descricao);

    Page<Receita> findAllByAtivoTrue(Pageable paginacao);

    @Query(
            """
            select r from Receita r
            where 
            r.descricao = :descricao
            AND
            r.ativo = true           
            """)
    Page<ReceitaDto> buscarPelaDescricao(Pageable paginacao, String descricao);

    @Query(
            """
            select r from Receita r 
            where
            month(r.data) = :mes
            """)
    List<ReceitaMesDto> listarPorMes(int mes);

    @Query(
            """
            select r from Receita r 
            where
            month(r.data) = :mes
            AND
            year(r.data) = :ano
            """)
    List<ReceitaMesDto> listarPorMesEAno(int ano, int mes);

}
