package com.learn.spring.batchintegration.demo;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
public class FileMessageToJobRequest {

    @Autowired
    private JobRegistry jobRegistry;

    @Transformer(inputChannel = "fileInputChannel", outputChannel = "jobChannel")
    public JobLaunchRequest toRequest(File file) throws NoSuchJobException {
        final Job job = jobRegistry.getJob("csvJob");
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("fileName", file.getAbsolutePath()).addDate("date", new Date());
        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }

}
