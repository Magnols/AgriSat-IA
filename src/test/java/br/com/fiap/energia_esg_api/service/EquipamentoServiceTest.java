package br.com.fiap.energia_esg_api.service;

import br.com.fiap.energia_esg_api.dto.EquipamentoRequestDTO;
import br.com.fiap.energia_esg_api.exception.ResourceNotFoundException;
import br.com.fiap.energia_esg_api.model.Area;
import br.com.fiap.energia_esg_api.model.Equipamento;
import br.com.fiap.energia_esg_api.repository.AreaRepository;
import br.com.fiap.energia_esg_api.repository.EquipamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipamentoServiceTest {

    @Mock
    private EquipamentoRepository equipamentoRepository;

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private EquipamentoService equipamentoService;

    @Test
    void deveCadastrarEquipamentoQuandoAreaExiste() {
        Area area = new Area();
        area.setIdArea(10L);
        area.setNome("Irrigacao inteligente");

        EquipamentoRequestDTO dto = new EquipamentoRequestDTO();
        dto.setNome("Bomba solar");
        dto.setStatus("ATIVO");
        dto.setLimiteConsumo(120.5);
        dto.setIdArea(10L);

        when(areaRepository.findById(10L)).thenReturn(Optional.of(area));
        when(equipamentoRepository.save(any(Equipamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Equipamento salvo = equipamentoService.cadastrar(dto);

        assertThat(salvo.getNome()).isEqualTo("Bomba solar");
        assertThat(salvo.getStatus()).isEqualTo("ATIVO");
        assertThat(salvo.getLimiteConsumo()).isEqualTo(120.5);
        assertThat(salvo.getArea()).isEqualTo(area);
        verify(areaRepository).findById(10L);
        verify(equipamentoRepository).save(any(Equipamento.class));
    }

    @Test
    void deveFalharAoCadastrarEquipamentoQuandoAreaNaoExiste() {
        EquipamentoRequestDTO dto = new EquipamentoRequestDTO();
        dto.setNome("Sensor de energia");
        dto.setStatus("ATIVO");
        dto.setLimiteConsumo(50.0);
        dto.setIdArea(99L);

        when(areaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> equipamentoService.cadastrar(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Area nao encontrada");
    }
}
