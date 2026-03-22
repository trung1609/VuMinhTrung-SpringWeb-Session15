package com.example.session15.dto.request;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDto {
    private Integer page;
    private Integer size;
    private String sort;
    private String direction;
}
