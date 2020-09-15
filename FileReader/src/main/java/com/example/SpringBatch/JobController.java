package com.example.SpringBatch;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("fileDataJob")
	private Job fileDbJob;
	
	@GetMapping("/runJob")
	public String runJob() {
		JobParametersBuilder jb = new JobParametersBuilder();
		jb.addString("uuid", UUID.randomUUID().toString());
		
//		JobExecution jobExecution = null;
		
		try {
			jobLauncher.run(fileDbJob, jb.toJobParameters());
		} catch (Exception e) {
			return "failed";
		}
		
		return "ok";
	}
}
