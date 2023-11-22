package financeiro.api.model;

import financeiro.api.dto.receita.CadastrarReceitaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Receita")
@Table(name = "tb_receitas")

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class Receita {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_receita;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Boolean ativo;

    public Receita(CadastrarReceitaDto dto) {
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data =dto.data();
        this.ativo = true;
    }
}
