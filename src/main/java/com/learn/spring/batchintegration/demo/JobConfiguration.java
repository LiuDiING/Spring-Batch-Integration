package com.learn.spring.batchintegration.demo;

import com.learn.spring.batchintegration.demo.domain.TestData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.learn.spring.batchintegration.demo")
public class JobConfiguration {

    @Autowired
    private FieldSetMapper fieldSetMapper;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    @Qualifier("csvJob")
    public Job job(@Qualifier("readWriteCsvStep") final Step step) {
        return jobs.get("csvReaderImport").repository(jobRepository).start(step).build();
    }

    @Bean
    @Qualifier("readWriteCsvStep")
    public Step step(ItemReader<TestData> reader, ItemWriter<TestData> writer) {
        return steps.get("readWriteCsvStep")
                .<TestData, TestData> chunk(1000)
                .reader(reader)
                .writer(writer)
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<TestData> reader(@Value("#{jobParameters['fileName']}") final String fileLocation) {
        final FlatFileItemReader<TestData> flatFileItemReader = new FlatFileItemReader<>();
        final DefaultLineMapper<TestData> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("test1", "test2");
        flatFileItemReader.setResource(new FileSystemResource(fileLocation));
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        flatFileItemReader.setLineMapper(lineMapper);
        return flatFileItemReader;
    }

    //TODO write this later
    @Bean
    public ItemWriter<TestData> itemWriter() {
        ItemWriter<TestData> itemWriter =
                new ItemStreamWriter<TestData>() {
                    @Override
                    public void open(ExecutionContext executionContext) throws ItemStreamException {

                    }

                    @Override
                    public void update(ExecutionContext executionContext) throws ItemStreamException {

                    }

                    @Override
                    public void close() throws ItemStreamException {

                    }

                    @Override
                    public void write(List<? extends TestData> list) throws Exception {

                    }
                };
        return itemWriter;
    }
}
