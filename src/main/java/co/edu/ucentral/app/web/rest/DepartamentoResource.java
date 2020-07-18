package co.edu.ucentral.app.web.rest;

import co.edu.ucentral.app.service.DepartamentoService;
import co.edu.ucentral.app.web.rest.errors.BadRequestAlertException;
import co.edu.ucentral.app.service.dto.DepartamentoDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link co.edu.ucentral.app.domain.Departamento}.
 */
@RestController
@RequestMapping("/api")
public class DepartamentoResource {

    private final Logger log = LoggerFactory.getLogger(DepartamentoResource.class);

    private static final String ENTITY_NAME = "departamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartamentoService departamentoService;

    public DepartamentoResource(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * {@code POST  /departamentos} : Create a new departamento.
     *
     * @param departamentoDTO the departamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departamentoDTO, or with status {@code 400 (Bad Request)} if the departamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departamentos")
    public ResponseEntity<DepartamentoDTO> createDepartamento(@RequestBody DepartamentoDTO departamentoDTO) throws URISyntaxException {
        log.debug("REST request to save Departamento : {}", departamentoDTO);
        if (departamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new departamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartamentoDTO result = departamentoService.save(departamentoDTO);
        return ResponseEntity.created(new URI("/api/departamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /departamentos} : Updates an existing departamento.
     *
     * @param departamentoDTO the departamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamentoDTO,
     * or with status {@code 400 (Bad Request)} if the departamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departamentos")
    public ResponseEntity<DepartamentoDTO> updateDepartamento(@RequestBody DepartamentoDTO departamentoDTO) throws URISyntaxException {
        log.debug("REST request to update Departamento : {}", departamentoDTO);
        if (departamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartamentoDTO result = departamentoService.save(departamentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departamentoDTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /departamentos} : get all the departamentos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departamentos in body.
     */
    @GetMapping("/departamentos")
    public ResponseEntity<List<DepartamentoDTO>> getAllDepartamentos(Pageable pageable) {
        log.debug("REST request to get a page of Departamentos");
        Page<DepartamentoDTO> page = departamentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /departamentos/:id} : get the "id" departamento.
     *
     * @param id the id of the departamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departamentos/{id}")
    public ResponseEntity<DepartamentoDTO> getDepartamento(@PathVariable String id) {
        log.debug("REST request to get Departamento : {}", id);
        Optional<DepartamentoDTO> departamentoDTO = departamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departamentoDTO);
    }

    /**
     * {@code DELETE  /departamentos/:id} : delete the "id" departamento.
     *
     * @param id the id of the departamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departamentos/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable String id) {
        log.debug("REST request to delete Departamento : {}", id);
        departamentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/departamentos?query=:query} : search for the departamento corresponding
     * to the query.
     *
     * @param query the query of the departamento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/departamentos")
    public ResponseEntity<List<DepartamentoDTO>> searchDepartamentos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Departamentos for query {}", query);
        Page<DepartamentoDTO> page = departamentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
