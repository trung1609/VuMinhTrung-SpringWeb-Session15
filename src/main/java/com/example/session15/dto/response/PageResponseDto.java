package com.example.session15.dto.response;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseDto <T>{
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;
}
