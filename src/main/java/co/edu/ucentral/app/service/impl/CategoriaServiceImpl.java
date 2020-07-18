package co.edu.ucentral.app.service.impl;

import co.edu.ucentral.app.service.CategoriaService;
import co.edu.ucentral.app.domain.Categoria;
import co.edu.ucentral.app.repository.CategoriaRepository;
import co.edu.ucentral.app.repository.search.CategoriaSearchRepository;
import co.edu.ucentral.app.service.dto.CategoriaDTO;
import co.edu.ucentral.app.service.mapper.CategoriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Categoria}.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    private final CategoriaMapper categoriaMapper;

    private final CategoriaSearchRepository categoriaSearchRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper, CategoriaSearchRepository categoriaSearchRepository) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
        this.categoriaSearchRepository = categoriaSearchRepository;
    }

    @Override
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        log.debug("Request to save Categoria : {}", categoriaDTO);
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        categoria = categoriaRepository.save(categoria);
        CategoriaDTO result = categoriaMapper.toDto(categoria);
        categoriaSearchRepository.save(categoria);
        return result;
    }

    @Override
    public Page<CategoriaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categorias");
        return categoriaRepository.findAll(pageable)
            .map(categoriaMapper::toDto);
    }


    @Override
    public Optional<CategoriaDTO> findOne(String id) {
        log.debug("Request to get Categoria : {}", id);
        return categoriaRepository.findById(id)
            .map(categoriaMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Categoria : {}", id);
        categoriaRepository.deleteById(id);
        categoriaSearchRepository.deleteById(id);
    }

    @Override
    public Page<CategoriaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Categorias for query {}", query);
        return categoriaSearchRepository.search(queryStringQuery(query), pageable)
            .map(categoriaMapper::toDto);
    }
}
