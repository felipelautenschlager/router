package br.com.felipelautenschlager.kn.router;

import br.com.felipelautenschlager.kn.router.model.RawRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("JOB COMPLETED.");

            List<RawRoute> results = jdbcTemplate.query("SELECT id, from_seq, to_seq, from_port, to_port, " +
                    "leg_duration, count FROM routes", new RowMapper<RawRoute>() {
                @Override
                public RawRoute mapRow(ResultSet resultSet, int i) throws SQLException {
                    RawRoute result = new RawRoute();
                    result.setId(resultSet.getString(1));
                    result.setFromSeq(resultSet.getInt(2));
                    result.setToSeq(resultSet.getInt(3));
                    result.setFromPort(resultSet.getString(4));
                    result.setToPort(resultSet.getString(5));
                    result.setLegDuration(resultSet.getLong(6));
                    result.setCount(resultSet.getInt(7));

                    return result;
                }
            });

            for (RawRoute r : results) {
                LOGGER.info("Found " + r);
            }

        }
    }
}
