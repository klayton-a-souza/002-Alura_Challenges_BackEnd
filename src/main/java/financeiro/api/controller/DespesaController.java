package financeiro.api.controller;

import financeiro.api.dto.despesa.DespesaDto;
import financeiro.api.exception.ValidacaoException;
import financeiro.api.model.despesa.Despesa;
import financeiro.api.service.DespesaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
}
