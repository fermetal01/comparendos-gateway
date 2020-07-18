package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.CombustibleService;
import co.edu.ucentral.app.domain.Combustible;
import co.edu.ucentral.app.repository.CombustibleRepository;
import co.edu.ucentral.app.repository.search.CombustibleSearchRepository;
import co.edu.ucentral.app.service.dto.CombustibleDTO;
import co.edu.ucentral.app.service.mapper.CombustibleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Combustible}.
 */
@Service
public class CombustibleServiceImpl implements CombustibleService {

    private final Logger log = LoggerFactory.getLogger(CombustibleServiceImpl.class);

    private final CombustibleRepository combustibleRepository;

    private final CombustibleMapper combustibleMapper;

    private final CombustibleSearchRepository combustibleSearchRepository;

    public CombustibleServiceImpl(CombustibleRepository combustibleRepository, CombustibleMapper combustibleMapper, CombustibleSearchRepository combustibleSearchRepository) {
        this.combustibleRepository = combustibleRepository;
        this.combustibleMapper = combustibleMapper;
        this.combustibleSearchRepository = combustibleSearchRepository;
    }

    @Override
    public CombustibleDTO save(CombustibleDTO combustibleDTO) {
        log.debug("Request to save Combustible : {}", combustibleDTO);
        Combustible combustible = combustibleMapper.toEntity(combustibleDTO);
        combustible = combustibleRepository.save(combustible);
        CombustibleDTO result = combustibleMapper.toDto(combustible);
        combustibleSearchRepository.save(combustible);
        return result;
    }

    @Override
    public Page<CombustibleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Combustibles");
        return combustibleRepository.findAll(pageable)
            .map(combustibleMapper::toDto);
    }


    @Override
    public Optional<CombustibleDTO> findOne(String id) {
        log.debug("Request to get Combustible : {}", id);
        return combustibleRepository.findById(id)
            .map(combustibleMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Combustible : {}", id);
        combustibleRepository.deleteById(id);
        combustibleSearchRepository.deleteById(id);
    }

    @Override
    public Page<CombustibleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Combustibles for query {}", query);
        return combustibleSearchRepository.search(queryStringQuery(query), pageable)
            .map(combustibleMapper::toDto);
    }
}
