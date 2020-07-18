package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.AgenteService;
import co.edu.ucentral.app.domain.Agente;
import co.edu.ucentral.app.repository.AgenteRepository;
import co.edu.ucentral.app.repository.search.AgenteSearchRepository;
import co.edu.ucentral.app.service.dto.AgenteDTO;
import co.edu.ucentral.app.service.mapper.AgenteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Agente}.
 */
@Service
public class AgenteServiceImpl implements AgenteService {

    private final Logger log = LoggerFactory.getLogger(AgenteServiceImpl.class);

    private final AgenteRepository agenteRepository;

    private final AgenteMapper agenteMapper;

    private final AgenteSearchRepository agenteSearchRepository;

    public AgenteServiceImpl(AgenteRepository agenteRepository, AgenteMapper agenteMapper, AgenteSearchRepository agenteSearchRepository) {
        this.agenteRepository = agenteRepository;
        this.agenteMapper = agenteMapper;
        this.agenteSearchRepository = agenteSearchRepository;
    }

    @Override
    public AgenteDTO save(AgenteDTO agenteDTO) {
        log.debug("Request to save Agente : {}", agenteDTO);
        Agente agente = agenteMapper.toEntity(agenteDTO);
        agente = agenteRepository.save(agente);
        AgenteDTO result = agenteMapper.toDto(agente);
        agenteSearchRepository.save(agente);
        return result;
    }

    @Override
    public Page<AgenteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agentes");
        return agenteRepository.findAll(pageable)
            .map(agenteMapper::toDto);
    }


    @Override
    public Optional<AgenteDTO> findOne(String id) {
        log.debug("Request to get Agente : {}", id);
        return agenteRepository.findById(id)
            .map(agenteMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Agente : {}", id);
        agenteRepository.deleteById(id);
        agenteSearchRepository.deleteById(id);
    }

    @Override
    public Page<AgenteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Agentes for query {}", query);
        return agenteSearchRepository.search(queryStringQuery(query), pageable)
            .map(agenteMapper::toDto);
    }
}
