package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Comparendo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Comparendo} entity.
 */
public interface ComparendoSearchRepository extends ElasticsearchRepository<Comparendo, String> {
}
