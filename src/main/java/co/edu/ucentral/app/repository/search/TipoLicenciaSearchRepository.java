package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.TipoLicencia;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link TipoLicencia} entity.
 */
public interface TipoLicenciaSearchRepository extends ElasticsearchRepository<TipoLicencia, String> {
}
