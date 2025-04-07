package com.farmdora.farmdoraproductmanagement.service;
import com.farmdora.farmdoraproductmanagement.dto.OptionDto;
import com.farmdora.farmdoraproductmanagement.dto.SaleFileDto;
import com.farmdora.farmdoraproductmanagement.dto.SaleRequestDto;
import com.farmdora.farmdoraproductmanagement.entity.*;
import com.farmdora.farmdoraproductmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleFileRepository saleFileRepository;
    private final OptionRepository optionRepository;
    private final SellerRepository sellerRepository;
    private final OptionTypeRepository optionTypeRepository;

    // 생성자 주입
    public SaleService(SaleRepository saleRepository,
                       SaleFileRepository saleFileRepository,
                       OptionRepository optionRepository,
                       SellerRepository sellerRepository,
                       OptionTypeRepository optionTypeRepository) {
        this.saleRepository = saleRepository;
        this.saleFileRepository = saleFileRepository;
        this.optionRepository = optionRepository;
        this.sellerRepository = sellerRepository;
        this.optionTypeRepository = optionTypeRepository;
    }

    public Integer createSale(SaleRequestDto requestDto) {
        // 1. Seller 엔티티 조회
        Seller seller = sellerRepository.findById(requestDto.getSellerId())
                .orElseThrow(() -> new RuntimeException("판매자를 찾을 수 없습니다."));

        // 2. Sale 엔티티 생성 및 저장
        Sale sale = Sale.builder()
                .seller(seller)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .origin(requestDto.getOrigin())
                .isBlind(false)  // 초기값 설정
                .build();

        Sale savedSale = saleRepository.save(sale);

        // 3. SaleFile 엔티티들 생성 및 저장
        if (requestDto.getFiles() != null) {
            for (SaleFileDto fileDto : requestDto.getFiles()) {
                SaleFile saleFile = SaleFile.builder()
                        .sale(savedSale)
                        .saveFile(fileDto.getSaveFile())
                        .originFile(fileDto.getOriginFile())
                        .isMain(fileDto.isMain())
                        .build();

                saleFileRepository.save(saleFile);
            }
        }

        // 4. Option 엔티티들 생성 및 저장
        if (requestDto.getOptions() != null) {
            for (OptionDto optionDto : requestDto.getOptions()) {
                // OptionType 조회
                OptionType optionType = optionTypeRepository.findById(optionDto.getTypeId())
                        .orElseThrow(() -> new RuntimeException("옵션 타입을 찾을 수 없습니다."));

                Options option = Options.builder()
                        .sale(savedSale)
                        .type(optionType)
                        .name(optionDto.getName())
                        .price(optionDto.getPrice())
                        .quantity(optionDto.getQuantity())
                        .isStop(false)  // 초기값 설정
                        .build();

                optionRepository.save(option);
            }
        }

        return savedSale.getId();
    }
}