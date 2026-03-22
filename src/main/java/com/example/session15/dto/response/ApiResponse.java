package com.example.session15.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T>{
    private String message;
    private int status;
    private T data;
}
