package com.finance_ia.api.controller;

import com.finance_ia.api.service.OnnxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-onnx")
public class OnnxController {

    @Autowired
    private OnnxService onnxService;

    @GetMapping
    public String probarModelo() {
        return onnxService.ejecutarInferencia();
    }
}
