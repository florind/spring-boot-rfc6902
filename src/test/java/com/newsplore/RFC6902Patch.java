package com.newsplore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsplore.domain.PolicyBundle;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
public class RFC6902Patch {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext context;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void patchAdd() throws Exception {
        PolicyBundle pb = new PolicyBundle();
        PolicyBundle.Payment payment = new PolicyBundle.Payment();
        payment.setPrice("EUR 19.99");
        payment.setFrequency(PolicyBundle.Payment.PaymentFrequency.Monthly);
        pb.setPayment(payment);

        String location = mockMvc.perform(post("/policyBundles")
            .content(new ObjectMapper().writeValueAsString(pb)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader(HttpHeaders.LOCATION);

        String newPrice = "EUR 19.00";
        payment.setPrice(newPrice);
        String newPriceJson = new ObjectMapper().writeValueAsString(payment);
        String updatePriceOp = "[{\"op\":\"replace\",\"path\":\"/payment\",\"value\":" + newPriceJson + "}]";

        mockMvc.perform(patch(location)
                .header(HttpHeaders.CONTENT_TYPE, "application/json-patch+json")
                .content(updatePriceOp))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("payment.price", Matchers.is(newPrice)));
    }
}
