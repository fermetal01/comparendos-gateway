package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.LicenciaService;
import co.edu.ucentral.app.domain.Licencia;
import co.edu.ucentral.app.repository.LicenciaRepository;
import co.edu.ucentral.app.repository.search.LicenciaSearchRepository;
import co.edu.ucentral.app.service.dto.LicenciaDTO;
import co.edu.ucentral.app.service.mapper.LicenciaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Licencia}.
 */
@Service
public class LicenciaServiceImpl implements LicenciaService {

    private final Logger log = LoggerFactory.getLogger(LicenciaServiceImpl.class);

    private final LicenciaRepository licenciaRepository;

    private final LicenciaMapper licenciaMapper;

    private final LicenciaSearchRepository licenciaSearchRepository;

    public LicenciaServiceImpl(LicenciaRepository licenciaRepository, LicenciaMapper licenciaMapper, LicenciaSearchRepository licenciaSearchRepository) {
        this.licenciaRepository = licenciaRepository;
        this.licenciaMapper = licenciaMapper;
        this.licenciaSearchRepository = licenciaSearchRepository;
    }

    @Override
    public LicenciaDTO save(LicenciaDTO licenciaDTO) {
        log.debug("Request to save Licencia : {}", licenciaDTO);
        Licencia licencia = licenciaMapper.toEntity(licenciaDTO);
        licencia = licenciaRepository.save(licencia);
        LicenciaDTO result = licenciaMapper.toDto(licencia);
        licenciaSearchRepository.save(licencia);
        return result;
    }

    @Override
    public Page<LicenciaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Licencias");
        return licenciaRepository.findAll(pageable)
            .map(licenciaMapper::toDto);
    }


    public Page<LicenciaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return licenciaRepository.findAllWithEagerRelationships(pageable).map(licenciaMapper::toDto);
    }

    @Override
    public Optional<LicenciaDTO> findOne(String id) {
        log.debug("Request to get Licencia : {}", id);
        return licenciaRepository.findOneWithEagerRelationships(id)
            .map(licenciaMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Licencia : {}", id);
        licenciaRepository.deleteById(id);
        licenciaSearchRepository.deleteById(id);
    }

    @Override
    public Page<LicenciaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Licencias for query {}", query);
        return licenciaSearchRepository.search(queryStringQuery(query), pageable)
            .map(licenciaMapper::toDto);
    }
}
