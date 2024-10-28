package org.example.issue_coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "org.example.common",
        "org.example.redis",
        "org.example.coupon",
        "org.example.issue_coupon"
})
public class IssueCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssueCouponApplication.class, args);
    }

}
