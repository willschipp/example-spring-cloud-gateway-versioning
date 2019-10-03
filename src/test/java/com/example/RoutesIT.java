package com.example;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RoutesIT {

    private static final Log logger = LogFactory.getLog(RoutesIT.class);

    @LocalServerPort
    protected int port = 0;

    WebTestClient webTestClient;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8001);

    @Before
    public void before() {
        logger.info("port: " + port);
        webTestClient = WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(10))
                .baseUrl("http://localhost:" + port)
                .build();
    }


    @Test
    public void test_API_Default() throws Exception {
        //setup a service so that SCG can access something
        wireMockRule.stubFor(get(urlPathMatching("/api/service"))
                .willReturn(aResponse()
                        .withStatus(200)
                )
        );
        //should get a redirect
        webTestClient.get().uri("/api/service")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueEquals("Location","/api/v1/service");

    }

    @Test
    public void test_API_Negative() throws Exception {
        //setup a service so that SCG can access something
        wireMockRule.stubFor(get(urlPathMatching("/api/service"))
                .willReturn(aResponse()
                        .withStatus(200)
                )
        );
        //should get a redirect
        webTestClient.get().uri("/hello")
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }
}
