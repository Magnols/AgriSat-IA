package br.com.fiap.energia_esg_api.service;

import br.com.fiap.energia_esg_api.dto.AreaRequestDTO;
import br.com.fiap.energia_esg_api.model.Area;
import br.com.fiap.energia_esg_api.repository.AreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<Area> listar() {
        return areaRepository.findAll();
    }

    public Area cadastrar(AreaRequestDTO dto) {
        Area area = new Area();
        area.setNome(dto.getNome());
        area.setDescricao(dto.getDescricao());
        return areaRepository.save(area);
    }
}
