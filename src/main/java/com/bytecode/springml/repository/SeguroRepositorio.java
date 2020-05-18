package com.bytecode.springml.repository;

import com.bytecode.springml.model.RepSeguro;
import com.bytecode.springml.model.Seguro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeguroRepositorio implements SeguroRep{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Seguro> findAll(Pageable pageable) {
        return mongoTemplate.find(new Query().with(pageable), Seguro.class);
    }

    @Override
    public List<RepSeguro> findDescribe(Pageable pageable) {
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.project("age", "charges", "bmi", "children")
                        .and(ConditionalOperators.Cond.when(
                                ComparisonOperators.Eq.valueOf("sex").equalTo("male")
                        ).then(0).otherwise(1)).as("sex")
                        .and(ConditionalOperators.Cond.when(
                                ComparisonOperators.Eq.valueOf("smoker").equalTo("yes")
                        ).then(1).otherwise(0)).as("smoker")
        ), "Seguro", RepSeguro.class)
                .getMappedResults();
    }
}
