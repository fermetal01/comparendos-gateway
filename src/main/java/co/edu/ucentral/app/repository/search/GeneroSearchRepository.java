package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Genero;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Genero} entity.
 */
public interface GeneroSearchRepository extends ElasticsearchRepository<Genero, String> {
}
