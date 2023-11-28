package financeiro.api.repository;

import financeiro.api.dto.despesa.DespesaDataDto;
import financeiro.api.dto.despesa.DespesaDto;
import financeiro.api.model.despesa.Categoria;
import financeiro.api.model.despesa.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa,Long> {
    Optional<Despesa> getReferenceByDescricao(String descricao);

    Page<Despesa> findAllByAtivoTrue(Pageable paginacao);

    @Query(
            """
            select d from Despesa d
            where
            d.descricao = :descricao
            AND
            d.ativo = true
            """)
    Page<DespesaDto> buscarPelaDescricao(Pageable paginacao, String descricao);

    @Query(
            """
            SELECT d FROM Despesa d
            WHERE
            MONTH(d.data) = :mes
            AND
            YEAR(d.data) = :ano
            """)
    List<DespesaDataDto> listarPelaData(int ano, int mes);

    @Query(
            """
            SELECT SUM(d.valor) FROM Despesa d
            WHERE
            MONTH(d.data) = :mes
            AND
            YEAR(d.data) = :ano
            """)
    BigDecimal despesaDoMes(int ano, int mes);

    @Query(
            """
            SELECT SUM (c.valor) FROM Despesa c
            WHERE
            c.categoria = :categoria
            AND
            MONTH(c.data) = :mes
            AND 
            YEAR(c.data) = :ano
            """)
    BigDecimal totalCategoria(int ano, int mes, Categoria categoria);
}
