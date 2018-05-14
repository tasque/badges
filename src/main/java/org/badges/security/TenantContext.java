package org.badges.security;

import org.badges.db.Company;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {

    private static final ThreadLocal<Company> currentTenant = new ThreadLocal<>();

    public Company getCurrentCompany() {
        return currentTenant.get();
    }

    void setCurrentTenant(Company tenant) {
        currentTenant.set(tenant);
    }

    void clear() {
        currentTenant.remove();
    }
}
