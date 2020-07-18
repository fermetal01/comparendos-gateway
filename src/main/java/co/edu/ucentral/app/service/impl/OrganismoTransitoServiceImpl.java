package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.OrganismoTransitoService;
import co.edu.ucentral.app.domain.OrganismoTransito;
import co.edu.ucentral.app.repository.OrganismoTransitoRepository;
import co.edu.ucentral.app.repository.search.OrganismoTransitoSearchRepository;
import co.edu.ucentral.app.service.dto.OrganismoTransitoDTO;
import co.edu.ucentral.app.service.mapper.OrganismoTransitoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link OrganismoTransito}.
 */
@Service
public class OrganismoTransitoServiceImpl implements OrganismoTransitoService {

    private final Logger log = LoggerFactory.getLogger(OrganismoTransitoServiceImpl.class);

    private final OrganismoTransitoRepository organismoTransitoRepository;

    private final OrganismoTransitoMapper organismoTransitoMapper;

    private final OrganismoTransitoSearchRepository organismoTransitoSearchRepository;

    public OrganismoTransitoServiceImpl(OrganismoTransitoRepository organismoTransitoRepository, OrganismoTransitoMapper organismoTransitoMapper, OrganismoTransitoSearchRepository organismoTransitoSearchRepository) {
        this.organismoTransitoRepository = organismoTransitoRepository;
        this.organismoTransitoMapper = organismoTransitoMapper;
        this.organismoTransitoSearchRepository = organismoTransitoSearchRepository;
    }

    @Override
    public OrganismoTransitoDTO save(OrganismoTransitoDTO organismoTransitoDTO) {
        log.debug("Request to save OrganismoTransito : {}", organismoTransitoDTO);
        OrganismoTransito organismoTransito = organismoTransitoMapper.toEntity(organismoTransitoDTO);
        organismoTransito = organismoTransitoRepository.save(organismoTransito);
        OrganismoTransitoDTO result = organismoTransitoMapper.toDto(organismoTransito);
        organismoTransitoSearchRepository.save(organismoTransito);
        return result;
    }

    @Override
    public Page<OrganismoTransitoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrganismoTransitos");
        return organismoTransitoRepository.findAll(pageable)
            .map(organismoTransitoMapper::toDto);
    }


    @Override
    public Optional<OrganismoTransitoDTO> findOne(String id) {
        log.debug("Request to get OrganismoTransito : {}", id);
        return organismoTransitoRepository.findById(id)
            .map(organismoTransitoMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete OrganismoTransito : {}", id);
        organismoTransitoRepository.deleteById(id);
        organismoTransitoSearchRepository.deleteById(id);
    }

    @Override
    public Page<OrganismoTransitoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrganismoTransitos for query {}", query);
        return organismoTransitoSearchRepository.search(queryStringQuery(query), pageable)
            .map(organismoTransitoMapper::toDto);
    }
}
