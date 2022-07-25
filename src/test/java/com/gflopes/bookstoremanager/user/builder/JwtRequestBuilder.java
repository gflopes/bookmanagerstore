package com.gflopes.bookstoremanager.user.builder;

import com.gflopes.bookstoremanager.jwt.dto.JwtRequest;
import lombok.Builder;

@Builder
public class JwtRequestBuilder {

    @Builder.Default
    private String username = "rodrigo";

    @Builder.Default
    private String password = "123456";

    public JwtRequest buildJwtRequest() {
        return new JwtRequest(username, password);
    }
}
