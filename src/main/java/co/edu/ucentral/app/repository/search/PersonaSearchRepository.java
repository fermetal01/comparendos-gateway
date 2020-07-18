package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Persona;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Persona} entity.
 */
public interface PersonaSearchRepository extends ElasticsearchRepository<Persona, String> {
}
