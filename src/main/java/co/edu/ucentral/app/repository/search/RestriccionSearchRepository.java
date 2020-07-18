package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Restriccion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Restriccion} entity.
 */
public interface RestriccionSearchRepository extends ElasticsearchRepository<Restriccion, String> {
}
