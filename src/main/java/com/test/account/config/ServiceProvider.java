package com.test.account.config;

import com.test.account.model.external.provider.ApiProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "service.api")
public class ServiceProvider {
    private List<ApiProvider> providers;

    public List<ApiProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<ApiProvider> providers) {
        this.providers = providers;
    }
}
