package com.example.SpringBatch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.SpringBatch.model.FileData;

@Configuration
@EnableBatchProcessing
public class FileDataJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Value("${chunkSize}")
	private int chunkSize;
	
	private static final String INSERT_DATA = "INSERT INTO"+" SAMPLE_DATA(name, info)"+" VALUES (?, ?)";

	@Bean(name="fileDataJob")
	public Job fileDataJob(Step fileDataStep) {
		return jobBuilderFactory.get("fileDataJob").incrementer(new RunIdIncrementer()).flow(fileDataStep).end()
				.build();
	}

	@Bean
	public Step fileDataStep(FlatFileItemReader<FileData> reader, JdbcBatchItemWriter<FileData> writer) {
		return stepBuilderFactory
				.get("fileDataStep")
				.<FileData, FileData>chunk(chunkSize)
				.reader(reader)
				.writer(writer)
				.faultTolerant()
				.skipLimit(5)
				.skip(Exception.class)
				.build();
	}
	@Bean
	public JdbcBatchItemWriter<FileData> writer(DataSource dataSource) {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		JdbcBatchItemWriter<FileData> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setJdbcTemplate(jdbcTemplate);
		writer.setSql(INSERT_DATA);
		writer.setItemPreparedStatementSetter(new FileDataStatementSetter());
		return writer;
	}
	@Bean
	public FlatFileItemReader<FileData> fileReader() {
		FlatFileItemReader<FileData> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource("input/report_file.CSV"));

//		reader.setLineMapper(new DefaultLineMapper<FileData>() {
//			{
//
//				setLineTokenizer(new DelimitedLineTokenizer() {
//					{
//						setNames(new String[] { "name", "info" });
//						setDelimiter(",");
//					}
//				});
//
//				setFieldSetMapper(new BeanWrapperFieldSetMapper<FileData>() {
//					{
//						setTargetType(FileData.class);
//					}
//				});
//			}
//		});
		reader.setLineMapper(lineMapper());
		return reader;
	}
	@Bean
	public LineMapper<FileData> lineMapper(){
		DefaultLineMapper<FileData> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("name","info");
		lineTokenizer.setDelimiter(",");
		BeanWrapperFieldSetMapper<FileData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(FileData.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
}




