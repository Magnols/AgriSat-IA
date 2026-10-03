package br.com.fiap.energia_esg_api.controller;

import br.com.fiap.energia_esg_api.dto.EquipamentoRequestDTO;
import br.com.fiap.energia_esg_api.model.Equipamento;
import br.com.fiap.energia_esg_api.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @GetMapping
    public List<Equipamento> listar() {
        return equipamentoService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipamento cadastrar(@Valid @RequestBody EquipamentoRequestDTO equipamentoRequestDTO) {
        return equipamentoService.cadastrar(equipamentoRequestDTO);
    }

    @PutMapping("/{id}")
    public Equipamento atualizar(@PathVariable Long id, @Valid @RequestBody EquipamentoRequestDTO equipamentoRequestDTO) {
        return equipamentoService.atualizar(id, equipamentoRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        equipamentoService.deletar(id);
    }
}
