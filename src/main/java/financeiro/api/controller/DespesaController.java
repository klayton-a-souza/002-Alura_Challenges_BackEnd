package financeiro.api.controller;

import financeiro.api.dto.despesa.AtualizacaoDespesaDto;
import financeiro.api.dto.despesa.DespesaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.despesa.Despesa;
import financeiro.api.service.DespesaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/despesas")

public class DespesaController {

    @Autowired
    private DespesaService despesaService;
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DespesaDto dto, UriComponentsBuilder builder){
        try {
            var uri = builder.path("/despesas/{id_despesa}/").buildAndExpand(dto.id_despesa()).toUri();
            Despesa despesaCadastrada = despesaService.cadastrar(dto);
            return ResponseEntity.created(uri).body(despesaCadastrada);
        }catch (ValidacaoException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }
    @GetMapping
    public ResponseEntity<List<DespesaDto>> listarDespesas(@PageableDefault (size = 5) Pageable paginacao){
        List<DespesaDto> listaDeTodasDespesas = despesaService.listar(paginacao);
        return ResponseEntity.ok(listaDeTodasDespesas);
    }
    @GetMapping("{id_despesa}")
    public ResponseEntity detalhar(@PathVariable Long id_despesa){
        try {
            Despesa despesaDetalhada = despesaService.detalhar(id_despesa);
            return ResponseEntity.ok(new DespesaDto(despesaDetalhada));
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PatchMapping
    @Transactional
    public ResponseEntity parcial(@RequestBody @Valid AtualizacaoDespesaDto dto){
        try {
            Despesa despesaAtualizada = despesaService.parcial(dto);
            return ResponseEntity.ok(new AtualizacaoDespesaDto(despesaAtualizada));
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }



}
