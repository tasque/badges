package org.badges.config;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
public class QuartzConfiguration {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Value("${quartz.config}") Resource config,
                                                     DataSource dataSource, JobFactory jobFactory) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        factoryBean.setConfigLocation(config);
        factoryBean.setJobFactory(jobFactory);
        factoryBean.setDataSource(dataSource);
        factoryBean.setWaitForJobsToCompleteOnShutdown(true);
        factoryBean.setAutoStartup(true);

        return factoryBean;
    }


    @Bean
    public JobFactory jobFactory(ApplicationContext context) {
        return (bundle, scheduler) -> context.getBean(bundle.getJobDetail().getJobClass());
    }
}
