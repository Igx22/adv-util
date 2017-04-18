package adv.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDistribution<T> {

    private class Event {
        public final T event;
        public final double relativeWeight;
        public Event(T event, double relativeWeight) {
            this.event = event;
            this.relativeWeight = relativeWeight;
        }
    }

    private double totalWeight = 0D;
    private List<Event> events = new ArrayList<>();
    private Random generator = new Random();

    public RandomDistribution(Random generator) {
        this.generator = generator;
    }

    public RandomDistribution() {
    }

    public void addEvent(T event, double relativeWeight) {
        events.add(new Event(event, relativeWeight));
        totalWeight += relativeWeight;
    }

    public T randomEvent() {
        double random = generator.nextDouble() * totalWeight;
        for (Event event : events) {
            random -= event.relativeWeight;
            if (random < 0D) {
                return event.event;
            }
        }
        // It's possible to get here due to rounding errors
        return events.get(events.size() - 1).event;
    }

}
