package br.com.fiap.energia_esg_api.service;

import br.com.fiap.energia_esg_api.dto.LeituraEnergiaRequestDTO;
import br.com.fiap.energia_esg_api.exception.ResourceNotFoundException;
import br.com.fiap.energia_esg_api.model.Equipamento;
import br.com.fiap.energia_esg_api.model.LeituraEnergia;
import br.com.fiap.energia_esg_api.repository.EquipamentoRepository;
import br.com.fiap.energia_esg_api.repository.LeituraEnergiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeituraEnergiaService {

    private final LeituraEnergiaRepository leituraEnergiaRepository;
    private final EquipamentoRepository equipamentoRepository;

    public LeituraEnergiaService(LeituraEnergiaRepository leituraEnergiaRepository,
                                 EquipamentoRepository equipamentoRepository) {
        this.leituraEnergiaRepository = leituraEnergiaRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<LeituraEnergia> listar() {
        return leituraEnergiaRepository.findAll();
    }

    public LeituraEnergia cadastrar(LeituraEnergiaRequestDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.getIdEquipamento())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipamento nao encontrado para leitura: id=" + dto.getIdEquipamento()));

        LeituraEnergia leituraEnergia = new LeituraEnergia();
        leituraEnergia.setDataHora(dto.getDataHora());
        leituraEnergia.setConsumo(dto.getConsumo());
        leituraEnergia.setEquipamento(equipamento);
        return leituraEnergiaRepository.save(leituraEnergia);
    }
}
