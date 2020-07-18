package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.ClaseVehiculo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ClaseVehiculo} entity.
 */
public interface ClaseVehiculoSearchRepository extends ElasticsearchRepository<ClaseVehiculo, String> {
}
