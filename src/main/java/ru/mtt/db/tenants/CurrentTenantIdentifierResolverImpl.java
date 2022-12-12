package ru.mtt.db.tenants;


import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    static String DEFAULT_TENANT = "main";

    @Override
    public String resolveCurrentTenantIdentifier() {
        if (TenantContext.getCurrentTenant() == null) {
            TenantContext.setCurrentTenant(DEFAULT_TENANT);
        }
        return TenantContext.getCurrentTenant();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}