package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.MarcaService;
import co.edu.ucentral.app.domain.Marca;
import co.edu.ucentral.app.repository.MarcaRepository;
import co.edu.ucentral.app.repository.search.MarcaSearchRepository;
import co.edu.ucentral.app.service.dto.MarcaDTO;
import co.edu.ucentral.app.service.mapper.MarcaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Marca}.
 */
@Service
public class MarcaServiceImpl implements MarcaService {

    private final Logger log = LoggerFactory.getLogger(MarcaServiceImpl.class);

    private final MarcaRepository marcaRepository;

    private final MarcaMapper marcaMapper;

    private final MarcaSearchRepository marcaSearchRepository;

    public MarcaServiceImpl(MarcaRepository marcaRepository, MarcaMapper marcaMapper, MarcaSearchRepository marcaSearchRepository) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
        this.marcaSearchRepository = marcaSearchRepository;
    }

    @Override
    public MarcaDTO save(MarcaDTO marcaDTO) {
        log.debug("Request to save Marca : {}", marcaDTO);
        Marca marca = marcaMapper.toEntity(marcaDTO);
        marca = marcaRepository.save(marca);
        MarcaDTO result = marcaMapper.toDto(marca);
        marcaSearchRepository.save(marca);
        return result;
    }

    @Override
    public Page<MarcaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Marcas");
        return marcaRepository.findAll(pageable)
            .map(marcaMapper::toDto);
    }


    @Override
    public Optional<MarcaDTO> findOne(String id) {
        log.debug("Request to get Marca : {}", id);
        return marcaRepository.findById(id)
            .map(marcaMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Marca : {}", id);
        marcaRepository.deleteById(id);
        marcaSearchRepository.deleteById(id);
    }

    @Override
    public Page<MarcaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Marcas for query {}", query);
        return marcaSearchRepository.search(queryStringQuery(query), pageable)
            .map(marcaMapper::toDto);
    }
}
