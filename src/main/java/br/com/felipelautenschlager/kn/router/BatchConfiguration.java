package br.com.felipelautenschlager.kn.router;

import br.com.felipelautenschlager.kn.router.model.Route;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public ItemReader<Route> reader() {
        FlatFileItemReader<Route> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("DEBRV_DEHAM_historical_routes.csv"));
        reader.setLineMapper(new DefaultLineMapper<Route>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] {"id", "from_seq", "to_seq", "from_port", "to_port", "leg_duration",
                         "count", "points"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Route>() {{
                setTargetType(Route.class);
            }});
        }});
        reader.setLinesToSkip(1);

        return reader;
    }

    @Bean
    public ItemWriter<Route> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Route> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Route>());
        writer.setSql("INSERT INTO routes (id, from_seq, to_seq, from_port, to_port, leg_duration, count, points) " +
                      "VALUES (:id, :fromSeq, :toSeq, :fromPort, :toPort, :legDuration, :count, :points)");
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean
    public Job readRoutesJob(JobBuilderFactory jobs, Step step1, JobExecutionListener listener) {
        return jobs.get("readRoutesJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory factory, ItemReader<Route> reader, ItemWriter<Route> writer) {
        return factory.get("step1")
                .<Route, Route>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }


}
