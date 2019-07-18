package com.learn.spring.batchintegration.demo;

import com.learn.spring.batchintegration.demo.domain.TestData;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
public class TestDataFieldMapper implements FieldSetMapper<TestData>{
    @Override
    public TestData mapFieldSet(FieldSet fieldSet) throws BindException {
        final TestData testData = new TestData();
        testData.test1 = fieldSet.readString(0);
        testData.test2 = fieldSet.readString(1);
        return testData;
    }
}
