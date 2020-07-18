package co.edu.ucentral.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AgenteSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AgenteSearchRepositoryMockConfiguration {

    @MockBean
    private AgenteSearchRepository mockAgenteSearchRepository;

}
