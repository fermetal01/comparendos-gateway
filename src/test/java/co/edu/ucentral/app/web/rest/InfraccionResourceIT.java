package co.edu.ucentral.app.web.rest;

import co.edu.ucentral.app.ComparendosApp;
import co.edu.ucentral.app.domain.Infraccion;
import co.edu.ucentral.app.repository.InfraccionRepository;
import co.edu.ucentral.app.repository.search.InfraccionSearchRepository;
import co.edu.ucentral.app.service.InfraccionService;
import co.edu.ucentral.app.service.dto.InfraccionDTO;
import co.edu.ucentral.app.service.mapper.InfraccionMapper;

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
 * Integration tests for the {@link InfraccionResource} REST controller.
 */
@SpringBootTest(classes = ComparendosApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InfraccionResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private InfraccionRepository infraccionRepository;

    @Autowired
    private InfraccionMapper infraccionMapper;

    @Autowired
    private InfraccionService infraccionService;

    /**
     * This repository is mocked in the co.edu.ucentral.app.repository.search test package.
     *
     * @see co.edu.ucentral.app.repository.search.InfraccionSearchRepositoryMockConfiguration
     */
    @Autowired
    private InfraccionSearchRepository mockInfraccionSearchRepository;

    @Autowired
    private MockMvc restInfraccionMockMvc;

    private Infraccion infraccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Infraccion createEntity() {
        Infraccion infraccion = new Infraccion()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION);
        return infraccion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Infraccion createUpdatedEntity() {
        Infraccion infraccion = new Infraccion()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);
        return infraccion;
    }

    @BeforeEach
    public void initTest() {
        infraccionRepository.deleteAll();
        infraccion = createEntity();
    }

    @Test
    public void createInfraccion() throws Exception {
        int databaseSizeBeforeCreate = infraccionRepository.findAll().size();
        // Create the Infraccion
        InfraccionDTO infraccionDTO = infraccionMapper.toDto(infraccion);
        restInfraccionMockMvc.perform(post("/api/infraccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infraccionDTO)))
            .andExpect(status().isCreated());

        // Validate the Infraccion in the database
        List<Infraccion> infraccionList = infraccionRepository.findAll();
        assertThat(infraccionList).hasSize(databaseSizeBeforeCreate + 1);
        Infraccion testInfraccion = infraccionList.get(infraccionList.size() - 1);
        assertThat(testInfraccion.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testInfraccion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);

        // Validate the Infraccion in Elasticsearch
        verify(mockInfraccionSearchRepository, times(1)).save(testInfraccion);
    }

    @Test
    public void createInfraccionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = infraccionRepository.findAll().size();

        // Create the Infraccion with an existing ID
        infraccion.setId("existing_id");
        InfraccionDTO infraccionDTO = infraccionMapper.toDto(infraccion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfraccionMockMvc.perform(post("/api/infraccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infraccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Infraccion in the database
        List<Infraccion> infraccionList = infraccionRepository.findAll();
        assertThat(infraccionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Infraccion in Elasticsearch
        verify(mockInfraccionSearchRepository, times(0)).save(infraccion);
    }


    @Test
    public void getAllInfraccions() throws Exception {
        // Initialize the database
        infraccionRepository.save(infraccion);

        // Get all the infraccionList
        restInfraccionMockMvc.perform(get("/api/infraccions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraccion.getId())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    public void getInfraccion() throws Exception {
        // Initialize the database
        infraccionRepository.save(infraccion);

        // Get the infraccion
        restInfraccionMockMvc.perform(get("/api/infraccions/{id}", infraccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infraccion.getId()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }
    @Test
    public void getNonExistingInfraccion() throws Exception {
        // Get the infraccion
        restInfraccionMockMvc.perform(get("/api/infraccions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateInfraccion() throws Exception {
        // Initialize the database
        infraccionRepository.save(infraccion);

        int databaseSizeBeforeUpdate = infraccionRepository.findAll().size();

        // Update the infraccion
        Infraccion updatedInfraccion = infraccionRepository.findById(infraccion.getId()).get();
        updatedInfraccion
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);
        InfraccionDTO infraccionDTO = infraccionMapper.toDto(updatedInfraccion);

        restInfraccionMockMvc.perform(put("/api/infraccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infraccionDTO)))
            .andExpect(status().isOk());

        // Validate the Infraccion in the database
        List<Infraccion> infraccionList = infraccionRepository.findAll();
        assertThat(infraccionList).hasSize(databaseSizeBeforeUpdate);
        Infraccion testInfraccion = infraccionList.get(infraccionList.size() - 1);
        assertThat(testInfraccion.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testInfraccion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);

        // Validate the Infraccion in Elasticsearch
        verify(mockInfraccionSearchRepository, times(1)).save(testInfraccion);
    }

    @Test
    public void updateNonExistingInfraccion() throws Exception {
        int databaseSizeBeforeUpdate = infraccionRepository.findAll().size();

        // Create the Infraccion
        InfraccionDTO infraccionDTO = infraccionMapper.toDto(infraccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraccionMockMvc.perform(put("/api/infraccions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infraccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Infraccion in the database
        List<Infraccion> infraccionList = infraccionRepository.findAll();
        assertThat(infraccionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Infraccion in Elasticsearch
        verify(mockInfraccionSearchRepository, times(0)).save(infraccion);
    }

    @Test
    public void deleteInfraccion() throws Exception {
        // Initialize the database
        infraccionRepository.save(infraccion);

        int databaseSizeBeforeDelete = infraccionRepository.findAll().size();

        // Delete the infraccion
        restInfraccionMockMvc.perform(delete("/api/infraccions/{id}", infraccion.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Infraccion> infraccionList = infraccionRepository.findAll();
        assertThat(infraccionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Infraccion in Elasticsearch
        verify(mockInfraccionSearchRepository, times(1)).deleteById(infraccion.getId());
    }

    @Test
    public void searchInfraccion() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        infraccionRepository.save(infraccion);
        when(mockInfraccionSearchRepository.search(queryStringQuery("id:" + infraccion.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(infraccion), PageRequest.of(0, 1), 1));

        // Search the infraccion
        restInfraccionMockMvc.perform(get("/api/_search/infraccions?query=id:" + infraccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraccion.getId())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
}
