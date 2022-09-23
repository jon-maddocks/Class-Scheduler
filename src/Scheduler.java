import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Scheduler {


    final static String CLASSES_FILE = "JITClasses.txt";
    static ArrayList<String> arlList = new ArrayList<>();

    public static void main(String[] args) {
        readFile();
        for (String s: arlList
             ) {
            System.out.println(s);
        }
    }

    public static void readFile() {

        try {
            BufferedReader bf = new BufferedReader(new FileReader(CLASSES_FILE));

            while(bf.readLine() != null){
                arlList.add(bf.readLine());
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
