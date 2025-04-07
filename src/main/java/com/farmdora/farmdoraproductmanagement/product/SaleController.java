package com.farmdora.farmdoraproductmanagement.product;

import com.farmdora.farmdoraproductmanagement.common.response.HttpResponse;
import com.farmdora.farmdoraproductmanagement.dto.SaleRequestDto;
import com.farmdora.farmdoraproductmanagement.service.SaleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my/seller/product")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }


    @PostMapping("add")
    public HttpResponse addProduct(@RequestBody SaleRequestDto requestDto) {

        Integer saleId = saleService.createSale(requestDto);

        return HttpResponse.builder()
//                .data(saleId)
                .build();
    }

}
