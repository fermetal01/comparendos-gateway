package co.edu.ucentral.app.web.rest;

import co.edu.ucentral.app.ComparendosApp;
import co.edu.ucentral.app.domain.TipoIdentificacion;
import co.edu.ucentral.app.repository.TipoIdentificacionRepository;
import co.edu.ucentral.app.repository.search.TipoIdentificacionSearchRepository;
import co.edu.ucentral.app.service.TipoIdentificacionService;
import co.edu.ucentral.app.service.dto.TipoIdentificacionDTO;
import co.edu.ucentral.app.service.mapper.TipoIdentificacionMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TipoIdentificacionResource} REST controller.
 */
@SpringBootTest(classes = ComparendosApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TipoIdentificacionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private TipoIdentificacionRepository tipoIdentificacionRepository;

    @Autowired
    private TipoIdentificacionMapper tipoIdentificacionMapper;

    @Autowired
    private TipoIdentificacionService tipoIdentificacionService;

    /**
     * This repository is mocked in the co.edu.ucentral.app.repository.search test package.
     *
     * @see co.edu.ucentral.app.repository.search.TipoIdentificacionSearchRepositoryMockConfiguration
     */
    @Autowired
    private TipoIdentificacionSearchRepository mockTipoIdentificacionSearchRepository;

    @Autowired
    private MockMvc restTipoIdentificacionMockMvc;

    private TipoIdentificacion tipoIdentificacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoIdentificacion createEntity() {
        TipoIdentificacion tipoIdentificacion = new TipoIdentificacion()
            .nombre(DEFAULT_NOMBRE);
        return tipoIdentificacion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoIdentificacion createUpdatedEntity() {
        TipoIdentificacion tipoIdentificacion = new TipoIdentificacion()
            .nombre(UPDATED_NOMBRE);
        return tipoIdentificacion;
    }

    @BeforeEach
    public void initTest() {
        tipoIdentificacionRepository.deleteAll();
        tipoIdentificacion = createEntity();
    }

    @Test
    public void createTipoIdentificacion() throws Exception {
        int databaseSizeBeforeCreate = tipoIdentificacionRepository.findAll().size();
        // Create the TipoIdentificacion
        TipoIdentificacionDTO tipoIdentificacionDTO = tipoIdentificacionMapper.toDto(tipoIdentificacion);
        restTipoIdentificacionMockMvc.perform(post("/api/tipo-identificacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoIdentificacionDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoIdentificacion in the database
        List<TipoIdentificacion> tipoIdentificacionList = tipoIdentificacionRepository.findAll();
        assertThat(tipoIdentificacionList).hasSize(databaseSizeBeforeCreate + 1);
        TipoIdentificacion testTipoIdentificacion = tipoIdentificacionList.get(tipoIdentificacionList.size() - 1);
        assertThat(testTipoIdentificacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the TipoIdentificacion in Elasticsearch
        verify(mockTipoIdentificacionSearchRepository, times(1)).save(testTipoIdentificacion);
    }

    @Test
    public void createTipoIdentificacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoIdentificacionRepository.findAll().size();

        // Create the TipoIdentificacion with an existing ID
        tipoIdentificacion.setId("existing_id");
        TipoIdentificacionDTO tipoIdentificacionDTO = tipoIdentificacionMapper.toDto(tipoIdentificacion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoIdentificacionMockMvc.perform(post("/api/tipo-identificacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoIdentificacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoIdentificacion in the database
        List<TipoIdentificacion> tipoIdentificacionList = tipoIdentificacionRepository.findAll();
        assertThat(tipoIdentificacionList).hasSize(databaseSizeBeforeCreate);

        // Validate the TipoIdentificacion in Elasticsearch
        verify(mockTipoIdentificacionSearchRepository, times(0)).save(tipoIdentificacion);
    }


    @Test
    public void getAllTipoIdentificacions() throws Exception {
        // Initialize the database
        tipoIdentificacionRepository.save(tipoIdentificacion);

        // Get all the tipoIdentificacionList
        restTipoIdentificacionMockMvc.perform(get("/api/tipo-identificacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoIdentificacion.getId())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    public void getTipoIdentificacion() throws Exception {
        // Initialize the database
        tipoIdentificacionRepository.save(tipoIdentificacion);

        // Get the tipoIdentificacion
        restTipoIdentificacionMockMvc.perform(get("/api/tipo-identificacions/{id}", tipoIdentificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoIdentificacion.getId()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }
    @Test
    public void getNonExistingTipoIdentificacion() throws Exception {
        // Get the tipoIdentificacion
        restTipoIdentificacionMockMvc.perform(get("/api/tipo-identificacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTipoIdentificacion() throws Exception {
        // Initialize the database
        tipoIdentificacionRepository.save(tipoIdentificacion);

        int databaseSizeBeforeUpdate = tipoIdentificacionRepository.findAll().size();

        // Update the tipoIdentificacion
        TipoIdentificacion updatedTipoIdentificacion = tipoIdentificacionRepository.findById(tipoIdentificacion.getId()).get();
        updatedTipoIdentificacion
            .nombre(UPDATED_NOMBRE);
        TipoIdentificacionDTO tipoIdentificacionDTO = tipoIdentificacionMapper.toDto(updatedTipoIdentificacion);

        restTipoIdentificacionMockMvc.perform(put("/api/tipo-identificacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoIdentificacionDTO)))
            .andExpect(status().isOk());

        // Validate the TipoIdentificacion in the database
        List<TipoIdentificacion> tipoIdentificacionList = tipoIdentificacionRepository.findAll();
        assertThat(tipoIdentificacionList).hasSize(databaseSizeBeforeUpdate);
        TipoIdentificacion testTipoIdentificacion = tipoIdentificacionList.get(tipoIdentificacionList.size() - 1);
        assertThat(testTipoIdentificacion.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the TipoIdentificacion in Elasticsearch
        verify(mockTipoIdentificacionSearchRepository, times(1)).save(testTipoIdentificacion);
    }

    @Test
    public void updateNonExistingTipoIdentificacion() throws Exception {
        int databaseSizeBeforeUpdate = tipoIdentificacionRepository.findAll().size();

        // Create the TipoIdentificacion
        TipoIdentificacionDTO tipoIdentificacionDTO = tipoIdentificacionMapper.toDto(tipoIdentificacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoIdentificacionMockMvc.perform(put("/api/tipo-identificacions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoIdentificacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoIdentificacion in the database
        List<TipoIdentificacion> tipoIdentificacionList = tipoIdentificacionRepository.findAll();
        assertThat(tipoIdentificacionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoIdentificacion in Elasticsearch
        verify(mockTipoIdentificacionSearchRepository, times(0)).save(tipoIdentificacion);
    }

    @Test
    public void deleteTipoIdentificacion() throws Exception {
        // Initialize the database
        tipoIdentificacionRepository.save(tipoIdentificacion);

        int databaseSizeBeforeDelete = tipoIdentificacionRepository.findAll().size();

        // Delete the tipoIdentificacion
        restTipoIdentificacionMockMvc.perform(delete("/api/tipo-identificacions/{id}", tipoIdentificacion.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoIdentificacion> tipoIdentificacionList = tipoIdentificacionRepository.findAll();
        assertThat(tipoIdentificacionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TipoIdentificacion in Elasticsearch
        verify(mockTipoIdentificacionSearchRepository, times(1)).deleteById(tipoIdentificacion.getId());
    }

    @Test
    public void searchTipoIdentificacion() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tipoIdentificacionRepository.save(tipoIdentificacion);
        when(mockTipoIdentificacionSearchRepository.search(queryStringQuery("id:" + tipoIdentificacion.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tipoIdentificacion), PageRequest.of(0, 1), 1));

        // Search the tipoIdentificacion
        restTipoIdentificacionMockMvc.perform(get("/api/_search/tipo-identificacions?query=id:" + tipoIdentificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoIdentificacion.getId())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
}
