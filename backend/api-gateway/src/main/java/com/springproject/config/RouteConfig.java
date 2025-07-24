package com.springproject.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path(
                        "/api/auth/**",
                        "/api/admin/user/**",
                        "/api/admin/user-information/**",
                        "/api/customer/user/**",
                        "/api/customer/user-information/**",
                        "/api/internal/user/**"
                ).uri("lb://USER-SERVICE"))
                .route("car-service", r -> r.path(
                        "/api/admin/brand/**",
                        "/api/admin/car/**",
                        "/api/admin/car-type/**",
                        "/api/admin/insurance/**",
                        "/api/admin/location/**",
                        "/api/admin/maintenance/**",
                        "/api/customer/car/**",
                        "/api/customer/location/**",
                        "/api/internal/car/**"
                ).uri("lb://CAR-SERVICE"))
                .route("booking-service", r -> r.path(
                        "/api/admin/booking/**",
                        "/api/admin/booking-history/**",
                        "/api/customer/booking/**",
                        "/api/customer/booking-history/**"
                ).uri("lb://BOOKING-SERVICE"))
                .route("payment-service", r -> r.path(
                        "/api/admin/payment/**",
                        "/api/admin/invoice/**",
                        "/api/stripe/**"
                ).uri("lb://PAYMENT-SERVICE"))
                .route("feedback-service", r -> r.path(
                        "/api/admin/feedback/**",
                        "/api/admin/rating-summary/**",
                        "/api/customer/feedback/**"
                ).uri("lb://FEEDBACK-SERVICE"))
                .route("chat-service", r -> r.path(
                        "/api/customer/conversation/**",
                        "/api/admin/conversation/**"
                ).uri("lb://CHAT-SERVICE"))
                .build();
    }

}
