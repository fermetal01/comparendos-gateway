package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.TipoIdentificacion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link TipoIdentificacion} entity.
 */
public interface TipoIdentificacionSearchRepository extends ElasticsearchRepository<TipoIdentificacion, String> {
}
