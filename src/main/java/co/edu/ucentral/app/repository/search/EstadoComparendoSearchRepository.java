package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.EstadoComparendo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link EstadoComparendo} entity.
 */
public interface EstadoComparendoSearchRepository extends ElasticsearchRepository<EstadoComparendo, String> {
}
