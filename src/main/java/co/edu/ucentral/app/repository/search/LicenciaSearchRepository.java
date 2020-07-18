package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Licencia;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Licencia} entity.
 */
public interface LicenciaSearchRepository extends ElasticsearchRepository<Licencia, String> {
}
