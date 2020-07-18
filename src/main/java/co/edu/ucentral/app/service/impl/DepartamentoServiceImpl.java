package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.DepartamentoService;
import co.edu.ucentral.app.domain.Departamento;
import co.edu.ucentral.app.repository.DepartamentoRepository;
import co.edu.ucentral.app.repository.search.DepartamentoSearchRepository;
import co.edu.ucentral.app.service.dto.DepartamentoDTO;
import co.edu.ucentral.app.service.mapper.DepartamentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Departamento}.
 */
@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final Logger log = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private final DepartamentoRepository departamentoRepository;

    private final DepartamentoMapper departamentoMapper;

    private final DepartamentoSearchRepository departamentoSearchRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository, DepartamentoMapper departamentoMapper, DepartamentoSearchRepository departamentoSearchRepository) {
        this.departamentoRepository = departamentoRepository;
        this.departamentoMapper = departamentoMapper;
        this.departamentoSearchRepository = departamentoSearchRepository;
    }

    @Override
    public DepartamentoDTO save(DepartamentoDTO departamentoDTO) {
        log.debug("Request to save Departamento : {}", departamentoDTO);
        Departamento departamento = departamentoMapper.toEntity(departamentoDTO);
        departamento = departamentoRepository.save(departamento);
        DepartamentoDTO result = departamentoMapper.toDto(departamento);
        departamentoSearchRepository.save(departamento);
        return result;
    }

    @Override
    public Page<DepartamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Departamentos");
        return departamentoRepository.findAll(pageable)
            .map(departamentoMapper::toDto);
    }


    @Override
    public Optional<DepartamentoDTO> findOne(String id) {
        log.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id)
            .map(departamentoMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
        departamentoSearchRepository.deleteById(id);
    }

    @Override
    public Page<DepartamentoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Departamentos for query {}", query);
        return departamentoSearchRepository.search(queryStringQuery(query), pageable)
            .map(departamentoMapper::toDto);
    }
}
