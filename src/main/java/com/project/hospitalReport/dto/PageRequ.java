package com.project.hospitalReport.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PageRequ {
    private Integer page;
    private Integer size;
    private String search;
}
