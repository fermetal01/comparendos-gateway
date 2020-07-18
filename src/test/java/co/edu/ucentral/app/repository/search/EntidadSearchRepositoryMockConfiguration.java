package co.edu.ucentral.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EntidadSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EntidadSearchRepositoryMockConfiguration {

    @MockBean
    private EntidadSearchRepository mockEntidadSearchRepository;

}
