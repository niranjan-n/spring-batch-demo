package com.example.SpringBatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

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
	@Autowired
	private JobRepository jobRepository;

	
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

	@Bean(name = "AsyncJobLauncher")	
	public JobLauncher asyncJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}


}
