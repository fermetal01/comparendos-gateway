package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Ciudad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Ciudad} entity.
 */
public interface CiudadSearchRepository extends ElasticsearchRepository<Ciudad, String> {
}
