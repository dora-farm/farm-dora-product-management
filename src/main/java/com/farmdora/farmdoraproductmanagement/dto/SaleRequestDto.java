package com.farmdora.farmdoraproductmanagement.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequestDto {
    // Sale 정보
    private Integer sellerId;
    private String title;
    private String content;
    private String origin;
    private List<SaleFileDto> files;
    private List<OptionDto> options;
}