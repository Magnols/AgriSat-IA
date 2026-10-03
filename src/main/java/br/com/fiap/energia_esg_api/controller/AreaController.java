package br.com.fiap.energia_esg_api.controller;

import br.com.fiap.energia_esg_api.dto.AreaRequestDTO;
import br.com.fiap.energia_esg_api.model.Area;
import br.com.fiap.energia_esg_api.service.AreaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/areas")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping
    public List<Area> listar() {
        return areaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Area cadastrar(@Valid @RequestBody AreaRequestDTO areaRequestDTO) {
        return areaService.cadastrar(areaRequestDTO);
    }
}
