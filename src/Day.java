import java.util.HashMap;

public class Day {
    protected HashMap<String, String> hm_ClassA;
    protected HashMap<String, String> hm_ClassB;
    protected HashMap<String, String> hm_ClassC;
    protected HashMap<String, String> hm_4CreditHours;
    protected HashMap<String, String> hm_3CreditHours;
    protected HashMap<String, String> hm_2CreditHours;
    protected HashMap<String, String> hm_1CreditHours;

    public Day() {
        this.hm_ClassA = new HashMap<>();
        this.hm_ClassB = new HashMap<>();
        this.hm_ClassC = new HashMap<>();
        hm_4CreditHours = new HashMap<>();
        hm_4CreditHours.put("08:30-10:30", "Available");
        hm_4CreditHours.put("10:30-12:30", "Available");
        hm_4CreditHours.put("12:30-02:30", "Available");
        hm_4CreditHours.put("02:30-04:30", "Available");
    }

    public HashMap<String, String> getHm_ClassA() {
        return hm_ClassA;
    }

    public void setHm_ClassA(HashMap<String, String> hm_ClassA) {
        this.hm_ClassA = new HashMap<>(hm_ClassA);
    }

    public HashMap<String, String> getHm_ClassB() {
        return hm_ClassB;
    }

    public void setHm_ClassB(HashMap<String, String> hm_ClassB) {
        this.hm_ClassB = new HashMap<>(hm_ClassB);
    }

    public HashMap<String, String> getHm_ClassC() {
        return hm_ClassC;
    }

    public void setHm_ClassC(HashMap<String, String> hm_ClassC) {
        this.hm_ClassC = new HashMap<>(hm_ClassC);
    }
}
