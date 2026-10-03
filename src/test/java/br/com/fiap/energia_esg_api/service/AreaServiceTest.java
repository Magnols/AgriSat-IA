package br.com.fiap.energia_esg_api.service;

import br.com.fiap.energia_esg_api.dto.AreaRequestDTO;
import br.com.fiap.energia_esg_api.model.Area;
import br.com.fiap.energia_esg_api.repository.AreaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AreaServiceTest {

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private AreaService areaService;

    @Test
    void deveListarAreasCadastradas() {
        Area area = new Area();
        area.setNome("Galpao solar");
        when(areaRepository.findAll()).thenReturn(List.of(area));

        List<Area> areas = areaService.listar();

        assertThat(areas).hasSize(1);
        assertThat(areas.get(0).getNome()).isEqualTo("Galpao solar");
        verify(areaRepository).findAll();
    }

    @Test
    void deveCadastrarAreaAPartirDoDto() {
        AreaRequestDTO dto = new AreaRequestDTO();
        dto.setNome("Estufa 01");
        dto.setDescricao("Area monitorada por sensores de energia");

        when(areaRepository.save(any(Area.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Area salva = areaService.cadastrar(dto);

        assertThat(salva.getNome()).isEqualTo("Estufa 01");
        assertThat(salva.getDescricao()).isEqualTo("Area monitorada por sensores de energia");
        verify(areaRepository).save(any(Area.class));
    }
}
