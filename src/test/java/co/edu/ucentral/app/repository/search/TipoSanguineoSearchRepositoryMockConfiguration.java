package co.edu.ucentral.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TipoSanguineoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TipoSanguineoSearchRepositoryMockConfiguration {

    @MockBean
    private TipoSanguineoSearchRepository mockTipoSanguineoSearchRepository;

}
