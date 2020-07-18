package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.TipoIdentificacionService;
import co.edu.ucentral.app.domain.TipoIdentificacion;
import co.edu.ucentral.app.repository.TipoIdentificacionRepository;
import co.edu.ucentral.app.repository.search.TipoIdentificacionSearchRepository;
import co.edu.ucentral.app.service.dto.TipoIdentificacionDTO;
import co.edu.ucentral.app.service.mapper.TipoIdentificacionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TipoIdentificacion}.
 */
@Service
public class TipoIdentificacionServiceImpl implements TipoIdentificacionService {

    private final Logger log = LoggerFactory.getLogger(TipoIdentificacionServiceImpl.class);

    private final TipoIdentificacionRepository tipoIdentificacionRepository;

    private final TipoIdentificacionMapper tipoIdentificacionMapper;

    private final TipoIdentificacionSearchRepository tipoIdentificacionSearchRepository;

    public TipoIdentificacionServiceImpl(TipoIdentificacionRepository tipoIdentificacionRepository, TipoIdentificacionMapper tipoIdentificacionMapper, TipoIdentificacionSearchRepository tipoIdentificacionSearchRepository) {
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
        this.tipoIdentificacionMapper = tipoIdentificacionMapper;
        this.tipoIdentificacionSearchRepository = tipoIdentificacionSearchRepository;
    }

    @Override
    public TipoIdentificacionDTO save(TipoIdentificacionDTO tipoIdentificacionDTO) {
        log.debug("Request to save TipoIdentificacion : {}", tipoIdentificacionDTO);
        TipoIdentificacion tipoIdentificacion = tipoIdentificacionMapper.toEntity(tipoIdentificacionDTO);
        tipoIdentificacion = tipoIdentificacionRepository.save(tipoIdentificacion);
        TipoIdentificacionDTO result = tipoIdentificacionMapper.toDto(tipoIdentificacion);
        tipoIdentificacionSearchRepository.save(tipoIdentificacion);
        return result;
    }

    @Override
    public Page<TipoIdentificacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoIdentificacions");
        return tipoIdentificacionRepository.findAll(pageable)
            .map(tipoIdentificacionMapper::toDto);
    }


    @Override
    public Optional<TipoIdentificacionDTO> findOne(String id) {
        log.debug("Request to get TipoIdentificacion : {}", id);
        return tipoIdentificacionRepository.findById(id)
            .map(tipoIdentificacionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete TipoIdentificacion : {}", id);
        tipoIdentificacionRepository.deleteById(id);
        tipoIdentificacionSearchRepository.deleteById(id);
    }

    @Override
    public Page<TipoIdentificacionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoIdentificacions for query {}", query);
        return tipoIdentificacionSearchRepository.search(queryStringQuery(query), pageable)
            .map(tipoIdentificacionMapper::toDto);
    }
}
