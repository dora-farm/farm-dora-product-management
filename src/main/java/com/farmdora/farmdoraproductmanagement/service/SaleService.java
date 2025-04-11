package com.farmdora.farmdoraproductmanagement.service;

import com.farmdora.farmdoraproductmanagement.dto.*;
import com.farmdora.farmdoraproductmanagement.entity.*;
import com.farmdora.farmdoraproductmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleFileRepository saleFileRepository;
    private final OptionRepository optionRepository;
    private final SellerRepository sellerRepository;
    private final OptionTypeRepository optionTypeRepository;

    private final StorageService storageService;

    // 생성자 주입
    public SaleService(SaleRepository saleRepository,
                       SaleFileRepository saleFileRepository,
                       OptionRepository optionRepository,
                       SellerRepository sellerRepository,
                       OptionTypeRepository optionTypeRepository,
                       StorageService storageService) {
        this.saleRepository = saleRepository;
        this.saleFileRepository = saleFileRepository;
        this.optionRepository = optionRepository;
        this.sellerRepository = sellerRepository;
        this.optionTypeRepository = optionTypeRepository;
        this.storageService = storageService;
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
                OptionType optionType = optionTypeRepository.findById(requestDto.getTypeId())
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

    // 판매글 ID로 user_id 조회
    public Integer getUserIdBySaleId(Integer saleId) {
        return saleRepository.findUserIdBySaleId(saleId);
    }

    public void deleteSale(Integer saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Sale ID입니다: " + saleId));

        List<SaleFile> saleFiles = saleFileRepository.findBySale(sale);

        //ObjectStorage의 파일 우선 제거
        for(SaleFile saleFile:saleFiles){
            storageService.delete("product/"+saleFile.getSaveFile());
        }
        //sale_file에 저장된 파일 정보 지우기
        saleFileRepository.deleteAll(saleFiles);
        //option에 저장된 정보 지우기
        List<Options> options = optionRepository.findBySale(sale);
        optionRepository.deleteAll(options);

        //sale에 저장된 정보 지우기
        saleRepository.delete(sale);
    }

    public SaleDetailDto getProductDetail(Integer productId) {
        Sale sale = saleRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Sale ID입니다: " + productId));

        List<SaleFile> saleFiles = saleFileRepository.findBySale(sale);
        List<Options> options = optionRepository.findBySale(sale);
        List<OptionDto> optionDtos = new ArrayList<>();
        String mainImage = null;
        List<String> detailImages = new ArrayList<>();

        for(SaleFile saleFile: saleFiles){
            if(!saleFile.isMain()){
                mainImage = storageService.getObjectStorageImageUrl(saleFile.getSaveFile());
            }
            else{
                detailImages.add(storageService.getObjectStorageImageUrl(saleFile.getSaveFile()));
            }
        }

        for (Options option : options) {
            OptionDto optionDto = OptionDto.builder()
                    .typeId(Integer.valueOf(option.getType().getId()))
                    .name(option.getName())
                    .price(option.getPrice())
                    .quantity(option.getQuantity())
                    .build();
            optionDtos.add(optionDto);
        }
        Integer typeId = optionDtos.get(0).getTypeId();
        OptionType optionType = optionTypeRepository.findByIdWithTypeBig(typeId).orElseThrow();
        OptionTypeDto optionTypeDto = OptionTypeDto.from(optionType);

        SaleDetailDto saleDetailDto = SaleDetailDto
                .builder()
                .id(sale.getId())
                .title(sale.getTitle())
                .content(sale.getContent())
                .origin(sale.getOrigin())
                .options(optionDtos)
                .bigCategory(optionTypeDto.getTypeBigName())
                .smallCategory(optionTypeDto.getTypeName())
                .mainImage(mainImage)
                .detailImages(detailImages)
                .build();

        return saleDetailDto;
    }
}