package financeiro.api.model.receita;

import financeiro.api.dto.receita.AtualizacaoParcialReceitaDto;
import financeiro.api.dto.receita.AtualizacaoTotalReceitaDto;
import financeiro.api.dto.receita.ReceitaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Receita")
@Table(name = "tb_receitas")

@AllArgsConstructor
@NoArgsConstructor
@Getter

@EqualsAndHashCode(of = "id_receita")
public class Receita {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_receita;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Boolean ativo;

    public Receita(ReceitaDto dto) {
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data =dto.data();
        this.ativo = true;
    }
    public void parcial(AtualizacaoParcialReceitaDto dto) {
        if (dto.descricao() != null) {
            this.descricao = dto.descricao();
        }
        if(dto.valor() != null){
            this.valor = dto.valor();
        }
        if(dto.data() != null){
            this.data = dto.data();
        }
    }
    public void total(AtualizacaoTotalReceitaDto dto) {
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data = dto.data();
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }
}
