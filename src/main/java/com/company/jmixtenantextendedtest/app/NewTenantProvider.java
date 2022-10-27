package com.company.jmixtenantextendedtest.app;

import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.multitenancy.core.AcceptsTenant;
import io.jmix.multitenancy.core.TenantProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Primary
@Component
public class NewTenantProvider implements TenantProvider {

    private final ObjectProvider<SessionData> sessionDataProvider;
    private final CurrentAuthentication currentAuthentication;

    public NewTenantProvider(ObjectProvider<SessionData> sessionDataProvider, CurrentAuthentication currentAuthentication) {
        this.sessionDataProvider = sessionDataProvider;
        this.currentAuthentication = currentAuthentication;
    }

    @Override
    public String getCurrentUserTenantId() {

        if (!currentAuthentication.isSet()) {
            return TenantProvider.NO_TENANT;
        }
        UserDetails userDetails = currentAuthentication.getUser();
        if (!(userDetails instanceof AcceptsTenant)) {
            return TenantProvider.NO_TENANT;
        }

        String tenantId = ((AcceptsTenant) userDetails).getTenantId();
        if (tenantId != null) {
            return tenantId;
        }

        //здесь выпадает исключение
        String account = (String) sessionDataProvider.getObject()
                .getAttribute("tenant");
        return account != null ? account : TenantProvider.NO_TENANT;
    }
}