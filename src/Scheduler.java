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
        // ******** This try and catch block ONLY works if the initial day is MW
        try {
            for (int i = 0; i < arlList.size(); i++) {
                String tempTrim = trimCourseID(arlList.get(i));
                String[] arrClassSplit = tempTrim.split("\t", 5);
                String requestTime = parseLeadZero(arrClassSplit[CLASS_START_TIME]) +
                        "-" + parseLeadZero(arrClassSplit[CLASS_END_TIME]);
                String requestClassTitle = arrClassSplit[CLASS_ID];
                String requestDays = arrClassSplit[CLASS_DAYS];


                boolean flag = false;
                boolean trueFlag = false;
                boolean boolGo_Back = false;
                int index = arlCredit4.indexOf(requestTime);
                while (!trueFlag) {
                    System.out.print("Requesting: " + requestTime + " for " + requestDays);
                    switch (requestDays) {
                        case "MW":
                            //do something for MW
                            flag = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle);
                            if (!flag && clsMonday.getHm_4CreditHours().get(arlCredit4.get(index))) {
                                while (!flag) {
                                    flag = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle);
                                    if (index < arlCredit4.size() - 1)
                                        index++;
                                    else {
                                        //entire Monday has been exhausted. Go back to TR
                                        requestDays = "TR";
                                        index = 0;
                                        requestTime = arlCredit4.get(index);
                                        break;
                                    }
                                    requestTime = arlCredit4.get(index);
                                }
                                //   break;
                            }
                            if (!flag) {
                                requestDays = "TR";
                            }
                            break;
                        case "TR":
                            //do something for TR
                            flag = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle);
                            if (!flag && clsTuesday.getHm_4CreditHours().get(arlCredit4.get(index--))) {
                                while (!flag) {
                                    flag = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle);
                                    if (index < arlCredit4.size() - 1)
                                        index++;
                                    else {
                                        //entire Tuesday has been exhausted. Go to Friday
                                        flag = true;
                                    }
                                    requestTime = arlCredit4.get(index);
                                }
                                break;
                            }
                            if (!flag) {
                                //we have to go back to monday
                                clsTuesday.hm_4CreditHours.put(requestTime, true);
                                requestDays = "MW";
                                index++;
                                requestTime = arlCredit4.get(index);
                                clsMonday.hm_4CreditHours.put(requestTime, true);
                            }
                            break;
                        case "M":
                            //do something for M
                            break;
                        case "T":
                            //do something for T
                            break;
                        case "W":
                            //do something for W
                            break;
                        case "R":
                            //do something for R
                            break;
                        case "F":
                            //do something for F
                            break;
                    }

                    if (flag) {
                        System.out.println();
                        trueFlag = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("--- Monday ---");
        for (String s : arlCredit4) {
            System.out.println(s + " - " + clsMonday.getHm_ClassA().get(s));
            System.out.println(s + " - " + clsMonday.getHm_ClassB().get(s));
            System.out.println(s + " - " + clsMonday.getHm_ClassC().get(s));
        }
        System.out.println("--- Tuesday ---");
        for (String s : arlCredit4) {
            System.out.println(s + " - " + clsTuesday.getHm_ClassA().get(s));
            System.out.println(s + " - " + clsTuesday.getHm_ClassB().get(s));
            System.out.println(s + " - " + clsTuesday.getHm_ClassC().get(s));
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

      //  clsMon.getHm_4CreditHours().put(requestTime, true);
        return class_A || class_B || class_C;
    }

    public static boolean requestTR(Day clsTues, Day clsThurs, String requestTime, String requestClassTitle) throws ParseException {
        System.out.println(" For Class A");
        boolean class_A = class_Scheduler(clsTues.hm_ClassA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(!class_A){
            System.out.println(" For Class B:");
            class_B = class_Scheduler(clsTues.hm_ClassB, requestTime, requestClassTitle);
            if(!class_B){
                System.out.println(" For Class C:");
                class_C = class_Scheduler(clsTues.hm_ClassC, requestTime, requestClassTitle);
            }
        }

        return class_A || class_B || class_C;
    }
}
