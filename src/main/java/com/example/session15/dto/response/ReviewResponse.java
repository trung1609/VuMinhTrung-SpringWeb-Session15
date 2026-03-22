package com.example.session15.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private String email;
    private Integer rating;
    private String comment;
    private LocalDateTime createdDate;
}
