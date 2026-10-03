package br.com.fiap.energia_esg_api.controller;

import br.com.fiap.energia_esg_api.dto.LeituraEnergiaRequestDTO;
import br.com.fiap.energia_esg_api.model.LeituraEnergia;
import br.com.fiap.energia_esg_api.service.LeituraEnergiaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leituras")
public class LeituraEnergiaController {

    private final LeituraEnergiaService leituraEnergiaService;

    public LeituraEnergiaController(LeituraEnergiaService leituraEnergiaService) {
        this.leituraEnergiaService = leituraEnergiaService;
    }

    @GetMapping
    public List<LeituraEnergia> listar() {
        return leituraEnergiaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeituraEnergia cadastrar(@Valid @RequestBody LeituraEnergiaRequestDTO leituraRequestDTO) {
        return leituraEnergiaService.cadastrar(leituraRequestDTO);
    }
}
