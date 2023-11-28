package financeiro.api.service;

import financeiro.api.dto.resumo.ResumoDto;
import financeiro.api.model.despesa.Categoria;
import financeiro.api.repository.DespesaRepository;
import financeiro.api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ResumoService {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private DespesaRepository despesaRepository;

    public ResumoDto resumoMes(int ano, int mes) {
        BigDecimal receitaTotal = receitaRepository.receitaDoMes(ano,mes);
        BigDecimal despesaTotal= despesaRepository.despesaDoMes(ano,mes);
        BigDecimal saldo = receitaTotal.subtract(despesaTotal);

        List<Categoria> categorias = Arrays.asList(Categoria.Alimentação,Categoria.Saúde,Categoria.Moradia,Categoria.Trasnporte,Categoria.Educação,Categoria.Lazer,Categoria.Imprevistos,Categoria.Outras);
        List<BigDecimal> valores = new ArrayList<>();

        for (int i = 0; i < categorias.size(); i++) {
            BigDecimal total = despesaRepository.totalCategoria(ano,mes,categorias.get(i));

            if(total == null){
                valores.add(BigDecimal.ZERO);
            }else {
                valores.add(total);
            }
        }
        return new ResumoDto(receitaTotal,despesaTotal,saldo,valores.get(0),valores.get(1),valores.get(2),valores.get(3),valores.get(4),valores.get(5),valores.get(6),valores.get(7));


    }
}
