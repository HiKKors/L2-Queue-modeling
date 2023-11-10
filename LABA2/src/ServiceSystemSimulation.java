import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.text.DecimalFormat;

public class ServiceSystemSimulation {
    private static final int TOTAL_REQUESTS = 1000;
    private static final int REPORT_INTERVAL = 100;

    private List<Request> queue;
    private List<Request> machine1;
    private List<Request> machine2;
    private Random random;
    private double sumWaitingTime;
    private int completedRequests;
    private double avg_in_100;
    private double avg_time;

    public ServiceSystemSimulation() {
        queue = new ArrayList<>();
        machine1 = new ArrayList<>();
        machine2 = new ArrayList<>();
        random = new Random();
        sumWaitingTime = 0;
        completedRequests = 0;
        avg_in_100 = 0;
        avg_time = 0;
    }

    public void runSimulation(double minProcessingTime, double maxProcessingTime) {
        double nextArrivalTime = 0;

        for (int i = 1; i <= TOTAL_REQUESTS; i++) {
            nextArrivalTime += generateRandomTime(minProcessingTime, maxProcessingTime);
            avg_in_100 += nextArrivalTime;
            Request newRequest = new Request(i, nextArrivalTime);
            queue.add(newRequest);

            if (i % REPORT_INTERVAL == 0) {

                System.out.println("Прогресс: " + i + " заявок обработанно.");
                System.out.println("Текущая длина очереди: " + queue.size());
                System.out.println("Среднее время ожидания: " + avg_in_100 / 100);
                System.out.println();
                avg_time += avg_in_100 / 100;
                avg_in_100 = 0;
            }

            if (machine1.isEmpty()) {
                serveRequest(newRequest, machine1);
            } else if (machine2.isEmpty()) {
                serveRequest(newRequest, machine2);
            }
        }

        System.out.println("Моделирование завершено.");
        System.out.println("Общее время обработки: " + nextArrivalTime + " минут.");
        System.out.println("Среднее время ожидания: " + getAverageWaitingTime());
        System.out.println("Процентная разница: " + calculatePercentageDifference() + "%");
    }

    private double generateRandomTime(double minTime, double maxTime) {
        return minTime + (maxTime - minTime) * random.nextDouble();
    }

    private void serveRequest(Request request, List<Request> machine) {
        double processingTime = generateRandomTime(0, 5);
        double departureTime = request.getInsertTime() + processingTime;
        request.setDepartureTime(departureTime);
        machine.add(request);
        queue.remove(request);

        sumWaitingTime += (departureTime - request.getInsertTime());
        completedRequests++;
    }

    private double getAverageWaitingTime() {
        return sumWaitingTime / completedRequests;
    }

    private double calculatePercentageDifference() {
        DecimalFormat df = new DecimalFormat("#.##");
        double num;
        double averageWaitingTimeSingleQueue = sumWaitingTime / completedRequests;
        double averageWaitingTimeTwoMachines = (sumWaitingTime + calculateQueueWaitingTime()) / completedRequests;

        num = 100 - ((averageWaitingTimeTwoMachines * 100) / averageWaitingTimeSingleQueue);
        return Math.round(num * 100.0) / 100.0;
    }

    private double calculateQueueWaitingTime() {
        double sumWaitingTimeInQueue = 0;

        for (Request request : queue) {
            sumWaitingTimeInQueue += (request.getDepartureTime() - request.getInsertTime());
        }

        return sumWaitingTimeInQueue;
    }

    public static void main(String[] args) {
        ServiceSystemSimulation simulation = new ServiceSystemSimulation();
        simulation.runSimulation(3, 5);
    }
}
