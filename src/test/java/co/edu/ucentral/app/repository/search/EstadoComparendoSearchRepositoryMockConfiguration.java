package co.edu.ucentral.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EstadoComparendoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EstadoComparendoSearchRepositoryMockConfiguration {

    @MockBean
    private EstadoComparendoSearchRepository mockEstadoComparendoSearchRepository;

}
