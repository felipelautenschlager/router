package br.com.felipelautenschlager.kn.router;

import br.com.felipelautenschlager.kn.router.model.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsoleItemWriter implements ItemWriter<Route> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleItemWriter.class);

    @Override
    public void write(List<? extends Route> list) throws Exception {
        LOG.info("--- BEST ROUTE FOUND ---");
        for (Route item : list) {
            LOG.info(item.toString());
        }
        LOG.info("------------------------");
    }
}
