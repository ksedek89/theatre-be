package pl.aswit.starter.e2e.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.util.FileCopyUtils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

import pl.aswit.starter.Application
import pl.aswit.starter.business.version.VersionService

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ContextConfiguration(initializers = [WireMockInitializer.class])
@TestPropertySource(properties = [
    //"PRU_HYDRA_ORDS_URL=http://0.0.0.0/", // FIXME example - to remove
    "SPRINGDOC_ENABLED=false",
    "APM_ENABLED=false",
    "app.version=1.1.2"
])
abstract class RestApiTestBase<T> extends Specification {
    @LocalServerPort
    int randomServerPort

    @Autowired
    ObjectMapper objectMapper

    // @Autowired
    // WireMockServer wireMockRuleHYDRA // FIXME example - to remove

    @Autowired
    VersionService versionService

    static class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            // WireMockRule wireMockHydraServer = new WireMockRule(new WireMockConfiguration().dynamicPort()) // FIXME example - to remove
            // wireMockHydraServer.start()
            //
            // configurableApplicationContext
            //         .getBeanFactory()
            //         .registerSingleton("wireMockRuleHYDRA", wireMockHydraServer)
            //
            // configurableApplicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            //             @Override
            //             void onApplicationEvent(ApplicationEvent event) {
            //                 if (event instanceof ContextClosedEvent) {
            //                     wireMockHydraServer.stop()
            //                 }
            //             }
            //         })
            //
            // TestPropertyValues.of(
            //         "PRU_HYDRA_ORDS_URL=" + "http://localhost:" + wireMockHydraServer.port() + "/hydra"
            //         ).applyTo(configurableApplicationContext.getEnvironment())
        }
    }

    T setupClient(Class<T> clientInterface) {
        def retrofit = new Retrofit.Builder()
                .baseUrl(String.format("http://localhost:%s/starter/", randomServerPort))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(new OkHttpClient().newBuilder().build()
                ).build()
        return retrofit.create(clientInterface)
    }

    protected static String readJson(String path) {
        def responseReader = new InputStreamReader(new ClassPathResource(path).getInputStream())
        return FileCopyUtils.copyToString(responseReader)
    }

    protected <T> T getResource(String path, TypeReference<T> ref) {
        def responseReader = new InputStreamReader(new ClassPathResource(path).getInputStream())
        return objectMapper.readValue(responseReader, ref)
    }
}
