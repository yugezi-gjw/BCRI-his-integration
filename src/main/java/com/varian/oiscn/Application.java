package com.varian.oiscn;

import com.varian.oiscn.patient.integration.service.HISPatientQueryResource;
import com.varian.oiscn.patient.integration.service.HisPatientInfoService;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

@Slf4j
public class Application extends io.dropwizard.Application<Configuration> {

    public static final String CONFIG_HISINTEGRATION_YAML = "config\\HisSystem.yaml";

    public static void main(String[] args) {
        try {
            new Application().run("server", getHost(args));
        } catch (Exception e) {
            log.error("Application Exception: {}", e);
        }
    }

    protected static String getHost(String[] args) {
        if (args == null || args.length == 0) {
            return "config/local.yaml";
        } else {
            return args[0];
        }
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        log.info("Server: [{}] is starting ...", getName());
        try {
            registerComponents(configuration, environment);

            addCorsHeader(environment);
        } catch (Exception e) {
            log.error("Server Starting with Error: {}", e.getMessage());
            throw e;
        }
        HisPatientInfoService.init(CONFIG_HISINTEGRATION_YAML);
        log.info("Server: [{}] is started !", getName());
    }

    private void registerComponents(Configuration configuration, Environment environment) {
        environment.jersey().register(new HISPatientQueryResource(configuration, environment));
    }

    protected void addCorsHeader(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        setFilterRegistrationProperties(filter);

        FilterRegistration.Dynamic adminFilter = environment.admin().addFilter("CORS", CrossOriginFilter.class);
        setFilterRegistrationProperties(adminFilter);
    }

    protected void setFilterRegistrationProperties(FilterRegistration.Dynamic filter) {
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "*");
        filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS,HEAD");
        filter.setInitParameter("allowCredentials", "true");
    }
}
