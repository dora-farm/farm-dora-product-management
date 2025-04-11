package com.farmdora.farmdoraproductmanagement.dto;

import com.farmdora.farmdoraproductmanagement.entity.Options;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailDto {
    // Sale 정보
    private Integer id;
    private String title;
    private String content;
    private String origin;
    // options의 정보
    private String bigCategory; //OptionTypeBig의 name
    private String smallCategory;  // OptionType의 name
    private String mainImage; // SaleFile의 mainimage를 url로 변경하여 저장
    private List<String> detailImages;
    private List<OptionDto> options;
}