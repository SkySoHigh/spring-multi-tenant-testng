package spring.multi.tenant.test.db.tenants;


import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    static String DEFAULT_TENANT = "main";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String currentTenant = TenantContext.getCurrentTenant();
        return currentTenant != null ? currentTenant : DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}