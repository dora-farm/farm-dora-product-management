package com.farmdora.farmdoraproductmanagement.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleRequestDto {
    // Sale 정보
    private Integer sellerId;
    private String title;
    private String content;
    private String origin;

    // SaleFile 정보
    private List<SaleFileDto> files;

    // Option 정보
    private List<OptionDto> options;

    @Getter
    @Setter
    public static class SaleFileDto {
        private String saveFile;
        private String originFile;
        private boolean isMain;
    }

    @Getter
    @Setter
    public static class OptionDto {
        private Integer typeId;  // OptionType의 ID
        private String name;
        private int price;
        private int quantity;
    }
}