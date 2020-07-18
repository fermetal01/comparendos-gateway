package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Servicio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Servicio} entity.
 */
public interface ServicioSearchRepository extends ElasticsearchRepository<Servicio, String> {
}
