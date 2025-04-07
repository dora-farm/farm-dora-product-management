package com.farmdora.farmdoraproductmanagement.product;

import com.farmdora.farmdoraproductmanagement.common.response.HttpResponse;
import com.farmdora.farmdoraproductmanagement.dto.SaleFileDto;
import com.farmdora.farmdoraproductmanagement.dto.SaleRequestDto;
import com.farmdora.farmdoraproductmanagement.service.SaleService;
import com.farmdora.farmdoraproductmanagement.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/my/seller/product")
public class SaleController {

    private final SaleService saleService;
    private final StorageService storageService;

    public SaleController(SaleService saleService, StorageService storageService) {
        this.saleService = saleService;
        this.storageService = storageService;
    }

    @PostMapping(value="add")
    public HttpResponse addProduct(
            @RequestPart("productData") String productDataStr,
            @RequestPart("files") List<MultipartFile> files) throws IOException {

        // JSON 문자열을 DTO 객체로 직접 변환
        SaleRequestDto requestDto =
                new ObjectMapper().readValue(productDataStr, SaleRequestDto.class);

        ArrayList<SaleFileDto> fileList = new ArrayList<>();
        boolean isFirstFile = false; // 첫 번째 파일 여부를 추적하는 플래그, 0이 메인

        for (MultipartFile part : files) {
            if (part.getSize() == 0) {
                continue;
            }
            String filename = UUID.randomUUID().toString();
            storageService.upload("product/" + filename, part.getInputStream());

            SaleFileDto attachedFile = new SaleFileDto();
            attachedFile.setSaveFile(filename);
            attachedFile.setOriginFile(part.getOriginalFilename());
            attachedFile.setMain(isFirstFile); // 첫 번째 파일만 false, 나머지는 true
            isFirstFile = true; // 플래그를 true로 변경하여 이후 파일은 모두 isMain=true(1)가 되도록 함

            fileList.add(attachedFile);
        }

        requestDto.setFiles(fileList);

        Integer saleId = saleService.createSale(requestDto);

        //입력 성공 시
        return HttpResponse.builder()
                .status(200)
                .message("저장 성공")
                .data("")
                .build();
    }

}
