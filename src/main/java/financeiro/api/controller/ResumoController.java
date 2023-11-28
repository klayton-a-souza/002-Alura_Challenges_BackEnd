package financeiro.api.controller;

import financeiro.api.dto.resumo.ResumoDto;
import financeiro.api.service.ResumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;

    @GetMapping("/{ano}/{mes}/")
    public ResponseEntity resumoMes(@PathVariable int ano, @PathVariable int mes){
        ResumoDto resumo = resumoService.resumoMes(ano,mes);
        return ResponseEntity.ok(resumo);
    }
}
