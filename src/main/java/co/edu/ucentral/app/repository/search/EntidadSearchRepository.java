package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Entidad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Entidad} entity.
 */
public interface EntidadSearchRepository extends ElasticsearchRepository<Entidad, String> {
}
