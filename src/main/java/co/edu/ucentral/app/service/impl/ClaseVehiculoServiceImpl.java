package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.ClaseVehiculoService;
import co.edu.ucentral.app.domain.ClaseVehiculo;
import co.edu.ucentral.app.repository.ClaseVehiculoRepository;
import co.edu.ucentral.app.repository.search.ClaseVehiculoSearchRepository;
import co.edu.ucentral.app.service.dto.ClaseVehiculoDTO;
import co.edu.ucentral.app.service.mapper.ClaseVehiculoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ClaseVehiculo}.
 */
@Service
public class ClaseVehiculoServiceImpl implements ClaseVehiculoService {

    private final Logger log = LoggerFactory.getLogger(ClaseVehiculoServiceImpl.class);

    private final ClaseVehiculoRepository claseVehiculoRepository;

    private final ClaseVehiculoMapper claseVehiculoMapper;

    private final ClaseVehiculoSearchRepository claseVehiculoSearchRepository;

    public ClaseVehiculoServiceImpl(ClaseVehiculoRepository claseVehiculoRepository, ClaseVehiculoMapper claseVehiculoMapper, ClaseVehiculoSearchRepository claseVehiculoSearchRepository) {
        this.claseVehiculoRepository = claseVehiculoRepository;
        this.claseVehiculoMapper = claseVehiculoMapper;
        this.claseVehiculoSearchRepository = claseVehiculoSearchRepository;
    }

    @Override
    public ClaseVehiculoDTO save(ClaseVehiculoDTO claseVehiculoDTO) {
        log.debug("Request to save ClaseVehiculo : {}", claseVehiculoDTO);
        ClaseVehiculo claseVehiculo = claseVehiculoMapper.toEntity(claseVehiculoDTO);
        claseVehiculo = claseVehiculoRepository.save(claseVehiculo);
        ClaseVehiculoDTO result = claseVehiculoMapper.toDto(claseVehiculo);
        claseVehiculoSearchRepository.save(claseVehiculo);
        return result;
    }

    @Override
    public Page<ClaseVehiculoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClaseVehiculos");
        return claseVehiculoRepository.findAll(pageable)
            .map(claseVehiculoMapper::toDto);
    }


    @Override
    public Optional<ClaseVehiculoDTO> findOne(String id) {
        log.debug("Request to get ClaseVehiculo : {}", id);
        return claseVehiculoRepository.findById(id)
            .map(claseVehiculoMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ClaseVehiculo : {}", id);
        claseVehiculoRepository.deleteById(id);
        claseVehiculoSearchRepository.deleteById(id);
    }

    @Override
    public Page<ClaseVehiculoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ClaseVehiculos for query {}", query);
        return claseVehiculoSearchRepository.search(queryStringQuery(query), pageable)
            .map(claseVehiculoMapper::toDto);
    }
}
