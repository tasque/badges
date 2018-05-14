package org.badges;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DatasourceConfig {


    @Bean
    public DataSource dataSource() throws IOException {
        EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres();
        embeddedPostgres.start();

        return DataSourceBuilder.create()
                .url(embeddedPostgres.getConnectionUrl().get())
                .password("postgres")
                .username("postgres")
                .build();
    }
}
