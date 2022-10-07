import java.util.HashMap;
import java.util.Map;

public class Day {
    protected HashMap<String, String> hm_ClassA;
    protected HashMap<String, String> hm_ClassB;
    protected HashMap<String, String> hm_ClassC;

    public Day() {
        this.hm_ClassA = new HashMap<>();
        this.hm_ClassB = new HashMap<>();
        this.hm_ClassC = new HashMap<>();
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

    public void printOutput(){
        System.out.println("Class A : ");
        for (Map.Entry<String, String> s : hm_ClassA.entrySet()
        ) {
            if (!s.getValue().equals("Available")) {
                System.out.println("\t" + s.getKey() + " -> " + s.getValue());
            }
        }

        System.out.println("Class B : ");
        for (Map.Entry<String, String> s : hm_ClassB.entrySet()
        ) {
            if (!s.getValue().equals("Available")) {
                System.out.println("\t" + s.getKey() + " -> " + s.getValue());
            }
        }

        System.out.println("Class C : ");
        for (Map.Entry<String, String> s : hm_ClassC.entrySet()
        ) {
            if (!s.getValue().equals("Available")) {
                System.out.println("\t" + s.getKey() + " -> " + s.getValue());
            }
        }
    }

}
