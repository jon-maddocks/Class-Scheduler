/**
 * The purpose of this class is to represent the classrooms for each week day. Each day will consist of three
 *  hashmaps that represent Classroom A, Classroom B, Classroom C. Each hashmap will contain a key of every
 *  valid time and a value to represent if that time slot is available or a course is currently in the
 *  respected classroom.
 *
 *  Functions:
 *      public Day();
 *      Hashmap getHM_ClassA();
 *      Hashmap setHM_ClassA();
 *      Hashmap getHM_ClassB();
 *      Hashmap setHM_ClassB();
 *      Hashmap getHM_ClassC();
 *      Hashmap setHM_ClassC();
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Day {
    protected HashMap<String, String> hm_ClassA;
    protected HashMap<String, String> hm_ClassB;
    protected HashMap<String, String> hm_ClassC;
    protected ArrayList<String> arlScheduledClasses;

    public Day() {
        this.hm_ClassA = new HashMap<>();
        this.hm_ClassB = new HashMap<>();
        this.hm_ClassC = new HashMap<>();
        this.arlScheduledClasses = new ArrayList<>();
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
