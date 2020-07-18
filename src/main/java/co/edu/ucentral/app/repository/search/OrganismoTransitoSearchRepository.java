package co.edu.ucentral.app.repository.search;

import co.edu.ucentral.app.domain.OrganismoTransito;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link OrganismoTransito} entity.
 */
public interface OrganismoTransitoSearchRepository extends ElasticsearchRepository<OrganismoTransito, String> {
}
