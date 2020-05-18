package com.bytecode.springml.repository;

import com.bytecode.springml.model.RepSeguro;
import com.bytecode.springml.model.Seguro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SeguroRep {
    public List<Seguro> findAll(Pageable pageable);
    public List<RepSeguro> findDescribe(Pageable pageable);
}
