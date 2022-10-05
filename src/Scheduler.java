import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Scheduler {
    final static String CLASSES_FILE = "src/JITClasses_Small.txt";
    static ArrayList<String> arlList = new ArrayList<>();
    static ArrayList<String> arlCredit4 = new ArrayList<>();
    static ArrayList<String> arlCredit3 = new ArrayList<>();
    static ArrayList<String> arlCredit2 = new ArrayList<>();
    static ArrayList<String> arlCredit1 = new ArrayList<>();

    static HashMap<String, String> hm_ClassA;
    static HashMap<String, String> hm_ClassB;
    static HashMap<String, String> hm_ClassC;
    static final int CLASS_ID = 0;
    static final int CLASS_PROFESSOR = 1;
    static final int CLASS_DAYS = 2;
    static final int CLASS_START_TIME = 3;
    static final int CLASS_END_TIME = 4;

    static Day clsMonday = new Day();
    static Day clsTuesday = new Day();
    static Day clsWednesday = new Day();
    static Day clsThursday = new Day();
    static Day clsFriday = new Day();


    public static void main(String[] args) {
        readFile();
        HashMap<String, String> hm_Times = new HashMap<>();
        arlCredit4.add("08:30-10:30");
        arlCredit4.add("10:30-12:30");
        arlCredit4.add("12:30-02:30");
        arlCredit4.add("02:30-04:30");
        arlCredit3.add("09:00-10:30");
        arlCredit3.add("10:00-11:30");
        arlCredit3.add("11:00-12:30");
        arlCredit3.add("12:00-01:30");
        arlCredit3.add("01:00-02:30");
        arlCredit3.add("02:00-03:30");
        arlCredit3.add("03:00-04:30");
        arlCredit2.add("09:00-11:00");
        arlCredit2.add("10:00-12:00");
        arlCredit2.add("11:00-01:00");
        arlCredit2.add("12:00-02:00");
        arlCredit2.add("01:00-03:00");
        arlCredit2.add("02:00-04:00");
        arlCredit1.add("09:00-10:00");
        arlCredit1.add("10:00-11:00");
        arlCredit1.add("11:00-12:00");
        arlCredit1.add("12:00-01:00");
        arlCredit1.add("01:00-02:00");
        arlCredit1.add("02:00-03:00");
        arlCredit1.add("03:00-04:00");

        for (String s : arlCredit4) hm_Times.put(s, "Available");
        for (String s : arlCredit3) hm_Times.put(s, "Available");
        for (String s : arlCredit2) hm_Times.put(s, "Available");
        for (String s : arlCredit1) hm_Times.put(s, "Available");


        hm_ClassA = new HashMap<>(hm_Times);
        hm_ClassB = new HashMap<>(hm_Times);
        hm_ClassC = new HashMap<>(hm_Times);

    //    Day clsMonday = new Day();
        clsMonday.setHm_ClassA(hm_ClassA);
        clsMonday.setHm_ClassB(hm_ClassB);
        clsMonday.setHm_ClassC(hm_ClassC);
   //     Day clsTuesday = new Day();
        clsTuesday.setHm_ClassA(hm_ClassA);
        clsTuesday.setHm_ClassB(hm_ClassB);
        clsTuesday.setHm_ClassC(hm_ClassC);
    //    Day clsWednesday = new Day();
        clsWednesday.setHm_ClassA(hm_ClassA);
        clsWednesday.setHm_ClassB(hm_ClassB);
        clsWednesday.setHm_ClassC(hm_ClassC);
     //   Day clsThursday = new Day();
        clsThursday.setHm_ClassA(hm_ClassA);
        clsThursday.setHm_ClassB(hm_ClassB);
        clsThursday.setHm_ClassC(hm_ClassC);
     //   Day clsFriday = new Day();
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
        // ******** This try and catch block ONLY works if the initial day is MW
        try {
            for(int i = 0; i < arlList.size(); i++){
                String tempTrim = trimCourseID(arlList.get(i));
                String[] arrClassSplit = tempTrim.split("\\s+", 5);
                String requestTime = parseLeadZero(arrClassSplit[CLASS_START_TIME]) +
                        "-" + parseLeadZero(arrClassSplit[CLASS_END_TIME]);
                String requestClassTitle = arrClassSplit[CLASS_ID];
                String requestDays = arrClassSplit[CLASS_DAYS];
                //like, if MW and it's a four credit course

                if(arlCredit4.contains(requestTime)){
                    if(requestDays.equals("MW"))
                        algorithmMW(arlCredit4, requestTime, requestDays, requestClassTitle, arlCredit4.indexOf(requestTime));
                    else if(requestDays.equals("TR"))
                        algorithmTR(arlCredit4, requestTime, requestDays, requestClassTitle, arlCredit4.indexOf(requestTime));
                } else if(arlCredit3.contains(requestTime)){
                    if(requestDays.equals("MW"))
                        algorithmMW(arlCredit3, requestTime, requestDays, requestClassTitle, arlCredit4.indexOf(requestTime));
                    else if(requestDays.equals("TR"))
                        algorithmTR(arlCredit3, requestTime, requestDays, requestClassTitle, arlCredit4.indexOf(requestTime));
                } else if(arlCredit2.contains(requestTime)){
                    if(requestDays.equals("MW"))
                        algorithmMW(arlCredit2, requestTime, requestDays, requestClassTitle, arlCredit4.indexOf(requestTime));
                    else if(requestDays.equals("TR"))
                        algorithmTR(arlCredit2, requestTime, requestDays, requestClassTitle, arlCredit4.indexOf(requestTime));                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        System.out.println("--- Monday ---");
        for (String s : arlCredit2) {
            System.out.println("Class A: " + s + " - " + clsMonday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsMonday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsMonday.getHm_ClassC().get(s));
        } */


        System.out.println("*** Four Credits ***");
        System.out.println("--- Monday ---");
        for (String s : arlCredit4) {
            System.out.println("Class A: " + s + " - " + clsMonday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsMonday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsMonday.getHm_ClassC().get(s));
        }
        System.out.println("--- Tuesday ---");
        for (String s : arlCredit4) {
            System.out.println("Class A: " + s + " - " + clsTuesday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsTuesday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsTuesday.getHm_ClassC().get(s));
        }
        System.out.println("--- Wednesday ---");
        for (String s : arlCredit4) {
            System.out.println("Class A: " + s + " - " + clsWednesday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsWednesday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsWednesday.getHm_ClassC().get(s));
        }
        System.out.println("--- Thursday ---");
        for (String s : arlCredit4) {
            System.out.println("Class A: " + s + " - " + clsThursday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsThursday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsThursday.getHm_ClassC().get(s));
        }

        System.out.println("\n*** Three Credits ***");
        System.out.println("--- Monday ---");
        for (String s : arlCredit3) {
            System.out.println("Class A: " + s + " - " + clsMonday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsMonday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsMonday.getHm_ClassC().get(s));
        }
        System.out.println("--- Tuesday ---");
        for (String s : arlCredit3) {
            System.out.println("Class A: " + s + " - " + clsTuesday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsTuesday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsTuesday.getHm_ClassC().get(s));
        }
        System.out.println("--- Wednesday ---");
        for (String s : arlCredit3) {
            System.out.println("Class A: " + s + " - " + clsWednesday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsWednesday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsWednesday.getHm_ClassC().get(s));
        }
        System.out.println("--- Thursday ---");
        for (String s : arlCredit3) {
            System.out.println("Class A: " + s + " - " + clsThursday.getHm_ClassA().get(s));
            System.out.println("Class B: " + s + " - " + clsThursday.getHm_ClassB().get(s));
            System.out.println("Class C: " + s + " - " + clsThursday.getHm_ClassC().get(s));
        }

    }

    public static void algorithmMW(ArrayList<String> arlCredit,
                                   String requestTime, String requestDays,
                                   String requestClassTitle, int ORGINAL_INDEX) throws ParseException {

            int index = arlCredit.indexOf(requestTime);

            boolean trueFlag = false;
            boolean[] requiredStep = new boolean[20];
            int currentStep = 0;
            boolean fridayFlag = false;

            while(!trueFlag){
                System.out.println("\n---------Requesting: " + requestClassTitle + " on " + requestDays + " at " +
                        requestTime + "---------");
                //Step 2, 3
                System.out.println(" * * * * Requesting for MW * * * *");
                requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle);
                //Step 4, 5
                if(!requiredStep[currentStep]){
                    System.out.println(" * * * * Requesting for TR * * * *");
                    currentStep++;
                    System.out.println("STEP: " + currentStep);
                    requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle);
                }
                //Steps 6 -> 9
                if(!requiredStep[currentStep]){
                    boolean flag = false;
                    System.out.println(" * * * * Requesting for MW * * * *");
                    while(!flag){
                        if(requiredStep[currentStep] || index == arlCredit.size()-1){
                            index = 0;
                            flag = true;
                        } else {
                            index++;
                            currentStep++;
                            requestTime = arlCredit.get(index);
                            requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle);
                        }
                    }
                }
                //Steps 10 -> 15
                if(!requiredStep[currentStep]){
                    boolean flag = false;
                    index = 0;
                    System.out.println(" * * * * Requesting for TR * * * *");
                    while(!flag){
                        if(requiredStep[currentStep] || index == arlCredit.size()){
                            index = 0;
                            flag = true;
                        } else {
                            requestTime = arlCredit.get(index);
                            requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle);
                            if(!requiredStep[currentStep]){
                                currentStep++;
                                index++;
                            }
                        }
                    }
                }
                //Steps 16, 17
                if(!requiredStep[currentStep]){
                    index = 0;
                    boolean flag = false;
                    System.out.println(" * * * * Requesting for MW * * * *");
                    while(!flag){
                        if(requiredStep[currentStep] || index == ORGINAL_INDEX){
                            flag = true;
                        } else {
                            requestTime = arlCredit.get(index);
                            requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle);
                            if(!requiredStep[currentStep]){
                                currentStep++;
                                index++;
                            }
                        }
                    }
                }


                if(requiredStep[currentStep])
                    trueFlag = true;
                else if(!requiredStep[currentStep] && index == ORGINAL_INDEX){
                    fridayFlag = true;
                    trueFlag =true;
                }
            }
            if(fridayFlag){
                System.out.println("------> FRIDAY FRIDAY FRIDAY : " + requestClassTitle
                        + " at " + requestTime + " <--------");
            }
    }

    public static void algorithmTR(ArrayList<String> arlCredit,
                                   String requestTime, String requestDays,
                                   String requestClassTitle, int ORGINAL_INDEX) throws ParseException {

        int index = arlCredit.indexOf(requestTime);

        boolean trueFlag = false;
        boolean[] requiredStep = new boolean[20];
        int currentStep = 0;
        boolean fridayFlag = false;

        while(!trueFlag){
            System.out.println("\n---------Requesting: " + requestClassTitle + " on " + requestDays + " at " +
                    requestTime + "---------");
            //Step 2, 3
            System.out.println(" * * * * Requesting for TR * * * *");
            requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle);
            //Step 4, 5
            if(!requiredStep[currentStep]){
                System.out.println(" * * * * Requesting for MW * * * *");
                currentStep++;
                requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle);
            }
            //Steps 6 -> 9
            if(!requiredStep[currentStep]){
                boolean flag = false;
                System.out.println(" * * * * Requesting for TR * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == arlCredit.size()-1){
                        index = 0;
                        flag = true;
                    } else {
                        index++;
                        currentStep++;
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle);
                    }
                }
            }
            //Steps 10 -> 15
            if(!requiredStep[currentStep]){
                boolean flag = false;
                index = 0;
                System.out.println(" * * * * Requesting for MW * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == arlCredit.size()){
                        index = 0;
                        flag = true;
                    } else {
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle);
                        if(!requiredStep[currentStep]){
                            currentStep++;
                            index++;
                        }
                    }
                }
            }
            //Steps 16, 17
            if(!requiredStep[currentStep]){
                index = 0;
                boolean flag = false;
                System.out.println(" * * * * Requesting for TR * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == ORGINAL_INDEX){
                        flag = true;
                    } else {
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle);
                        if(!requiredStep[currentStep]){
                            currentStep++;
                            index++;
                        }
                    }
                }
            }


            if(requiredStep[currentStep])
                trueFlag = true;
            else if(!requiredStep[currentStep] && index == ORGINAL_INDEX){
                fridayFlag = true;
                trueFlag =true;
            }
        }
        if(fridayFlag){
            System.out.println("------> FRIDAY FRIDAY FRIDAY : " + requestClassTitle
                    + " at " + requestTime + " <--------");
        }
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

    public static boolean class_Scheduler(HashMap<String, String > hm_Class, String requestTime, String requestClassTitle) throws ParseException {
        boolean initialTimeFlag;
        HashMap<String, String> hm_ConflictTimes = new HashMap<>();
        final int TIME_START = 0;
        final int TIME_END = 1;

        if (hm_Class.get(requestTime).equals("Available")) {
            System.out.println(requestClassTitle + " time slot is empty: " + requestTime);
        } else {
            System.out.println(requestClassTitle + " CANNOT be inserted.");
            return false;
        }

        for (Map.Entry<String, String> s : hm_Class.entrySet()
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
            }
        }
        //One issue, duplicate class titles with the same time, will be inserted
        if (conflictTimes(hm_ConflictTimes)) {
            System.out.println(requestClassTitle + " inserted into " + requestTime);
            hm_Class.put(requestTime, requestClassTitle);
            return true;
        } else {
            System.out.println(requestClassTitle + " CANNOT be inserted.");
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

    public static boolean requestMW(Day clsMon, Day clsWed, String requestTime, String requestClassTitle) throws ParseException {
        System.out.println(" For Class A");
        boolean class_A =  class_Scheduler(clsMon.hm_ClassA, requestTime, requestClassTitle) &&
                            class_Scheduler(clsWed.hm_ClassA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(!class_A){
            System.out.println(" For Class B:");
            class_B = class_Scheduler(clsMon.hm_ClassB, requestTime, requestClassTitle) &&
                    class_Scheduler(clsWed.hm_ClassB, requestTime, requestClassTitle);
            if(!class_B){
                System.out.println(" For Class C:");
                class_C = class_Scheduler(clsMon.hm_ClassC, requestTime, requestClassTitle) &&
                        class_Scheduler(clsWed.hm_ClassC, requestTime, requestClassTitle);
            }
        }
        return class_A || class_B || class_C;
    }

    public static boolean requestTR(Day clsTues, Day clsThurs, String requestTime, String requestClassTitle) throws ParseException {
        System.out.println(" For Class A");
        boolean class_A = class_Scheduler(clsTues.hm_ClassA, requestTime, requestClassTitle) &&
                            class_Scheduler(clsThurs.hm_ClassA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(!class_A){
            System.out.println(" For Class B:");
            class_B = class_Scheduler(clsTues.hm_ClassB, requestTime, requestClassTitle) &&
                    class_Scheduler(clsThurs.hm_ClassB, requestTime, requestClassTitle);
            if(!class_B){
                System.out.println(" For Class C:");
                class_C = class_Scheduler(clsTues.hm_ClassC, requestTime, requestClassTitle) &&
                        class_Scheduler(clsThurs.hm_ClassC, requestTime, requestClassTitle);
            }
        }

        return class_A || class_B || class_C;
    }

    public static boolean requestM(Day clsMon, String requestTime, String requestClassTitle) throws ParseException {
        System.out.println(" For Class A");
        boolean class_A = class_Scheduler(clsMon.hm_ClassA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(!class_A){
            System.out.println(" For Class B:");
            class_B = class_Scheduler(clsMon.hm_ClassB, requestTime, requestClassTitle);
            if(!class_B){
                System.out.println(" For Class C:");
                class_C = class_Scheduler(clsMon.hm_ClassC, requestTime, requestClassTitle);
            }
        }

        return class_A || class_B || class_C;
    }
}
