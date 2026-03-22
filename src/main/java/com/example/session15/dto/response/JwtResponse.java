package com.example.session15.dto.response;

import com.example.session15.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String username;
    private String accessToken;
    private String refreshToken;
    private final String type = "Bearer";
    private Date expire;

    @JsonIgnoreProperties({"password", "id"})
    private Users users;
}
