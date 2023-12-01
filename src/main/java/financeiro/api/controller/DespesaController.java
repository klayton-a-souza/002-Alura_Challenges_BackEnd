package financeiro.api.controller;

import financeiro.api.dto.despesa.AtualizacaoPacialDespesaDto;
import financeiro.api.dto.despesa.AtualizacaoTotalDespesaDto;
import financeiro.api.dto.despesa.DespesaDataDto;
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
    public ResponseEntity<List<DespesaDto>> listarDespesas(@PageableDefault (size = 5) Pageable paginacao, @RequestParam (required = false) String descricao){
        try {
            return ResponseEntity.ok(despesaService.listar(paginacao,descricao));
        }catch (ValidacaoException exception){
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity parcial(@RequestBody @Valid AtualizacaoPacialDespesaDto dto){
        try {
            Despesa despesaAtualizada = despesaService.parcial(dto);
            return ResponseEntity.ok(new AtualizacaoPacialDespesaDto(despesaAtualizada));
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity total(@RequestBody @Valid AtualizacaoTotalDespesaDto dto){
        try {
            Despesa despesaAtualizada = despesaService.total(dto);
            return ResponseEntity.ok(new AtualizacaoTotalDespesaDto(despesaAtualizada));
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("{id_despesa}")
    @Transactional
    public ResponseEntity exlusaoLogica(@PathVariable Long id_despesa){
        try {
            despesaService.exclusaoLogica(id_despesa);
            return ResponseEntity.noContent().build();
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @GetMapping("/{ano}/{mes}/")
    public ResponseEntity listarPelaDta(@PathVariable int ano, @PathVariable int mes){
        try {
            List<DespesaDataDto>  despesaDataDtos = despesaService.listarPelaData(ano,mes);
            return ResponseEntity.ok(despesaDataDtos);
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }



}
