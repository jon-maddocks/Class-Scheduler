import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Scheduler {


    final static String CLASSES_FILE = "src/JITClasses.txt";
    static ArrayList<String> arlList = new ArrayList<>();

    static HashMap<String, String> hm_ClassA;
    static HashMap<String, String> hm_ClassB;
    static HashMap<String, String> hm_ClassC;

    public static void main(String[] args){
        readFile();

        /*
            1. decide which section the initial time is being checked against
            2. check class A, class B, class C of that section's time
            3. check the adjacent day of those same times for each class
            4. return back to the initial day
            5. check class A, class B, class C of the next section's time


            The idea is to have an extensive HashMap of every valid time
         */

        HashMap<String, String> hm_Times = new HashMap<>();
        hm_Times.put("08:30-10:30", "Available");
        hm_Times.put("10:30-12:30", "Available");
        hm_Times.put("12:30-02:30", "Available");
        hm_Times.put("02:30-04:30", "Available");
        hm_Times.put("09:00-10:30", "Available");
        hm_Times.put("10:00-11:30", "Available");
        hm_Times.put("11:00-12:30", "Available");
        hm_Times.put("12:00-01:30", "Available");
        hm_Times.put("01:00-02:30", "Available");
        hm_Times.put("02:00-03:30", "Available");
        hm_Times.put("03:00-04:30", "Available");
        hm_Times.put("09:00-11:00", "Available");
        hm_Times.put("10:00-12:00", "Available");
        hm_Times.put("11:00-01:00", "Available");
        hm_Times.put("12:00-02:00", "Available");
        hm_Times.put("01:00-03:00", "Available");
        hm_Times.put("02:00-04:00", "Available");
        hm_Times.put("09:00-10:00", "Available");
        hm_Times.put("10:00-11:00", "Available");
        hm_Times.put("11:00-12:00", "Available");
        hm_Times.put("12:00-01:00", "Available");
        hm_Times.put("01:00-02:00", "Available");
        hm_Times.put("02:00-03:00", "Available");
        hm_Times.put("03:00-04:00", "Available");


        hm_ClassA = new HashMap<>(hm_Times);
        hm_ClassB = new HashMap<>(hm_Times);
        hm_ClassC = new HashMap<>(hm_Times);

        Day clsMonday = new Day();
        clsMonday.setHm_ClassA(hm_ClassA);
        clsMonday.setHm_ClassB(hm_ClassB);
        clsMonday.setHm_ClassC(hm_ClassC);
        Day clsTuesday = new Day();
        clsTuesday.setHm_ClassA(hm_ClassA);
        clsTuesday.setHm_ClassB(hm_ClassB);
        clsTuesday.setHm_ClassC(hm_ClassC);
        Day clsWednesday = new Day();
        clsWednesday.setHm_ClassA(hm_ClassA);
        clsWednesday.setHm_ClassB(hm_ClassB);
        clsWednesday.setHm_ClassC(hm_ClassC);
        Day clsThursday = new Day();
        clsThursday.setHm_ClassA(hm_ClassA);
        clsThursday.setHm_ClassB(hm_ClassB);
        clsThursday.setHm_ClassC(hm_ClassC);
        Day clsFriday = new Day();
        clsFriday.setHm_ClassA(hm_ClassA);
        clsFriday.setHm_ClassB(hm_ClassB);
        clsFriday.setHm_ClassC(hm_ClassC);


        LocalTime lt830 = LocalTime.parse("08:30");
        LocalTime lt900 = LocalTime.parse("09:00");
        LocalTime lt930 = LocalTime.parse("09:30");
        LocalTime lt1000 = LocalTime.parse("10:00");
        LocalTime lt1030 = LocalTime.parse("10:30");

        LocalTime lt230 = LocalTime.parse("14:30");
        LocalTime lt430 = LocalTime.parse("16:30");


        String requestTime = "08:30-10:30";
        System.out.println("REQUESTING: 8:30-10:30");
        try{
            for (Map.Entry<String,String> s: hm_Times.entrySet()
            ) {
                String[] arrsplit = s.getKey().split("-");
                String[] arrRequest = requestTime.split("-");

                LocalTime startTime = LocalTime.parse(parseMilitaryTime(arrsplit, 0));
                LocalTime endTime = LocalTime.parse(parseMilitaryTime(arrsplit, 1));
                LocalTime requestStartTime = LocalTime.parse(parseMilitaryTime(arrRequest, 0));
                LocalTime requestEndTime = LocalTime.parse(parseMilitaryTime(arrRequest, 1));

                //check original time first. and for now we'll check sequential time
                if(requestTime.equals(s.getKey())){
                    //if available, change value to the class, else move on to sequential time
                    System.out.println("found: " + s.getKey());
                }
                //                                  10:00       12:00
                //checking if 10:30 is in-between startTime and endTime
            if((requestStartTime.isAfter(startTime) && requestStartTime.isBefore(endTime)) ||
                    (requestEndTime.isAfter(startTime) && requestEndTime.isBefore(endTime)) ||
                    (startTime.isAfter(requestStartTime) && startTime.isBefore(requestEndTime)) ||
                    (endTime.isAfter(requestStartTime) && endTime.isBefore(requestEndTime))
            ){
                //The request time is in-between all the times within this block

            }

            }
        } catch(Exception e){
            e.printStackTrace();
        }
        clsMonday.getHm_ClassA().put("This is the time ", "to mag");
        System.out.println(clsMonday.getHm_ClassA());
        System.out.println(clsTuesday.getHm_ClassA());
    }

    public static void readFile() {

        try {
            BufferedReader bf = new BufferedReader(new FileReader(CLASSES_FILE));
            while(true){
                String line = bf.readLine();
                if(line != null)
                    arlList.add(line);
                else
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String trimCourseID(String strLine){
        StringBuilder strTrim = new StringBuilder();

        for(int i = 0; i < strLine.length(); i++){
            if(i != 3)
                strTrim.append(strLine.charAt(i));
        }

        return strTrim.toString();
    }

    public static String parseMilitaryTime(String[] arrTime, int index) throws ParseException {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");

        if(arrTime[index].equals("02:30") || arrTime[index].equals("01:30") || arrTime[index].equals("01:00") ||
                arrTime[index].equals("02:00") || arrTime[index].equals("03:30") || arrTime[index].equals("03:00") ||
                arrTime[index].equals("04:00") || arrTime[index].equals("04:30")){
            String temp = arrTime[index] + " PM";
            Date date = parseFormat.parse(temp);
            arrTime[index] = displayFormat.format(date);
        }

        return arrTime[index];
    }
}
