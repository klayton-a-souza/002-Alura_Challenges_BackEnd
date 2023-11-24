package financeiro.api.model.despesa;

import financeiro.api.dto.despesa.DespesaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Despesa")
@Table(name = "tb_despesas")

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class Despesa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_despesa;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    public Despesa(DespesaDto dto) {
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data = dto.data();
        this.ativo = true;
        this.tipo = dto.tipo();
    }
}
