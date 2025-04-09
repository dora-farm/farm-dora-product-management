package com.farmdora.farmdoraproductmanagement.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionDto {
    private Integer typeId;  // OptionType의 ID
    private String name;
    private int price;
    private int quantity;
}
