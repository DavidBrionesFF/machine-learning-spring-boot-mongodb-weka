package com.bytecode.springml.controller;

import com.bytecode.springml.model.RepSeguro;
import com.bytecode.springml.model.Seguro;
import com.bytecode.springml.repository.SeguroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;

import java.util.List;

@RestController
@RequestMapping("/seguro")
public class SeguroController {
    @Autowired
    private SeguroRepositorio seguroRepositorio;

    @GetMapping("/findAll")
    public List<Seguro> findAll(Pageable pageable){
        return seguroRepositorio.findAll(pageable);
    }

    @GetMapping("/findDescribe")
    public List<RepSeguro> findDescribe(Pageable pageable){
        return seguroRepositorio.findDescribe(pageable);
    }

    @Autowired
    private LinearRegression linearRegression;

    @PostMapping("/predict")
    public RepSeguro predict(@RequestBody RepSeguro repSeguro) throws Exception {
        DenseInstance instance = new DenseInstance(6, new double[]{
                repSeguro.getAge(),
                repSeguro.getSex(),
                repSeguro.getBmi(),
                repSeguro.getChildren(),
                repSeguro.getSmoker()
        });
        repSeguro.setCharges(linearRegression.classifyInstance(instance));
        return repSeguro;
    }
}
