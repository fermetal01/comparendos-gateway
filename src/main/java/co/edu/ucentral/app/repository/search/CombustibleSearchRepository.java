package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Combustible;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Combustible} entity.
 */
public interface CombustibleSearchRepository extends ElasticsearchRepository<Combustible, String> {
}
