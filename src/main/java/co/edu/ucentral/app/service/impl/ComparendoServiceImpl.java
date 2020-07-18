package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.ComparendoService;
import co.edu.ucentral.app.domain.Comparendo;
import co.edu.ucentral.app.repository.ComparendoRepository;
import co.edu.ucentral.app.repository.search.ComparendoSearchRepository;
import co.edu.ucentral.app.service.dto.ComparendoDTO;
import co.edu.ucentral.app.service.mapper.ComparendoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Comparendo}.
 */
@Service
public class ComparendoServiceImpl implements ComparendoService {

    private final Logger log = LoggerFactory.getLogger(ComparendoServiceImpl.class);

    private final ComparendoRepository comparendoRepository;

    private final ComparendoMapper comparendoMapper;

    private final ComparendoSearchRepository comparendoSearchRepository;

    public ComparendoServiceImpl(ComparendoRepository comparendoRepository, ComparendoMapper comparendoMapper, ComparendoSearchRepository comparendoSearchRepository) {
        this.comparendoRepository = comparendoRepository;
        this.comparendoMapper = comparendoMapper;
        this.comparendoSearchRepository = comparendoSearchRepository;
    }

    @Override
    public ComparendoDTO save(ComparendoDTO comparendoDTO) {
        log.debug("Request to save Comparendo : {}", comparendoDTO);
        Comparendo comparendo = comparendoMapper.toEntity(comparendoDTO);
        comparendo = comparendoRepository.save(comparendo);
        ComparendoDTO result = comparendoMapper.toDto(comparendo);
        comparendoSearchRepository.save(comparendo);
        return result;
    }

    @Override
    public Page<ComparendoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comparendos");
        return comparendoRepository.findAll(pageable)
            .map(comparendoMapper::toDto);
    }


    public Page<ComparendoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return comparendoRepository.findAllWithEagerRelationships(pageable).map(comparendoMapper::toDto);
    }

    @Override
    public Optional<ComparendoDTO> findOne(String id) {
        log.debug("Request to get Comparendo : {}", id);
        return comparendoRepository.findOneWithEagerRelationships(id)
            .map(comparendoMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Comparendo : {}", id);
        comparendoRepository.deleteById(id);
        comparendoSearchRepository.deleteById(id);
    }

    @Override
    public Page<ComparendoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Comparendos for query {}", query);
        return comparendoSearchRepository.search(queryStringQuery(query), pageable)
            .map(comparendoMapper::toDto);
    }
}
