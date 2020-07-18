package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Infraccion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Infraccion} entity.
 */
public interface InfraccionSearchRepository extends ElasticsearchRepository<Infraccion, String> {
}
