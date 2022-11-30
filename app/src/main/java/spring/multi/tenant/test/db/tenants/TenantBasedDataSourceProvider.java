package spring.multi.tenant.test.db.tenants;


import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Map;

/**
 *
 * Tenants to Datasource provider impl
 */
public class TenantBasedDataSourceProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Autowired
    private Map<String, DataSource> Tenants2DataSources;

    @Override
    protected DataSource selectAnyDataSource() {
        return this.Tenants2DataSources.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantId) {
        return this.Tenants2DataSources.get(tenantId);
    }

}