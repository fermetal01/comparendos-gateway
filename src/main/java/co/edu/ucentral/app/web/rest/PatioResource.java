package co.edu.ucentral.app.web.rest;

import co.edu.ucentral.app.service.PatioService;
import co.edu.ucentral.app.web.rest.errors.BadRequestAlertException;
import co.edu.ucentral.app.service.dto.PatioDTO;

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
 * REST controller for managing {@link co.edu.ucentral.app.domain.Patio}.
 */
@RestController
@RequestMapping("/api")
public class PatioResource {

    private final Logger log = LoggerFactory.getLogger(PatioResource.class);

    private static final String ENTITY_NAME = "patio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatioService patioService;

    public PatioResource(PatioService patioService) {
        this.patioService = patioService;
    }

    /**
     * {@code POST  /patios} : Create a new patio.
     *
     * @param patioDTO the patioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patioDTO, or with status {@code 400 (Bad Request)} if the patio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patios")
    public ResponseEntity<PatioDTO> createPatio(@RequestBody PatioDTO patioDTO) throws URISyntaxException {
        log.debug("REST request to save Patio : {}", patioDTO);
        if (patioDTO.getId() != null) {
            throw new BadRequestAlertException("A new patio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatioDTO result = patioService.save(patioDTO);
        return ResponseEntity.created(new URI("/api/patios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /patios} : Updates an existing patio.
     *
     * @param patioDTO the patioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patioDTO,
     * or with status {@code 400 (Bad Request)} if the patioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patios")
    public ResponseEntity<PatioDTO> updatePatio(@RequestBody PatioDTO patioDTO) throws URISyntaxException {
        log.debug("REST request to update Patio : {}", patioDTO);
        if (patioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PatioDTO result = patioService.save(patioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patioDTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /patios} : get all the patios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patios in body.
     */
    @GetMapping("/patios")
    public ResponseEntity<List<PatioDTO>> getAllPatios(Pageable pageable) {
        log.debug("REST request to get a page of Patios");
        Page<PatioDTO> page = patioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patios/:id} : get the "id" patio.
     *
     * @param id the id of the patioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patios/{id}")
    public ResponseEntity<PatioDTO> getPatio(@PathVariable String id) {
        log.debug("REST request to get Patio : {}", id);
        Optional<PatioDTO> patioDTO = patioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patioDTO);
    }

    /**
     * {@code DELETE  /patios/:id} : delete the "id" patio.
     *
     * @param id the id of the patioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patios/{id}")
    public ResponseEntity<Void> deletePatio(@PathVariable String id) {
        log.debug("REST request to delete Patio : {}", id);
        patioService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/patios?query=:query} : search for the patio corresponding
     * to the query.
     *
     * @param query the query of the patio search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/patios")
    public ResponseEntity<List<PatioDTO>> searchPatios(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Patios for query {}", query);
        Page<PatioDTO> page = patioService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
