package org.badges;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DatasourceConfig {


    @Bean
//    @Primary
    @Lazy
    public RestTemplate testRestTemplate(@LocalServerPort Integer port) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port)
                .requestFactory(requestFactory)
                .errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return false;
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {

                    }
                })
                .build();

        return restTemplate;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EmbeddedPostgres embeddedPostgres() {
        return new EmbeddedPostgres();
    }

    @Bean
    public DataSource dataSource(EmbeddedPostgres embeddedPostgres) {
        AbstractPostgresConfig.Credentials credentials = embeddedPostgres.getConfig().get().credentials();

        return DataSourceBuilder.create()
                .url(embeddedPostgres.getConnectionUrl().get())
                .password(credentials.password())
                .username(credentials.username())
                .build();
    }
}
