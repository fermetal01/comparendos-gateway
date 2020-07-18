package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Rango;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Rango} entity.
 */
public interface RangoSearchRepository extends ElasticsearchRepository<Rango, String> {
}
