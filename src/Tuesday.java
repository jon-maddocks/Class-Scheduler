import java.util.HashMap;

public class Tuesday {
    private HashMap<String, String> hm_ClassA;
    private HashMap<String, String> hm_ClassB;
    private HashMap<String, String> hm_ClassC;

    public Tuesday(HashMap<String, String> hm_ClassA, HashMap<String, String> hm_ClassB, HashMap<String, String> hm_ClassC) {
        this.hm_ClassA = hm_ClassA;
        this.hm_ClassB = hm_ClassB;
        this.hm_ClassC = hm_ClassC;
    }

    public HashMap<String, String> getHm_ClassA() {
        return hm_ClassA;
    }

    public void setHm_ClassA(HashMap<String, String> hm_ClassA) {
        this.hm_ClassA = hm_ClassA;
    }

    public HashMap<String, String> getHm_ClassB() {
        return hm_ClassB;
    }

    public void setHm_ClassB(HashMap<String, String> hm_ClassB) {
        this.hm_ClassB = hm_ClassB;
    }

    public HashMap<String, String> getHm_ClassC() {
        return hm_ClassC;
    }

    public void setHm_ClassC(HashMap<String, String> hm_ClassC) {
        this.hm_ClassC = hm_ClassC;
    }
}
