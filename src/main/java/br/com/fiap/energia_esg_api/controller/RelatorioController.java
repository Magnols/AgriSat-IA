package br.com.fiap.energia_esg_api.controller;

import br.com.fiap.energia_esg_api.model.ConsumoDiario;
import br.com.fiap.energia_esg_api.service.ConsumoDiarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final ConsumoDiarioService consumoDiarioService;

    public RelatorioController(ConsumoDiarioService consumoDiarioService) {
        this.consumoDiarioService = consumoDiarioService;
    }

    @GetMapping("/consumo-diario")
    public List<ConsumoDiario> listarConsumoDiario() {
        return consumoDiarioService.listarConsumoDiario();
    }
}
