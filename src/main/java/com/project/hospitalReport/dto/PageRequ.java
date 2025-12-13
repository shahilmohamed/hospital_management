package com.project.hospitalReport.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class PageRequ {
    private Integer page;
    private Integer size;
    private String search;
    private LocalDate date;
}
