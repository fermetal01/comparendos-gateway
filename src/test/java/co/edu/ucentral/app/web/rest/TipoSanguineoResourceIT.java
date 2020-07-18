package co.edu.ucentral.app.web.rest;

import co.edu.ucentral.app.ComparendosApp;
import co.edu.ucentral.app.domain.TipoSanguineo;
import co.edu.ucentral.app.repository.TipoSanguineoRepository;
import co.edu.ucentral.app.repository.search.TipoSanguineoSearchRepository;
import co.edu.ucentral.app.service.TipoSanguineoService;
import co.edu.ucentral.app.service.dto.TipoSanguineoDTO;
import co.edu.ucentral.app.service.mapper.TipoSanguineoMapper;

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
 * Integration tests for the {@link TipoSanguineoResource} REST controller.
 */
@SpringBootTest(classes = ComparendosApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TipoSanguineoResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    @Autowired
    private TipoSanguineoRepository tipoSanguineoRepository;

    @Autowired
    private TipoSanguineoMapper tipoSanguineoMapper;

    @Autowired
    private TipoSanguineoService tipoSanguineoService;

    /**
     * This repository is mocked in the co.edu.ucentral.app.repository.search test package.
     *
     * @see co.edu.ucentral.app.repository.search.TipoSanguineoSearchRepositoryMockConfiguration
     */
    @Autowired
    private TipoSanguineoSearchRepository mockTipoSanguineoSearchRepository;

    @Autowired
    private MockMvc restTipoSanguineoMockMvc;

    private TipoSanguineo tipoSanguineo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoSanguineo createEntity() {
        TipoSanguineo tipoSanguineo = new TipoSanguineo()
            .tipo(DEFAULT_TIPO);
        return tipoSanguineo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoSanguineo createUpdatedEntity() {
        TipoSanguineo tipoSanguineo = new TipoSanguineo()
            .tipo(UPDATED_TIPO);
        return tipoSanguineo;
    }

    @BeforeEach
    public void initTest() {
        tipoSanguineoRepository.deleteAll();
        tipoSanguineo = createEntity();
    }

    @Test
    public void createTipoSanguineo() throws Exception {
        int databaseSizeBeforeCreate = tipoSanguineoRepository.findAll().size();
        // Create the TipoSanguineo
        TipoSanguineoDTO tipoSanguineoDTO = tipoSanguineoMapper.toDto(tipoSanguineo);
        restTipoSanguineoMockMvc.perform(post("/api/tipo-sanguineos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoSanguineoDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoSanguineo in the database
        List<TipoSanguineo> tipoSanguineoList = tipoSanguineoRepository.findAll();
        assertThat(tipoSanguineoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoSanguineo testTipoSanguineo = tipoSanguineoList.get(tipoSanguineoList.size() - 1);
        assertThat(testTipoSanguineo.getTipo()).isEqualTo(DEFAULT_TIPO);

        // Validate the TipoSanguineo in Elasticsearch
        verify(mockTipoSanguineoSearchRepository, times(1)).save(testTipoSanguineo);
    }

    @Test
    public void createTipoSanguineoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoSanguineoRepository.findAll().size();

        // Create the TipoSanguineo with an existing ID
        tipoSanguineo.setId("existing_id");
        TipoSanguineoDTO tipoSanguineoDTO = tipoSanguineoMapper.toDto(tipoSanguineo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoSanguineoMockMvc.perform(post("/api/tipo-sanguineos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoSanguineoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoSanguineo in the database
        List<TipoSanguineo> tipoSanguineoList = tipoSanguineoRepository.findAll();
        assertThat(tipoSanguineoList).hasSize(databaseSizeBeforeCreate);

        // Validate the TipoSanguineo in Elasticsearch
        verify(mockTipoSanguineoSearchRepository, times(0)).save(tipoSanguineo);
    }


    @Test
    public void getAllTipoSanguineos() throws Exception {
        // Initialize the database
        tipoSanguineoRepository.save(tipoSanguineo);

        // Get all the tipoSanguineoList
        restTipoSanguineoMockMvc.perform(get("/api/tipo-sanguineos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoSanguineo.getId())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }
    
    @Test
    public void getTipoSanguineo() throws Exception {
        // Initialize the database
        tipoSanguineoRepository.save(tipoSanguineo);

        // Get the tipoSanguineo
        restTipoSanguineoMockMvc.perform(get("/api/tipo-sanguineos/{id}", tipoSanguineo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoSanguineo.getId()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO));
    }
    @Test
    public void getNonExistingTipoSanguineo() throws Exception {
        // Get the tipoSanguineo
        restTipoSanguineoMockMvc.perform(get("/api/tipo-sanguineos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTipoSanguineo() throws Exception {
        // Initialize the database
        tipoSanguineoRepository.save(tipoSanguineo);

        int databaseSizeBeforeUpdate = tipoSanguineoRepository.findAll().size();

        // Update the tipoSanguineo
        TipoSanguineo updatedTipoSanguineo = tipoSanguineoRepository.findById(tipoSanguineo.getId()).get();
        updatedTipoSanguineo
            .tipo(UPDATED_TIPO);
        TipoSanguineoDTO tipoSanguineoDTO = tipoSanguineoMapper.toDto(updatedTipoSanguineo);

        restTipoSanguineoMockMvc.perform(put("/api/tipo-sanguineos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoSanguineoDTO)))
            .andExpect(status().isOk());

        // Validate the TipoSanguineo in the database
        List<TipoSanguineo> tipoSanguineoList = tipoSanguineoRepository.findAll();
        assertThat(tipoSanguineoList).hasSize(databaseSizeBeforeUpdate);
        TipoSanguineo testTipoSanguineo = tipoSanguineoList.get(tipoSanguineoList.size() - 1);
        assertThat(testTipoSanguineo.getTipo()).isEqualTo(UPDATED_TIPO);

        // Validate the TipoSanguineo in Elasticsearch
        verify(mockTipoSanguineoSearchRepository, times(1)).save(testTipoSanguineo);
    }

    @Test
    public void updateNonExistingTipoSanguineo() throws Exception {
        int databaseSizeBeforeUpdate = tipoSanguineoRepository.findAll().size();

        // Create the TipoSanguineo
        TipoSanguineoDTO tipoSanguineoDTO = tipoSanguineoMapper.toDto(tipoSanguineo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoSanguineoMockMvc.perform(put("/api/tipo-sanguineos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tipoSanguineoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoSanguineo in the database
        List<TipoSanguineo> tipoSanguineoList = tipoSanguineoRepository.findAll();
        assertThat(tipoSanguineoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoSanguineo in Elasticsearch
        verify(mockTipoSanguineoSearchRepository, times(0)).save(tipoSanguineo);
    }

    @Test
    public void deleteTipoSanguineo() throws Exception {
        // Initialize the database
        tipoSanguineoRepository.save(tipoSanguineo);

        int databaseSizeBeforeDelete = tipoSanguineoRepository.findAll().size();

        // Delete the tipoSanguineo
        restTipoSanguineoMockMvc.perform(delete("/api/tipo-sanguineos/{id}", tipoSanguineo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoSanguineo> tipoSanguineoList = tipoSanguineoRepository.findAll();
        assertThat(tipoSanguineoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TipoSanguineo in Elasticsearch
        verify(mockTipoSanguineoSearchRepository, times(1)).deleteById(tipoSanguineo.getId());
    }

    @Test
    public void searchTipoSanguineo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tipoSanguineoRepository.save(tipoSanguineo);
        when(mockTipoSanguineoSearchRepository.search(queryStringQuery("id:" + tipoSanguineo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tipoSanguineo), PageRequest.of(0, 1), 1));

        // Search the tipoSanguineo
        restTipoSanguineoMockMvc.perform(get("/api/_search/tipo-sanguineos?query=id:" + tipoSanguineo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoSanguineo.getId())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }
}
