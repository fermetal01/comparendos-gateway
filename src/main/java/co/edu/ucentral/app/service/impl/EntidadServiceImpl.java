package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.EntidadService;
import co.edu.ucentral.app.domain.Entidad;
import co.edu.ucentral.app.repository.EntidadRepository;
import co.edu.ucentral.app.repository.search.EntidadSearchRepository;
import co.edu.ucentral.app.service.dto.EntidadDTO;
import co.edu.ucentral.app.service.mapper.EntidadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Entidad}.
 */
@Service
public class EntidadServiceImpl implements EntidadService {

    private final Logger log = LoggerFactory.getLogger(EntidadServiceImpl.class);

    private final EntidadRepository entidadRepository;

    private final EntidadMapper entidadMapper;

    private final EntidadSearchRepository entidadSearchRepository;

    public EntidadServiceImpl(EntidadRepository entidadRepository, EntidadMapper entidadMapper, EntidadSearchRepository entidadSearchRepository) {
        this.entidadRepository = entidadRepository;
        this.entidadMapper = entidadMapper;
        this.entidadSearchRepository = entidadSearchRepository;
    }

    @Override
    public EntidadDTO save(EntidadDTO entidadDTO) {
        log.debug("Request to save Entidad : {}", entidadDTO);
        Entidad entidad = entidadMapper.toEntity(entidadDTO);
        entidad = entidadRepository.save(entidad);
        EntidadDTO result = entidadMapper.toDto(entidad);
        entidadSearchRepository.save(entidad);
        return result;
    }

    @Override
    public Page<EntidadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Entidads");
        return entidadRepository.findAll(pageable)
            .map(entidadMapper::toDto);
    }


    @Override
    public Optional<EntidadDTO> findOne(String id) {
        log.debug("Request to get Entidad : {}", id);
        return entidadRepository.findById(id)
            .map(entidadMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Entidad : {}", id);
        entidadRepository.deleteById(id);
        entidadSearchRepository.deleteById(id);
    }

    @Override
    public Page<EntidadDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Entidads for query {}", query);
        return entidadSearchRepository.search(queryStringQuery(query), pageable)
            .map(entidadMapper::toDto);
    }
}
