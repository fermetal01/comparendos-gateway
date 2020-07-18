package co.edu.ucentral.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ClaseVehiculoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ClaseVehiculoSearchRepositoryMockConfiguration {

    @MockBean
    private ClaseVehiculoSearchRepository mockClaseVehiculoSearchRepository;

}
