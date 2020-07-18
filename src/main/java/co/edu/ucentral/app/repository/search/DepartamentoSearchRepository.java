package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.Departamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Departamento} entity.
 */
public interface DepartamentoSearchRepository extends ElasticsearchRepository<Departamento, String> {
}
