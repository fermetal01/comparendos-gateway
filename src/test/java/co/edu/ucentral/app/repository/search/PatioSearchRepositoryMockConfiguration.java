package co.edu.ucentral.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PatioSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PatioSearchRepositoryMockConfiguration {

    @MockBean
    private PatioSearchRepository mockPatioSearchRepository;

}
