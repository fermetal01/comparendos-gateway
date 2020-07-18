package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.RestriccionService;
import co.edu.ucentral.app.domain.Restriccion;
import co.edu.ucentral.app.repository.RestriccionRepository;
import co.edu.ucentral.app.repository.search.RestriccionSearchRepository;
import co.edu.ucentral.app.service.dto.RestriccionDTO;
import co.edu.ucentral.app.service.mapper.RestriccionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Restriccion}.
 */
@Service
public class RestriccionServiceImpl implements RestriccionService {

    private final Logger log = LoggerFactory.getLogger(RestriccionServiceImpl.class);

    private final RestriccionRepository restriccionRepository;

    private final RestriccionMapper restriccionMapper;

    private final RestriccionSearchRepository restriccionSearchRepository;

    public RestriccionServiceImpl(RestriccionRepository restriccionRepository, RestriccionMapper restriccionMapper, RestriccionSearchRepository restriccionSearchRepository) {
        this.restriccionRepository = restriccionRepository;
        this.restriccionMapper = restriccionMapper;
        this.restriccionSearchRepository = restriccionSearchRepository;
    }

    @Override
    public RestriccionDTO save(RestriccionDTO restriccionDTO) {
        log.debug("Request to save Restriccion : {}", restriccionDTO);
        Restriccion restriccion = restriccionMapper.toEntity(restriccionDTO);
        restriccion = restriccionRepository.save(restriccion);
        RestriccionDTO result = restriccionMapper.toDto(restriccion);
        restriccionSearchRepository.save(restriccion);
        return result;
    }

    @Override
    public Page<RestriccionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Restriccions");
        return restriccionRepository.findAll(pageable)
            .map(restriccionMapper::toDto);
    }


    @Override
    public Optional<RestriccionDTO> findOne(String id) {
        log.debug("Request to get Restriccion : {}", id);
        return restriccionRepository.findById(id)
            .map(restriccionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Restriccion : {}", id);
        restriccionRepository.deleteById(id);
        restriccionSearchRepository.deleteById(id);
    }

    @Override
    public Page<RestriccionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Restriccions for query {}", query);
        return restriccionSearchRepository.search(queryStringQuery(query), pageable)
            .map(restriccionMapper::toDto);
    }
}
