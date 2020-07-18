package co.edu.ucentral.app.web.rest;

import co.edu.ucentral.app.ComparendosApp;
import co.edu.ucentral.app.domain.Restriccion;
import co.edu.ucentral.app.repository.RestriccionRepository;
import co.edu.ucentral.app.repository.search.RestriccionSearchRepository;
import co.edu.ucentral.app.service.RestriccionService;
import co.edu.ucentral.app.service.dto.RestriccionDTO;
import co.edu.ucentral.app.service.mapper.RestriccionMapper;

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
 * Integration tests for the {@link RestriccionResource} REST controller.
 */
@SpringBootTest(classes = ComparendosApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RestriccionResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    @Autowired
    private RestriccionRepository restriccionRepository;

    @Autowired
    private RestriccionMapper restriccionMapper;

    @Autowired
    private RestriccionService restriccionService;

    /**
     * This repository is mocked in the co.edu.ucentral.app.repository.search test package.
     *
     * @see co.edu.ucentral.app.repository.search.RestriccionSearchRepositoryMockConfiguration
     */
    @Autowired
    private RestriccionSearchRepository mockRestriccionSearchRepository;

    @Autowired
    private MockMvc restRestriccionMockMvc;

    private Restriccion restriccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restriccion createEntity() {
        Restriccion restriccion = new Restriccion()
            .tipo(DEFAULT_TIPO);
        return restriccion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restriccion createUpdatedEntity() {
        Restriccion restriccion = new Restriccion()
            .tipo(UPDATED_TIPO);
        return restriccion;
    }

    @BeforeEach
    public void initTest() {
        restriccionRepository.deleteAll();
        restriccion = createEntity();
    }

    @Test
    public void createRestriccion() throws Exception {
        int databaseSizeBeforeCreate = restriccionRepository.findAll().size();
        // Create the Restriccion
        RestriccionDTO restriccionDTO = restriccionMapper.toDto(restriccion);
        restRestriccionMockMvc.perform(post("/api/restriccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restriccionDTO)))
            .andExpect(status().isCreated());

        // Validate the Restriccion in the database
        List<Restriccion> restriccionList = restriccionRepository.findAll();
        assertThat(restriccionList).hasSize(databaseSizeBeforeCreate + 1);
        Restriccion testRestriccion = restriccionList.get(restriccionList.size() - 1);
        assertThat(testRestriccion.getTipo()).isEqualTo(DEFAULT_TIPO);

        // Validate the Restriccion in Elasticsearch
        verify(mockRestriccionSearchRepository, times(1)).save(testRestriccion);
    }

    @Test
    public void createRestriccionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restriccionRepository.findAll().size();

        // Create the Restriccion with an existing ID
        restriccion.setId("existing_id");
        RestriccionDTO restriccionDTO = restriccionMapper.toDto(restriccion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestriccionMockMvc.perform(post("/api/restriccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restriccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Restriccion in the database
        List<Restriccion> restriccionList = restriccionRepository.findAll();
        assertThat(restriccionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Restriccion in Elasticsearch
        verify(mockRestriccionSearchRepository, times(0)).save(restriccion);
    }


    @Test
    public void getAllRestriccions() throws Exception {
        // Initialize the database
        restriccionRepository.save(restriccion);

        // Get all the restriccionList
        restRestriccionMockMvc.perform(get("/api/restriccions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restriccion.getId())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }
    
    @Test
    public void getRestriccion() throws Exception {
        // Initialize the database
        restriccionRepository.save(restriccion);

        // Get the restriccion
        restRestriccionMockMvc.perform(get("/api/restriccions/{id}", restriccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restriccion.getId()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO));
    }
    @Test
    public void getNonExistingRestriccion() throws Exception {
        // Get the restriccion
        restRestriccionMockMvc.perform(get("/api/restriccions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRestriccion() throws Exception {
        // Initialize the database
        restriccionRepository.save(restriccion);

        int databaseSizeBeforeUpdate = restriccionRepository.findAll().size();

        // Update the restriccion
        Restriccion updatedRestriccion = restriccionRepository.findById(restriccion.getId()).get();
        updatedRestriccion
            .tipo(UPDATED_TIPO);
        RestriccionDTO restriccionDTO = restriccionMapper.toDto(updatedRestriccion);

        restRestriccionMockMvc.perform(put("/api/restriccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restriccionDTO)))
            .andExpect(status().isOk());

        // Validate the Restriccion in the database
        List<Restriccion> restriccionList = restriccionRepository.findAll();
        assertThat(restriccionList).hasSize(databaseSizeBeforeUpdate);
        Restriccion testRestriccion = restriccionList.get(restriccionList.size() - 1);
        assertThat(testRestriccion.getTipo()).isEqualTo(UPDATED_TIPO);

        // Validate the Restriccion in Elasticsearch
        verify(mockRestriccionSearchRepository, times(1)).save(testRestriccion);
    }

    @Test
    public void updateNonExistingRestriccion() throws Exception {
        int databaseSizeBeforeUpdate = restriccionRepository.findAll().size();

        // Create the Restriccion
        RestriccionDTO restriccionDTO = restriccionMapper.toDto(restriccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestriccionMockMvc.perform(put("/api/restriccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(restriccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Restriccion in the database
        List<Restriccion> restriccionList = restriccionRepository.findAll();
        assertThat(restriccionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Restriccion in Elasticsearch
        verify(mockRestriccionSearchRepository, times(0)).save(restriccion);
    }

    @Test
    public void deleteRestriccion() throws Exception {
        // Initialize the database
        restriccionRepository.save(restriccion);

        int databaseSizeBeforeDelete = restriccionRepository.findAll().size();

        // Delete the restriccion
        restRestriccionMockMvc.perform(delete("/api/restriccions/{id}", restriccion.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restriccion> restriccionList = restriccionRepository.findAll();
        assertThat(restriccionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Restriccion in Elasticsearch
        verify(mockRestriccionSearchRepository, times(1)).deleteById(restriccion.getId());
    }

    @Test
    public void searchRestriccion() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        restriccionRepository.save(restriccion);
        when(mockRestriccionSearchRepository.search(queryStringQuery("id:" + restriccion.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(restriccion), PageRequest.of(0, 1), 1));

        // Search the restriccion
        restRestriccionMockMvc.perform(get("/api/_search/restriccions?query=id:" + restriccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restriccion.getId())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)));
    }
}
