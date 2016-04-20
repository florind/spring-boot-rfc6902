package com.newsplore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)  //http://stackoverflow.com/questions/29880911/how-to-configure-auditing-via-java-config-in-spring-data-and-spring-data-rest
@Entity(name = "POLICY_BUNDLE")
@NoArgsConstructor
public class PolicyBundle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Embedded @Valid
    private Payment payment;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Embeddable
    public static class Payment {
        public enum PaymentFrequency {Monthly, Quarterly, Yearly}

        private String price;

        @NotNull private PaymentFrequency frequency;
    }
}
