import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Scheduler {
    final static String CLASSES_FILE = "src/JITClasses_Small.txt";
    static ArrayList<String> arlList = new ArrayList<>();

    static HashMap<String, String> hm_ClassA;
    static HashMap<String, String> hm_ClassB;
    static HashMap<String, String> hm_ClassC;
    static final int CLASS_ID = 0;
    static final int CLASS_PROFESSOR = 1;
    static final int CLASS_DAYS = 2;
    static final int CLASS_START_TIME = 3;
    static final int CLASS_END_TIME = 4;

    public static void main(String[] args) {
        readFile();
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


                /*
                    0: CSC105
                    1: James
                    2: MW
                    3: 8:30
                    4: 10:30
                 */
        // For the days, maybe we could have a switch case statement determining for either MW, TR, M, T, etc...
        try {
            for (int i = 0; i < arlList.size(); i++) {
                String tempTrim = trimCourseID(arlList.get(i));
                String[] arrClassSplit = tempTrim.split("\t", 5);
                String requestTime = parseLeadZero(arrClassSplit[CLASS_START_TIME]) +
                        "-" + parseLeadZero(arrClassSplit[CLASS_END_TIME]);
                String requestClassTitle = arrClassSplit[CLASS_ID];

                System.out.println("Requesting: " + requestTime);
                class_A_Scheduler(clsMonday, requestTime, requestClassTitle);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    /*    try{
            LocalTime requestStart= LocalTime.parse("12:30");
            LocalTime startTime = LocalTime.parse("12:00");

            String strEndTime = parseLeadZero("1:30");
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            String temp = "0" + arrTime[index] + " PM";
            Date date = parseFormat.parse(temp);
            arrTime[index] = displayFormat.format(date);
            LocalTime endTime = LocalTime.parse("01:30");

            if(requestStart.isAfter(startTime) && requestStart.isBefore(endTime)){
                System.out.println(requestStart + " is in between: " + startTime + "-" + endTime);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

     */

    }

    public static void readFile() {

        try {
            BufferedReader bf = new BufferedReader(new FileReader(CLASSES_FILE));
            while (true) {
                String line = bf.readLine();
                if (line != null)
                    arlList.add(line);
                else
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String trimCourseID(String strLine) {
        StringBuilder strTrim = new StringBuilder();

        for (int i = 0; i < strLine.length(); i++) {
            if (i != 3)
                strTrim.append(strLine.charAt(i));
        }

        return strTrim.toString();
    }

    public static String parseMilitaryTime(String[] arrTime, int index) throws ParseException {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");

        if (arrTime[index].equals("02:30") || arrTime[index].equals("01:30") || arrTime[index].equals("01:00") ||
                arrTime[index].equals("02:00") || arrTime[index].equals("03:30") || arrTime[index].equals("03:00") ||
                arrTime[index].equals("04:00") || arrTime[index].equals("04:30")) {
            String temp =  arrTime[index] + " PM";
            Date date = parseFormat.parse(temp);
            arrTime[index] = displayFormat.format(date);
        } else if (arrTime[index].equals("08:30") || arrTime[index].equals("09:00")) {
            arrTime[index] = arrTime[index];
        }

        return arrTime[index];
    }

    public static String parseLeadZero(String strTime) {
        String[] arrSplit = strTime.split(":");
        String strResult = strTime;
        if (arrSplit[0].length() == 1) {
            strResult = "0" + strTime;
        }

        return strResult;
    }

    public static boolean class_A_Scheduler(Day clsDay, String requestTime, String requestClassTitle) throws ParseException {
        boolean initialTimeFlag;
        HashMap<String, String> hm_ConflictTimes = new HashMap<>();
        final int TIME_START = 0;
        final int TIME_END = 1;

        if (clsDay.hm_ClassA.get(requestTime).equals("Available")) {
            System.out.println(requestClassTitle + " time slot is empty: " + requestTime);
            initialTimeFlag = true;
        } else {
            System.out.println(requestClassTitle + " CANNOT be inserted.");
            System.out.println();
            return false;
        }

        for (Map.Entry<String, String> s : clsDay.hm_ClassA.entrySet()
        ) {
            String[] arrsplit = s.getKey().split("-");
            String[] arrRequest = requestTime.split("-");

            LocalTime startTime = LocalTime.parse(parseMilitaryTime(arrsplit, TIME_START));
            LocalTime endTime = LocalTime.parse(parseMilitaryTime(arrsplit, TIME_END));
            LocalTime requestStartTime = LocalTime.parse(parseMilitaryTime(arrRequest, TIME_START));
            LocalTime requestEndTime = LocalTime.parse(parseMilitaryTime(arrRequest, TIME_END));

            //                                  10:00       12:00
            //checking if 10:30 is in-between startTime and endTime
            if ((requestStartTime.isAfter(startTime) && requestStartTime.isBefore(endTime)) ||
                    (requestEndTime.isAfter(startTime) && requestEndTime.isBefore(endTime)) ||
                    (startTime.isAfter(requestStartTime) && startTime.isBefore(requestEndTime)) ||
                    (endTime.isAfter(requestStartTime) && endTime.isBefore(requestEndTime))
            ) {
                //The request time is in-between all the times within this block
                //if ALL is available return true, else, return false
                hm_ConflictTimes.put(startTime + "-" + endTime, s.getValue());
                System.out.println(requestTime + " is in between: " + startTime +"-" + endTime);
            } else {
             //   System.out.println(requestTime + " is NOT in between: " + startTime +"-" + endTime);
            }
        }
        //One issue, duplicate class titles with the same time, will be inserted
        if (conflictTimes(hm_ConflictTimes)) {
            System.out.println(requestClassTitle + " inserted into " + requestTime);
            System.out.println();
            clsDay.hm_ClassA.put(requestTime, requestClassTitle);
            return true;
        } else {
            System.out.println(requestClassTitle + " CANNOT be inserted.");
            System.out.println();
            return false;
        }
    }

    public static boolean conflictTimes(HashMap<String, String> hm_ConflictTimes) {
        for (Map.Entry<String, String> s : hm_ConflictTimes.entrySet()
        ) {
            if (!s.getValue().equals("Available")) {
                return false;
            }
        }
        return true;
    }

    public static String findSectionNumber(HashMap<String, Integer> hm_Request, String strRequest){
        if(hm_Request.containsKey(strRequest)){
            int currentNum = hm_Request.get(strRequest);
            hm_Request.put(strRequest, currentNum++);
        }
        return "";
    }
}

/*
public static boolean class_A_Scheduler(Day clsDay, String requestTime, String requestClassTitle) throws ParseException {
        boolean initialTimeFlag;
        HashMap<String, String> hm_ConflictTimes = new HashMap<>();
        final int TIME_START = 0;
        final int TIME_END = 1;

        if (clsDay.hm_ClassA.get(requestTime).equals("Available")) {
            System.out.println(requestClassTitle + " time slot is empty: " + requestTime);
            initialTimeFlag = true;
        } else {
            System.out.println(requestClassTitle + " CANNOT be inserted.");
            System.out.println();
            return false;
        }

        for (Map.Entry<String, String> s : clsDay.hm_ClassA.entrySet()
        ) {
            String[] arrsplit = s.getKey().split("-");
            String[] arrRequest = requestTime.split("-");

            LocalTime startTime = LocalTime.parse(parseMilitaryTime(arrsplit, TIME_START));
            LocalTime endTime = LocalTime.parse(parseMilitaryTime(arrsplit, TIME_END));
            LocalTime requestStartTime = LocalTime.parse(parseMilitaryTime(arrRequest, TIME_START));
            LocalTime requestEndTime = LocalTime.parse(parseMilitaryTime(arrRequest, TIME_END));

            //                                  10:00       12:00
            //checking if 10:30 is in-between startTime and endTime
            if ((requestStartTime.isAfter(startTime) && requestStartTime.isBefore(endTime)) ||
                    (requestEndTime.isAfter(startTime) && requestEndTime.isBefore(endTime)) ||
                    (startTime.isAfter(requestStartTime) && startTime.isBefore(requestEndTime)) ||
                    (endTime.isAfter(requestStartTime) && endTime.isBefore(requestEndTime))
            ) {
                //The request time is in-between all the times within this block
                //if ALL is available return true, else, return false
                hm_ConflictTimes.put(startTime + "-" + endTime, s.getValue());
                System.out.println(requestTime + " is in between: " + startTime +"-" + endTime);
            } else {
                System.out.println(requestTime + " is NOT in between: " + startTime +"-" + endTime);
            }
        }
        //One issue, duplicate class titles with the same time, will be inserted
        if (conflictTimes(hm_ConflictTimes)) {
            System.out.println(requestClassTitle + " inserted into " + requestTime);
            System.out.println();
            clsDay.hm_ClassA.put(requestTime, requestClassTitle);
            return true;
        } else {
            System.out.println(requestClassTitle + " CANNOT be inserted.");
            System.out.println();
            return false;
        }
    }
 */
