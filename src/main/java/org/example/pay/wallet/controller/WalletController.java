package org.example.pay.wallet.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "지갑 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {
}
