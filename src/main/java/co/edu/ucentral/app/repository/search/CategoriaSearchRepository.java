package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Categoria;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Categoria} entity.
 */
public interface CategoriaSearchRepository extends ElasticsearchRepository<Categoria, String> {
}
