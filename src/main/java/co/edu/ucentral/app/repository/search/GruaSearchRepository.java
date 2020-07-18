package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Grua;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Grua} entity.
 */
public interface GruaSearchRepository extends ElasticsearchRepository<Grua, String> {
}
