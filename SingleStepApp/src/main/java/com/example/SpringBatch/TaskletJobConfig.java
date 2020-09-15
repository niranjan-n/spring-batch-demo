package com.example.SpringBatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class TaskletJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private HelloWorldTasklet helloWorldTasklet;

	@Bean
	public Job helloWorldJob() {
		System.out.println("helloworldJob() bean created");
		return jobBuilderFactory.get("helloWorldJob").incrementer(new RunIdIncrementer()).flow(singleStep()).end()
				.build();
	}

	@Bean
	public Step singleStep() {
		System.out.println("singleStep() bean created");
		return stepBuilderFactory.get("taskletStep").tasklet(helloWorldTasklet).build();
	}
	

}
