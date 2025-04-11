package com.farmdora.farmdoraproductmanagement.dto;

import com.farmdora.farmdoraproductmanagement.entity.OptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionTypeDto {
    // OptionType 정보
    private Short typeId;
    private String typeName;

    // OptionTypeBig 정보
    private Short typeBigId;
    private String typeBigName;

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static OptionTypeDto from(OptionType optionType) {
        if (optionType == null) {
            return null;
        }

        OptionTypeDto dto = new OptionTypeDto();

        // OptionType 정보 설정
        dto.setTypeId(optionType.getId());
        dto.setTypeName(optionType.getName());

        // OptionTypeBig 정보 설정
        if (optionType.getOptionTypeBig() != null) {
            dto.setTypeBigId(optionType.getOptionTypeBig().getId());
            dto.setTypeBigName(optionType.getOptionTypeBig().getName());
        }

        return dto;
    }

    // 생성자를 이용한 변환 (대안 방법)
    public OptionTypeDto(OptionType optionType) {
        if (optionType != null) {
            this.typeId = optionType.getId();
            this.typeName = optionType.getName();

            if (optionType.getOptionTypeBig() != null) {
                this.typeBigId = optionType.getOptionTypeBig().getId();
                this.typeBigName = optionType.getOptionTypeBig().getName();
            }
        }
    }
}