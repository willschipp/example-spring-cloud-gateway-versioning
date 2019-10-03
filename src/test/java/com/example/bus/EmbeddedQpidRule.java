package com.example.bus;

import org.apache.qpid.server.SystemLauncher;
import org.junit.rules.ExternalResource;

import java.util.HashMap;
import java.util.Map;

public class EmbeddedQpidRule extends ExternalResource {

    private final SystemLauncher broker = new SystemLauncher();

    @Override
    protected void before() throws Throwable {
        startQpidBroker();
    }

    @Override
    protected void after() {
        broker.shutdown();
    }

    private void startQpidBroker() throws Exception {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("type","Memory");
        attributes.put("initialConfigurationLocation",findResourcePath("qpid-config.json"));
        broker.startup(attributes);
    }

    private String findResourcePath(final String pathName) {
        return EmbeddedQpidRule.class.getClassLoader().getResource(pathName).toExternalForm();
    }
}

