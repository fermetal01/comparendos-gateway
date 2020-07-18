package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.TipoSanguineo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link TipoSanguineo} entity.
 */
public interface TipoSanguineoSearchRepository extends ElasticsearchRepository<TipoSanguineo, String> {
}
