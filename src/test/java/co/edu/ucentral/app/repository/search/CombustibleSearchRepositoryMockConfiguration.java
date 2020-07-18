package co.edu.ucentral.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CombustibleSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CombustibleSearchRepositoryMockConfiguration {

    @MockBean
    private CombustibleSearchRepository mockCombustibleSearchRepository;

}
