package financeiro.api.service;

import financeiro.api.dto.resumo.ResumoDto;
import financeiro.api.model.despesa.Categoria;
import financeiro.api.repository.DespesaRepository;
import financeiro.api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

        BigDecimal alimentacao = despesaRepository.totalCategoria(ano,mes, Categoria.Alimentação);
        BigDecimal saude = despesaRepository.totalCategoria(ano,mes,Categoria.Saúde);
        BigDecimal moradia = despesaRepository.totalCategoria(ano,mes,Categoria.Moradia);
        BigDecimal transporte = despesaRepository.totalCategoria(ano,mes,Categoria.Trasnporte);
        BigDecimal educacao = despesaRepository.totalCategoria(ano,mes,Categoria.Educação);
        BigDecimal lazer = despesaRepository.totalCategoria(ano,mes,Categoria.Lazer);
        BigDecimal imprevistos = despesaRepository.totalCategoria(ano,mes,Categoria.Imprevistos);
        BigDecimal outras = despesaRepository.totalCategoria(ano,mes,Categoria.Outras);
        return new ResumoDto(receitaTotal,despesaTotal,saldo,alimentacao,saude,moradia,transporte,educacao,lazer,imprevistos,outras);


    }
}
