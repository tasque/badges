package org.badges.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {


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
