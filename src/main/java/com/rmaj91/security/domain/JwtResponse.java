package com.rmaj91.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtResponse {

    private String accessToken;
    private String tokenType;
    private Date expiredAt;
}
