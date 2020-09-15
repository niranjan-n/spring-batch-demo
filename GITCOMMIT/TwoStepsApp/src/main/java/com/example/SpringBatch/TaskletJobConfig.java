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
	private FirstTasklet firstTasklet;

	@Autowired
	private SecondTasklet secondTasklet;

	@Bean
	public Job TwoStepsJob() {
		System.out.println("TwoStepsJob() bean created");
		return jobBuilderFactory.get("2 steps Job").incrementer(new RunIdIncrementer()).flow(firstStep()).next(secondStep()).end()
				.build();
	}

	@Bean
	public Step firstStep() {
		System.out.println("firstStep() bean created");
		return stepBuilderFactory.get("taskletStep1").tasklet(firstTasklet).build();
	}
	@Bean
	public Step secondStep() {
		System.out.println("secondStep() bean created");
		return stepBuilderFactory.get("taskletStep2").tasklet(secondTasklet).build();
	}
	

}
