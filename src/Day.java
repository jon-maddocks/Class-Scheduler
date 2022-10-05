import java.util.HashMap;
import java.util.Map;

public class Day {
    protected HashMap<String, String> hm_ClassA;
    protected HashMap<String, String> hm_ClassB;
    protected HashMap<String, String> hm_ClassC;
    protected HashMap<String, Boolean> hm_4CreditHours;
    protected HashMap<String, Boolean> hm_3CreditHours;
    protected HashMap<String, Boolean> hm_2CreditHours;
    protected HashMap<String, Boolean> hm_1CreditHours;

    public Day() {
        this.hm_ClassA = new HashMap<>();
        this.hm_ClassB = new HashMap<>();
        this.hm_ClassC = new HashMap<>();
        this.hm_4CreditHours = new HashMap<>();
        this.hm_4CreditHours.put("08:30-10:30", false);
        this.hm_4CreditHours.put("10:30-12:30", false);
        this.hm_4CreditHours.put("12:30-02:30", false);
        this.hm_4CreditHours.put("02:30-04:30", false);

        this.hm_3CreditHours = new HashMap<>();
        this.hm_3CreditHours.put("09:00-10:30", false);
        this.hm_3CreditHours.put("10:00-11:30", false);
        this.hm_3CreditHours.put("11:00-12:30", false);
        this.hm_3CreditHours.put("12:00-01:30", false);
        this.hm_3CreditHours.put("01:00-02:30", false);
        this.hm_3CreditHours.put("02:00-03:30", false);

        this.hm_2CreditHours = new HashMap<>();
        this.hm_2CreditHours.put("09:00-11:00", false);
        this.hm_2CreditHours.put("10:00-12:00", false);
        this.hm_2CreditHours.put("11:00-01:00", false);
        this.hm_2CreditHours.put("12:00-02:00", false);
        this.hm_2CreditHours.put("01:00-03:00", false);
        this.hm_2CreditHours.put("02:00-04:00", false);



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


    public HashMap<String, Boolean> getHm_4CreditHours() {
        return hm_4CreditHours;
    }


    public HashMap<String, Boolean> getHm_3CreditHours() {
        return hm_3CreditHours;
    }

    public HashMap<String, Boolean> getHm_2CreditHours() {
        return hm_2CreditHours;
    }


    public HashMap<String, Boolean> getHm_1CreditHours() {
        return hm_1CreditHours;
    }

    public boolean check_all(HashMap<String, Boolean> hm_check) {
        for (Map.Entry<String, Boolean> s : hm_check.entrySet()
        ) {
            if (!s.getValue().equals(false)) {
                return false;
            }
        }
        return true;
    }
}
