package br.com.fiap.energia_esg_api.controller;

import br.com.fiap.energia_esg_api.model.Alerta;
import br.com.fiap.energia_esg_api.service.AlertaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public List<Alerta> listar() {
        return alertaService.listar();
    }
}
