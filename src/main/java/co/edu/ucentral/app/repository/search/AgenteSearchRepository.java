package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Agente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Agente} entity.
 */
public interface AgenteSearchRepository extends ElasticsearchRepository<Agente, String> {
}
