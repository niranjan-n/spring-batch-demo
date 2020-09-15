package com.example.SpringBatch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FirstTasklet implements Tasklet {
	
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("inside first tasklet");
		System.out.println("execute: start sleep!");
		Thread.sleep(10000);
		System.out.println("execute: end sleep!");
		throw new RuntimeException("job  execution failed");
	}

}
