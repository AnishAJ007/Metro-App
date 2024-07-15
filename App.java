import java.util.*;

public class MetroApp {
    public static void main(String[] args) {
        MetroGraph metroGraph = new MetroGraph();
        Scanner scanner = new Scanner(System.in);

        // Example usage
        System.out.println("Welcome to Mumbai Metro App!");
        System.out.print("Enter starting station: ");
        String startStation = scanner.nextLine();

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(metroGraph);
        dijkstra.shortestPath(startStation);

        System.out.print("Enter destination station: ");
        String destinationStation = scanner.nextLine();

        List<String> path = dijkstra.getPath(destinationStation);
        if (path != null) {
            System.out.println("Shortest path from " + startStation + " to " + destinationStation + ":");
            for (String station : path) {
                System.out.print(station + " -> ");
            }
            System.out.println("Reached in " + dijkstra.getDistance(destinationStation) + " minutes.");
        } else {
            System.out.println("No path found from " + startStation + " to " + destinationStation);
        }

        scanner.close();
    }
}

class MetroStation {
    private String name;

    public MetroStation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class MetroGraph {
    private Map<String, List<MetroStation>> metroMap;

    public MetroGraph() {
        metroMap = new HashMap<>();
        initializeMetroNetwork();
    }

    private void initializeMetroNetwork() {
        List<MetroStation> borivaliNeighbors = Arrays.asList(
            new MetroStation("Dahisar"),
            new MetroStation("Andheri")
        );
        metroMap.put("Borivali", borivaliNeighbors);

        List<MetroStation> andheriNeighbors = Arrays.asList(
            new MetroStation("Borivali"),
            new MetroStation("Ghatkopar"),
            new MetroStation("BKC")
        );
        metroMap.put("Andheri", andheriNeighbors);

        // Add more stations and connections
    }

    public List<MetroStation> getAdjacentStations(String stationName) {
        return metroMap.get(stationName);
    }
}

class DijkstraAlgorithm {
    private Map<String, Integer> distance;
    private Map<String, String> previous;
    private Set<String> visited;
    private MetroGraph graph;

    public DijkstraAlgorithm(MetroGraph graph) {
        this.graph = graph;
    }

    public void shortestPath(String startStation) {
        distance = new HashMap<>();
        previous = new HashMap<>();
        visited = new HashSet<>();

        for (String station : graph.getAdjacentStations(startStation)) {
            distance.put(station, Integer.MAX_VALUE);
            previous.put(station, null);
        }
        distance.put(startStation, 0);

        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        queue.add(startStation);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            List<MetroStation> neighbors = graph.getAdjacentStations(current);
            for (MetroStation neighbor : neighbors) {
                int newDistance = distance.get(current) + getTravelTime(current, neighbor.getName());
                if (newDistance < distance.get(neighbor.getName())) {
                    distance.put(neighbor.getName(), newDistance);
                    previous.put(neighbor.getName(), current);
                    queue.add(neighbor.getName());
                }
            }
        }
    }

    public List<String> getPath(String destinationStation) {
        List<String> path = new ArrayList<>();
        for (String station = destinationStation; station != null; station = previous.get(station)) {
            path.add(station);
        }
        Collections.reverse(path);
        return (path.size() > 1) ? path : null;
    }

    public int getDistance(String station) {
        return distance.get(station);
    }

    private int getTravelTime(String station1, String station2) {
        // Simulated travel time calculation
        return 10; // Example time in minutes
    }
}
