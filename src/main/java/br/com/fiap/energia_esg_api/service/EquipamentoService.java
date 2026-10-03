package br.com.fiap.energia_esg_api.service;

import br.com.fiap.energia_esg_api.dto.EquipamentoRequestDTO;
import br.com.fiap.energia_esg_api.exception.ResourceNotFoundException;
import br.com.fiap.energia_esg_api.model.Area;
import br.com.fiap.energia_esg_api.model.Equipamento;
import br.com.fiap.energia_esg_api.repository.AreaRepository;
import br.com.fiap.energia_esg_api.repository.EquipamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final AreaRepository areaRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, AreaRepository areaRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.areaRepository = areaRepository;
    }

    public List<Equipamento> listar() {
        return equipamentoRepository.findAll();
    }

    public Equipamento cadastrar(EquipamentoRequestDTO dto) {
        Area area = buscarAreaPorId(dto.getIdArea());
        Equipamento equipamento = new Equipamento();
        preencherDadosEquipamento(equipamento, dto, area);
        return equipamentoRepository.save(equipamento);
    }

    public Equipamento atualizar(Long id, EquipamentoRequestDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento nao encontrado: id=" + id));

        Area area = buscarAreaPorId(dto.getIdArea());
        preencherDadosEquipamento(equipamento, dto, area);
        return equipamentoRepository.save(equipamento);
    }

    public void deletar(Long id) {
        if (!equipamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipamento nao encontrado: id=" + id);
        }
        equipamentoRepository.deleteById(id);
    }

    private Area buscarAreaPorId(Long idArea) {
        return areaRepository.findById(idArea)
                .orElseThrow(() -> new ResourceNotFoundException("Area nao encontrada: id=" + idArea));
    }

    private void preencherDadosEquipamento(Equipamento equipamento, EquipamentoRequestDTO dto, Area area) {
        equipamento.setNome(dto.getNome());
        equipamento.setStatus(dto.getStatus());
        equipamento.setLimiteConsumo(dto.getLimiteConsumo());
        equipamento.setArea(area);
    }
}
