package br.com.felipelautenschlager.kn.router;

import br.com.felipelautenschlager.kn.router.model.Point;
import br.com.felipelautenschlager.kn.router.model.Route;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GpsPointsParserProcessor implements ItemProcessor<Route, List<Point>> {

    public static final String GPS_POINTS_REGEXP = "(\\[([0-9]+.[0-9]+), ([0-9]+.[0-9]+), ([0-9]+), ([0-9]+.[0-9]+))\\]";

    @Override
    public List<Point> process(Route route) throws Exception {
        List<Point> points = new ArrayList<>();

        Pattern regexp = Pattern.compile(GPS_POINTS_REGEXP);
        Matcher matcher = regexp.matcher(route.getPoints());
        Point point;
        while (matcher.find()) {
            point = new Point();
            MatchResult result = matcher.toMatchResult();

            // Set route key to the point
            point.setId(route.getId());
            point.setFromSeq(route.getFromSeq());
            point.setToSeq(route.getToSeq());

            // Set the point details
            point.setLatitude(Double.parseDouble(result.group(2)));
            point.setLongitude(Double.parseDouble(result.group(3)));
            point.setTimestamp(new Date(Long.parseLong(result.group(4))));
            point.setKnots(Double.parseDouble(result.group(5)));

            points.add(point);
        }

        return points;
    }
}
