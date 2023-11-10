public class Request {
    private int key;
    private double insertTime;
    private double departureTime;

    public Request(int key, double insertTime) {
        this.key = key;
        this.insertTime = insertTime;
    }

    public int getKey() {
        return key;
    }

    public double getInsertTime() {
        return insertTime;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }
}