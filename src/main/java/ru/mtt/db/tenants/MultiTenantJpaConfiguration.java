package ru.mtt.db.tenants;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import ru.mtt.db.tenants.conf.DBConfiguration;
import ru.mtt.db.tenants.conf.DBConfigurationLoader;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
@EnableConfigurationProperties({JpaProperties.class})
@EnableJpaRepositories(basePackages = {"ru.mtt"},
        transactionManagerRef = "TransactionManager",
        entityManagerFactoryRef = "EntityManagerFactory")
public class MultiTenantJpaConfiguration {
    private final String PACKAGE_SCAN = "ru.mtt";

    @Autowired
    private JpaProperties jpaProperties;


    @Bean(name = "MultiTenantConnectionProvider")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new TenantBasedDataSourceProvider();
    }

    @Bean(name = "CurrentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean(name = "Tenants2DataSources")
    public Map<String, DataSource> tenants2DataSources() {
        @NotNull HashMap<String, DBConfiguration> dbConfiguration = DBConfigurationLoader.load("tenants.json");
        Map<String, DataSource> datasourceMap = new HashMap<>();

        for (DBConfiguration cfg : dbConfiguration.values()) {
            DataSourceBuilder<HikariDataSource> dataSourceBuilder = DataSourceBuilder.create().type(HikariDataSource.class);
            dataSourceBuilder.driverClassName(cfg.getDataSourceClassName()).url(cfg.getUrl()).username(cfg.getUser()).password(cfg.getPassword());
            datasourceMap.put(cfg.getName(), dataSourceBuilder.build());
        }
        return datasourceMap;
    }

    @PersistenceContext
    @Primary
    @Bean(name = "EntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("MultiTenantConnectionProvider") MultiTenantConnectionProvider multiTenantConnectionProvider, @Qualifier("CurrentTenantIdentifierResolver") CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> hibernateProps = new LinkedHashMap<>(this.jpaProperties.getProperties());
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        // No dataSource is set to resulting EntityManagerFactoryBean
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(PACKAGE_SCAN);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(hibernateProps);
        return em;
    }

    @Bean(name = "EntityManagerFactory")
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }


    @Bean(name = "TransactionManager")
    public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpa = new JpaTransactionManager();
        jpa.setEntityManagerFactory(entityManagerFactory);
        return jpa;
    }

}