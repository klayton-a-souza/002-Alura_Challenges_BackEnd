package financeiro.api.model.despesa;

import financeiro.api.dto.despesa.AtualizacaoPacialDespesaDto;
import financeiro.api.dto.despesa.AtualizacaoTotalDespesaDto;
import financeiro.api.dto.despesa.DespesaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Despesa")
@Table(name = "tb_despesas")

@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(of = "id_despesa")
public class Despesa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_despesa;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public Long getId_despesa() {
        return id_despesa;
    }
    public String getDescricao() {
        return descricao;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public LocalDateTime getData() {
        return data;
    }
    public Boolean getAtivo() {
        return ativo;
    }
    public Categoria getCategoria() {
        return categoria;
    }

    public Despesa(DespesaDto dto) {
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data = dto.data();
        this.ativo = true;
        this.categoria = dto.categoria();
    }

    public Despesa(DespesaDto dto, Categoria categoria) {
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data = dto.data();
        this.ativo = true;
        this.categoria = categoria;
    }

    public Despesa(Despesa despesaDetalhada) {
        this.descricao = despesaDetalhada.getDescricao();
        this.valor = despesaDetalhada.getValor();
        this.data = despesaDetalhada.data;
        this.ativo = true;
        this.categoria = despesaDetalhada.categoria;
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

    public void parcial(AtualizacaoPacialDespesaDto dto) {
        if(dto.descricao() != null){
            this.descricao = dto.descricao();
        }
        if(dto.valor() != null){
            this.valor = dto.valor();
        }
        if(dto.data() != null){
            this.data = dto.data();
        }
        if(dto.categoria() != null){
            this.categoria = dto.categoria();
        }
    }

    public void total(AtualizacaoTotalDespesaDto dto) {
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data = dto.data();
        this.categoria = dto.categoria();
    }
}
