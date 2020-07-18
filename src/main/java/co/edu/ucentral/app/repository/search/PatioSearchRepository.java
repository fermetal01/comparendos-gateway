package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Patio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Patio} entity.
 */
public interface PatioSearchRepository extends ElasticsearchRepository<Patio, String> {
}
