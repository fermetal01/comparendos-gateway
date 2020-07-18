package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Vehiculo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Vehiculo} entity.
 */
public interface VehiculoSearchRepository extends ElasticsearchRepository<Vehiculo, String> {
}
