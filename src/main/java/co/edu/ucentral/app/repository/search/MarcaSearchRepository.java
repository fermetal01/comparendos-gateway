package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Marca;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Marca} entity.
 */
public interface MarcaSearchRepository extends ElasticsearchRepository<Marca, String> {
}
