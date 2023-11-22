package financeiro.api.controller;

import financeiro.api.dto.receita.AtualizacaoReceitaDto;
import financeiro.api.dto.receita.ReceitaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.Receita;
import financeiro.api.service.ReceitaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/receitas")

public class ReceitaController {
    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid ReceitaDto dto, UriComponentsBuilder builder){
        try {
            var uri = builder.path("/receitas/{id_receita}").buildAndExpand(dto.id_receita()).toUri();
            Receita receitaCadastrada = receitaService.cadastrar(dto);

            return ResponseEntity.created(uri).body(receitaCadastrada);
        }catch (ValidacaoException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<ReceitaDto>> listar(@PageableDefault (size = 5) Pageable paginacao){
        List<ReceitaDto> receitas = receitaService.listar(paginacao);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("{id_receita}")
    public ResponseEntity detalhar(@PathVariable Long id_receita){
        try {
            Receita receitaDetalhada = receitaService.detalhar(id_receita);
            return ResponseEntity.ok(new ReceitaDto(receitaDetalhada));
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PatchMapping
    @Transactional
    public ResponseEntity parcial(@RequestBody @Valid AtualizacaoReceitaDto dto){
        try {
            Receita receitaAtualizada = receitaService.parcial(dto);
            return ResponseEntity.ok(new AtualizacaoReceitaDto(receitaAtualizada));
        }catch (ValidacaoException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

}
