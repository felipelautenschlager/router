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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.sql.ResultSet;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    /* STEP 1 - READ ROUTES FROM FILE */

    @Bean(name = "step1Reader")
    public ItemReader<Route> readRoutesStep1Reader() {
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

    @Bean(name = "step1Writer")
    public ItemWriter<Route> readRoutesStep1Writer(DataSource dataSource) {
        JdbcBatchItemWriter<Route> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Route>());
        writer.setSql("INSERT INTO routes (id, from_seq, to_seq, from_port, to_port, leg_duration, count, points) " +
                      "VALUES (:id, :fromSeq, :toSeq, :fromPort, :toPort, :legDuration, :count, :points)");
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean(name = "step1")
    public Step step1(StepBuilderFactory factory, @Qualifier("step1Reader") ItemReader<Route> reader,
                      @Qualifier("step1Writer") ItemWriter<Route> writer) {
        return factory.get("step1")
                .<Route, Route>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

    /* STEP 1.2 - PARSE GPS POINTS */
    @Bean(name = "step2Reader")
    public ItemReader<Route> parseGPSPointsStep12Reader(DataSource dataSource) {
        JdbcCursorItemReader<Route> jdbcReader = new JdbcCursorItemReader<>();
        jdbcReader.setDataSource(dataSource);
        jdbcReader.setSql("SELECT id, from_seq, to_seq, points " +
                "FROM routes ORDER BY leg_duration ASC LIMIT 1");
        jdbcReader.setRowMapper((ResultSet resultSet, int i) -> {
            Route route = new Route();
            route.setId(resultSet.getString("id"));
            route.setFromSeq(resultSet.getInt("from_seq"));
            route.setToSeq(resultSet.getInt("to_seq"));
            route.setPoints(resultSet.getString("points"));

            return route;
        });


        return jdbcReader;
    }

    /* STEP 2 - FIND THE BEST ROUTE */

    @Bean(name = "step2Reader")
    public ItemReader<Route> findBestRouteStep2Reader(DataSource dataSource) {
        JdbcCursorItemReader<Route> jdbcReader = new JdbcCursorItemReader<>();
        jdbcReader.setDataSource(dataSource);
        jdbcReader.setSql("SELECT id, from_seq, to_seq, from_port, to_port, leg_duration, count, points " +
                          "FROM routes ORDER BY leg_duration ASC LIMIT 1");
        jdbcReader.setRowMapper((ResultSet resultSet, int i) -> {
            Route route = new Route();
            route.setId(resultSet.getString("id"));
            route.setFromSeq(resultSet.getInt("from_seq"));
            route.setToSeq(resultSet.getInt("to_seq"));
            route.setFromPort(resultSet.getString("from_port"));
            route.setToPort(resultSet.getString("to_port"));
            route.setLegDuration(resultSet.getLong("leg_duration"));
            route.setCount(resultSet.getInt("count"));
            route.setPoints(resultSet.getString("points"));

            return route;
        });


        return jdbcReader;
    }

    @Bean(name = "step2")
    public Step step2(StepBuilderFactory factory, @Qualifier("step2Reader") ItemReader<Route> reader,
                      ConsoleItemWriter writer) {
        return factory.get("step2")
                .<Route, Route>chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
    }

    /* JOB DEFINITION */

    @Bean
    public Job readRoutesJob(JobBuilderFactory jobs, @Qualifier("step1") Step step1,
                             @Qualifier("step2") Step step2, JobExecutionListener listener) {
        return jobs.get("readRoutesJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .next(step2)
                .end()
                .build();
    }


}
