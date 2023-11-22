package financeiro.api.controller;

import financeiro.api.dto.receita.CadastrarReceitaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.Receita;
import financeiro.api.service.ReceitaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/receitas")

public class ReceitaController {


    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid CadastrarReceitaDto dto, UriComponentsBuilder builder){
        try {

            var uri = builder.path("/receitas/{id_receita}").buildAndExpand(dto.id_receita()).toUri();
            Receita receitaCadastrada = receitaService.cadastrar(dto);

            return ResponseEntity.created(uri).body(receitaCadastrada);
        }catch (ValidacaoException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    @GetMapping
    public String testeController(){
        return "Testando controller";
    }
}
