/**
 * This class is the meat and bones of the scheduling algorithm. A text file will be provided by the user, and this
 *  program will go line-by-line and input the file into an ArrayList. The list will be iterated through one-by-one,
 *  and determine whether or not the class can be scheduled. Once the scheduling is complete, the scheduler will
 *  send an Arraylist of the classes that can be scheduled to the DBTest.java class. In which the database portion
 *  will be compiled.
 *
 *  Functions:
 *      	public Scheduler();
 * 	        void runScheduler();
 * 	        void algorithmMW(ArrayList, String, String, int, ArrayList, String);
 * 	        void algorithmTR(ArrayList, String, String, int, ArrayList, String);
 * 	        void algorithm_day(ArrayList, ArrayList, String, String, String, String);
 * 	        void algorithm_Friday(ArrayList, String, String, String, String);
 * 	        String trimCourseID(String);
 * 	        String parseMilitaryTime(String[], int);
 * 	        String convertTime(String);
 * 	        String parseLeadZero(String);
 * 	        boolean class_Scheduler(Hashmap, String, String);
 * 	        boolean conflictTimes(Hashmap);
 * 	        boolean requestMW(Day, Day, String, String, String);
 * 	        boolean requestTR(Day, Day, String, String, String);
 * 	        boolean request_day(Hashmap, Hashmap, Hashmap, String, String);
 * 	        boolean requestF(Day, String, String, String);
 * 	        ArrayList getClasses();
 * 	        ArrayList getUnavailableClasses();
 */

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Scheduler {
    static ArrayList<String> arlList = new ArrayList<>();
    //Arraylist for each credit
    static ArrayList<String> arlCredit4 = new ArrayList<>();
    static ArrayList<String> arlCredit3 = new ArrayList<>();
    static ArrayList<String> arlCredit2 = new ArrayList<>();
    static ArrayList<String> arlCredit1 = new ArrayList<>();
    //ArrayList for Friday
    static ArrayList<String> arlCredit4_Friday = new ArrayList<>();
    static ArrayList<String> arlCredit3_Friday = new ArrayList<>();
    //Hashmaps for each classroom
    static HashMap<String, String> hm_ClassA;
    static HashMap<String, String> hm_ClassB;
    static HashMap<String, String> hm_ClassC;
    //Day objects for their respective day
    static Day clsMonday = new Day();
    static Day clsTuesday = new Day();
    static Day clsWednesday = new Day();
    static Day clsThursday = new Day();
    static Day clsFriday = new Day();
    //Arraylists for either two day or one day courses
    static ArrayList<String> arlTwoDay = new ArrayList<>();
    static ArrayList<String> arlSingleDay = new ArrayList<>();
    static ArrayList<Day> clsDays = new ArrayList<>();
    //ArrayLists that will contain classes that will be scheduled and classes that could not
    static ArrayList<String> arlScheduledClasses = new ArrayList<>();
    static ArrayList<String> arlCannotSchedule;
    //Arraylist that will have each class with their section number
    static ArrayList<String> arlSectionClasses = new ArrayList<>();
    //String that will append class information throughout the program
    static String SCHEDULE_CLASS = "";

    static File file = new File("COPY.txt");
    static HashMap<String, Integer> hmSectionNum = new HashMap<>();

    //Global constants that will correspond to their respective index when splitting data
    final static int CLASS_ID = 0;
    final static int CLASS_PROFESSOR = 1;
    final static int CLASS_DAYS = 2;
    final static int CLASS_START_TIME = 3;
    final static int CLASS_END_TIME = 4;

    public Scheduler() {
        arlCannotSchedule = new ArrayList<>();
        //Hashmap that will contain every valid time possible
        HashMap<String, String> hm_Times = new HashMap<>();
        //Add each possible time into their respective ArrayList
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

        arlCredit4_Friday.add("08:30-12:30");
        arlCredit4_Friday.add("09:30-01:30");
        arlCredit4_Friday.add("10:30-02:30");
        arlCredit4_Friday.add("11:30-03:30");
        arlCredit4_Friday.add("12:30-04:30");

        arlCredit3_Friday.add("09:00-12:00");
        arlCredit3_Friday.add("10:00-01:00");
        arlCredit3_Friday.add("11:00-02:00");
        arlCredit3_Friday.add("12:00-03:00");
        arlCredit3_Friday.add("01:00-04:00");

        //Add each valid time into the Hashmap, and provide it's availability
        for (String s : arlCredit4) hm_Times.put(s, "Available");
        for (String s : arlCredit3) hm_Times.put(s, "Available");
        for (String s : arlCredit2) hm_Times.put(s, "Available");
        for (String s : arlCredit1) hm_Times.put(s, "Available");
        for (String s : arlCredit4_Friday) hm_Times.put(s, "Available");
        for (String s : arlCredit3_Friday) hm_Times.put(s, "Available");

        arlSingleDay.add("M");
        arlSingleDay.add("T");
        arlSingleDay.add("W");
        arlSingleDay.add("R");
        arlTwoDay.add("MW");
        arlTwoDay.add("TR");

        hm_ClassA = new HashMap<>(hm_Times);
        hm_ClassB = new HashMap<>(hm_Times);
        hm_ClassC = new HashMap<>(hm_Times);

    //    MONDAY
        clsMonday.setHm_ClassA(hm_ClassA);
        clsMonday.setHm_ClassB(hm_ClassB);
        clsMonday.setHm_ClassC(hm_ClassC);
   //     TUESDAY
        clsTuesday.setHm_ClassA(hm_ClassA);
        clsTuesday.setHm_ClassB(hm_ClassB);
        clsTuesday.setHm_ClassC(hm_ClassC);
    //    WEDNESDAY
        clsWednesday.setHm_ClassA(hm_ClassA);
        clsWednesday.setHm_ClassB(hm_ClassB);
        clsWednesday.setHm_ClassC(hm_ClassC);
     //   THURSDAY
        clsThursday.setHm_ClassA(hm_ClassA);
        clsThursday.setHm_ClassB(hm_ClassB);
        clsThursday.setHm_ClassC(hm_ClassC);
     //   FRIDAY
        clsFriday.setHm_ClassA(hm_ClassA);
        clsFriday.setHm_ClassB(hm_ClassB);
        clsFriday.setHm_ClassC(hm_ClassC);
    //    DAYS
        clsDays.add(clsMonday);
        clsDays.add(clsTuesday);
        clsDays.add(clsWednesday);
        clsDays.add(clsThursday);
        clsDays.add(clsFriday);
    }

    public static void runScheduler(){
        try {
            for(int i = 0; i < arlList.size(); i++){
                // The arlList will contain every line from the file, and each will be individually handled here.
                //  It will get split apart and inserted into the scheduling algorithm below, depending on the
                //  request day and request time.
                int sectionNum = 0;
                String tempTrim = trimCourseID(arlList.get(i));
                String[] arrClassSplit = tempTrim.split("\\s+", 5);
                String requestTime = parseLeadZero(arrClassSplit[CLASS_START_TIME]) +
                        "-" + parseLeadZero(arrClassSplit[CLASS_END_TIME]);
                String requestClassTitle = arrClassSplit[CLASS_ID];
                arlSectionClasses.add(requestClassTitle);

                for(String s : arlSectionClasses){
                    if(s.equals(requestClassTitle)){
                        Integer intTemp = hmSectionNum.get(s);
                        sectionNum = (intTemp == null) ? 1 : intTemp+1;
                        hmSectionNum.put(arrClassSplit[CLASS_ID], sectionNum);
                        break;
                    }
                }
                requestClassTitle +=  " " + parseLeadZero(String.valueOf(sectionNum));
                String requestDays = arrClassSplit[CLASS_DAYS];

                if(arlCredit4.contains(requestTime)){
                    if(requestDays.equals("MW"))
                        algorithmMW(arlCredit4, requestTime, requestClassTitle, arlCredit4.indexOf(requestTime), arlCredit4_Friday, arrClassSplit[CLASS_PROFESSOR]);
                    else if(requestDays.equals("TR"))
                        algorithmTR(arlCredit4, requestTime, requestClassTitle, arlCredit4.indexOf(requestTime), arlCredit4_Friday, arrClassSplit[CLASS_PROFESSOR]);
                } else if(arlCredit3.contains(requestTime)){
                    if(requestDays.equals("MW"))
                        algorithmMW(arlCredit3, requestTime, requestClassTitle, arlCredit3.indexOf(requestTime), arlCredit3_Friday, arrClassSplit[CLASS_PROFESSOR]);
                    else if(requestDays.equals("TR"))
                        algorithmTR(arlCredit3, requestTime, requestClassTitle, arlCredit3.indexOf(requestTime), arlCredit3_Friday, arrClassSplit[CLASS_PROFESSOR]);
                } else if(arlCredit2.contains(requestTime) || arlCredit1.contains(requestTime)){
                    if(requestDays.equals("M") | requestDays.equals("T") | requestDays.equals("W") | requestDays.equals("R"))
                        if(arlCredit2.contains(requestTime))
                            algorithm_day(arlCredit2, clsDays, requestTime, requestDays, requestClassTitle, arrClassSplit[CLASS_PROFESSOR]);
                        else if(arlCredit1.contains(requestTime))
                            algorithm_day(arlCredit1, clsDays, requestTime, requestDays, requestClassTitle, arrClassSplit[CLASS_PROFESSOR]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Scheduling Algorithm for when the request days are MW
    public static void algorithmMW(ArrayList<String> arlCredit,
                                   String requestTime, String requestClassTitle, int ORIGINAL_INDEX,
                                   ArrayList<String> arlFriday, String requestProf) throws ParseException {
            int index = arlCredit.indexOf(requestTime);
            String ORIGINAL_REQUEST_TIME = requestTime;
            boolean trueFlag = false;
            boolean[] requiredStep = new boolean[50];
            int currentStep = 0;
            boolean fridayFlag = false;

            while(!trueFlag){
 //               System.out.println("\n---------Requesting: " + requestClassTitle + " on " + requestDays + " at " +
 //                       requestTime + "---------");
                //Check initial days MW
                requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle, requestProf);
                //Check same time, sequential days TR
                if(!requiredStep[currentStep]){
     //               System.out.println(" * * * * Requesting for TR * * * *");
                    currentStep++;
                    requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle, requestProf);
                }
                //Check all sequential times MW
                if(!requiredStep[currentStep]){
                    boolean flag = false;
       //             System.out.println(" * * * * Requesting for MW * * * *");
                    while(!flag){
                        if(requiredStep[currentStep] || index == arlCredit.size()-1){
                            index = 0;
                            flag = true;
                        } else {
                            index++;
                            currentStep++;
                            requestTime = arlCredit.get(index);
                            requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle, requestProf);
                        }
                    }
                }
                //Check from earliest time on sequential days TR
                if(!requiredStep[currentStep]){
                    boolean flag = false;
                    index = 0;
         //           System.out.println(" * * * * Requesting for TR * * * *");
                    while(!flag){
                        if(requiredStep[currentStep] || index == arlCredit.size()){
                            index = 0;
                            flag = true;
                        } else {
                            requestTime = arlCredit.get(index);
                            requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle, requestProf);
                            if(!requiredStep[currentStep]){
                                currentStep++;
                                index++;
                            }
                        }
                    }
                }
                //Loop back to Monday and check from earliest times
                if(!requiredStep[currentStep]){
                    index = 0;
                    boolean flag = false;
         //           System.out.println(" * * * * Requesting for MW * * * *");
                    while(!flag){
                        if(requiredStep[currentStep] || index == ORIGINAL_INDEX){
                            flag = true;
                        } else {
                            requestTime = arlCredit.get(index);
                            requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle, requestProf);
                            if(!requiredStep[currentStep]){
                                currentStep++;
                                index++;
                            }
                        }
                    }
                }
                //If nothing conflicted, schedule the class
                if(requiredStep[currentStep]){
                    arlScheduledClasses.add(SCHEDULE_CLASS);
                    trueFlag = true;
                }
                //Something conflicted, class could not be scheduled. Exit loop and set flag for Friday
                else if(!requiredStep[currentStep] && index == ORIGINAL_INDEX){
                    fridayFlag = true;
                    trueFlag =true;
                }
            }
            //Try to schedule on Friday
            if(fridayFlag){
                algorithm_Friday(arlFriday, requestClassTitle, requestProf, "MW", ORIGINAL_REQUEST_TIME);
            }
    }

    //Scheduling Algorithm for when the request days are TR
    public static void algorithmTR(ArrayList<String> arlCredit,
                                   String requestTime, String requestClassTitle, int ORIGINAL_INDEX,
                                   ArrayList<String> arlFriday, String requestProf) throws ParseException {

        int index = arlCredit.indexOf(requestTime);
        String ORIGINAL_REQUEST_TIME = requestTime;
        boolean trueFlag = false;
        boolean[] requiredStep = new boolean[50];
        int currentStep = 0;
        boolean fridayFlag = false;

        while(!trueFlag){
    //        System.out.println("\n---------Requesting: " + requestClassTitle + " on " + requestDays + " at " +
     //               requestTime + "---------");
            //Check initial time and day for TR
      //      System.out.println(" * * * * Requesting for TR * * * *");
            requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle, requestProf);
            //Check initial time on sequential days MW
            if(!requiredStep[currentStep]){
    //            System.out.println(" * * * * Requesting for MW * * * *");
                currentStep++;
                requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle, requestProf);
            }
            //Check all sequential times on TR
            if(!requiredStep[currentStep]){
                boolean flag = false;
      //          System.out.println(" * * * * Requesting for TR * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == arlCredit.size()-1){
                        index = 0;
                        flag = true;
                    } else {
                        index++;
                        currentStep++;
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle, requestProf);
                    }
                }
            }
            //Check all earliest times on sequential days MW
            if(!requiredStep[currentStep]){
                boolean flag = false;
                index = 0;
     //           System.out.println(" * * * * Requesting for MW * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == arlCredit.size()){
                        index = 0;
                        flag = true;
                    } else {
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = requestMW(clsMonday, clsWednesday, requestTime, requestClassTitle, requestProf);
                        if(!requiredStep[currentStep]){
                            currentStep++;
                            index++;
                        }
                    }
                }
            }
            //Loop back to TR and check all earlier times
            if(!requiredStep[currentStep]){
                index = 0;
                boolean flag = false;
       //         System.out.println(" * * * * Requesting for TR * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == ORIGINAL_INDEX){
                        flag = true;
                    } else {
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = requestTR(clsTuesday, clsThursday, requestTime, requestClassTitle, requestProf);
                        if(!requiredStep[currentStep]){
                            currentStep++;
                            index++;
                        }
                    }
                }
            }
            //Nothing conflicted, add to schedule
            if(requiredStep[currentStep]){
                arlScheduledClasses.add(SCHEDULE_CLASS);
                trueFlag = true;
            }
            //Conflict occurred, set flag for Friday and exit loop
            else if(!requiredStep[currentStep] && index == ORIGINAL_INDEX){
                fridayFlag = true;
                trueFlag =true;
            }
        }
        //Try to schedule class for Friday
        if(fridayFlag){
            algorithm_Friday(arlFriday, requestClassTitle, requestProf, "TR", ORIGINAL_REQUEST_TIME);
        }
    }
    //Scheduling Algorithm for when the request days are M,T,W,R
    public static void algorithm_day(ArrayList<String> arlCredit, ArrayList<Day> clsDays,
                                   String requestTime, String requestDays,
                                   String requestClassTitle, String requestProf) throws ParseException {

        int index = arlCredit.indexOf(requestTime);
        int dayIndex = arlSingleDay.indexOf(requestDays);
        String ORIGINAL_REQUEST_TIME = requestTime;
        int OG_INDEX = index;
        int OG_DAY_INDEX = dayIndex;
        boolean trueFlag = false;
        boolean[] requiredStep = new boolean[50];
        int currentStep = 0;
        boolean fridayFlag = false;
        String currentDay = "";

        while(!trueFlag){
    //        System.out.println("\n---------Requesting: " + requestClassTitle + " on " + arlSingleDay.get(dayIndex) + " at " +
    //                requestTime + "---------");
            //Check initial day and time
    //        System.out.println(" * * * * Requesting for " + arlSingleDay.get(dayIndex) + " * * * *");
            requiredStep[currentStep] = request_day(clsDays.get(dayIndex).hm_ClassA, clsDays.get(dayIndex).hm_ClassB,
                    clsDays.get(dayIndex).hm_ClassC, requestTime, requestClassTitle);
            currentDay = arlSingleDay.get(dayIndex);
            //Check all sequential times on initial day
            if(!requiredStep[currentStep]){
                boolean flag = false;
      //          System.out.println(" * * * * Requesting for " + arlSingleDay.get(dayIndex) + " * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == arlCredit.size()-1){
                        index = 0;
                        dayIndex++;
                        flag = true;
                    } else {
                        index++;
                        currentStep++;
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = request_day(clsDays.get(dayIndex).hm_ClassA, clsDays.get(dayIndex).hm_ClassB,
                                clsDays.get(dayIndex).hm_ClassC, requestTime, requestClassTitle);
                        currentDay = arlSingleDay.get(dayIndex);
                    }
                }
            }

            //Check earliest times for the next day forward
            if(!requiredStep[currentStep]){
                boolean flag = false;
                index = 0;
                requestTime = arlCredit.get(index);
                if(dayIndex == arlSingleDay.size())
                    dayIndex=0;
       //         System.out.println(" * * * * Requesting for " + arlSingleDay.get(dayIndex) + " * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == arlCredit.size() || dayIndex != arlSingleDay.size()){
                        index = 0;
                        flag = true;
                    } else {
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = request_day(clsDays.get(dayIndex).hm_ClassA, clsDays.get(dayIndex).hm_ClassB,
                                clsDays.get(dayIndex).hm_ClassC, requestTime, requestClassTitle);
                        currentDay = arlSingleDay.get(dayIndex);
                        if(!requiredStep[currentStep]){
                            currentStep++;
                            index++;
                        }
                    }
                }
            }

            //Check earliest times for the initial day
            if(!requiredStep[currentStep] && dayIndex == arlSingleDay.indexOf(requestDays)){
                currentDay = arlSingleDay.get(OG_DAY_INDEX);
                requestTime = arlCredit.get(index);
                dayIndex = OG_DAY_INDEX;
                boolean flag = false;
                while(!flag){
                    if(requiredStep[currentStep] || index == OG_INDEX){
                        index = 0;
                        flag = true;
                    } else {
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = request_day(clsDays.get(dayIndex).hm_ClassA, clsDays.get(dayIndex).hm_ClassB,
                                clsDays.get(dayIndex).hm_ClassC, requestTime, requestClassTitle);
                        if(!requiredStep[currentStep]){
                            currentStep++;
                            index++;
                        }
                    }
                }
            }
            //No conflicts occurred, schedule the class
            if(requiredStep[currentStep]){
                SCHEDULE_CLASS += " " + currentDay + " "  + requestProf;
                arlScheduledClasses.add(SCHEDULE_CLASS);
                trueFlag = true;
            }
            //Conflicts occurred, class could not be scheduled. Set Friday flag
            else if(!requiredStep[currentStep] && dayIndex == arlSingleDay.indexOf(requestDays)){
                fridayFlag = true;
                trueFlag = true;
            }
        }
        //Try to schedule class on Friday
        if(fridayFlag){
            algorithm_Friday(arlCredit, requestClassTitle, requestProf, requestDays, ORIGINAL_REQUEST_TIME);
        }
    }

    public static void algorithm_Friday(ArrayList<String> arlCredit, String requestClassTitle,
                                        String requestProf, String requestDay, String originalRequestTime) throws ParseException {

        int index = 0;
        String requestTime = arlCredit.get(index);
        boolean trueFlag = false;
        boolean[] requiredStep = new boolean[50];
        int currentStep = 0;
        boolean unavailableFlag = false;

        while(!trueFlag){
   //         System.out.println("\n---------Requesting: " + requestClassTitle + " on Friday at " +
   //                 requestTime + "---------");
            //Check initial time on Friday
            requiredStep[currentStep] = requestF(clsFriday, requestTime, requestClassTitle, requestProf);

            //Check sequential times on Friday
            if(!requiredStep[currentStep]){
                boolean flag = false;
                index++;
      //          System.out.println(" * * * * Requesting for " + arlCredit.get(index) + " * * * *");
                while(!flag){
                    if(requiredStep[currentStep] || index == arlCredit.size()-1){
                        index = 0;
                        flag = true;
                    } else {
                        index++;
                        currentStep++;
                        requestTime = arlCredit.get(index);
                        requiredStep[currentStep] = requestF(clsFriday, requestTime, requestClassTitle, requestProf);
                    }
                }
            }
            //No conflicts occurred, schedule class on Friday
            if(requiredStep[currentStep]){
                arlScheduledClasses.add(SCHEDULE_CLASS);
                trueFlag = true;
            } else if(!requiredStep[currentStep]){
                unavailableFlag = true;
                trueFlag =true;
            }
        }
        //Conflicts occurred, the class was unable to be scheduled
        if(unavailableFlag){
            String strTemp = String.format("%-10s %-7s %-3s %s", requestClassTitle, requestProf, requestDay, originalRequestTime);
            arlCannotSchedule.add(strTemp);
        }
    }

    //Trim the space in-between CSC ID
    public static String trimCourseID(String strLine) {
        StringBuilder strTrim = new StringBuilder();

        for (int i = 0; i < strLine.length(); i++) {
            if (i != 3)
                strTrim.append(strLine.charAt(i));
        }
        return strTrim.toString();
    }

    //Convert universal time into military time
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

    //Convert military time to universal time
    public static String convertTime(String strMilitaryTime){
        int intHours = (int)strMilitaryTime.charAt(0) - '0';
        int intMinutes = (int)strMilitaryTime.charAt(1)- '0';
        int intConvert = intHours * 10 + intMinutes;
        StringBuilder strUTime = new StringBuilder();

        intConvert %= 12;
        if (intConvert == 0) {
            strUTime.append("12");
            for (int i = 2; i < 5; ++i) {
                strUTime.append(strMilitaryTime.charAt(i));
            }
        }
        else {
            strUTime.append(intConvert);
            for (int i = 2; i < 5; ++i) {
                strUTime.append(strMilitaryTime.charAt(i));
            }
        }
        return strUTime.toString();
    }
    //Add leading zero if the length is only 1
    public static String parseLeadZero(String strTime) {
        String[] arrSplit = strTime.split(":");
        String strResult = strTime;
        if (arrSplit[0].length() == 1) {
            strResult = "0" + strTime;
        }
        return strResult;
    }

    //Purpose is to find EVERY conflict of class times between the start and end time of the requested class
    public static boolean class_Scheduler(HashMap<String, String > hm_Class, String requestTime, String requestClassTitle) throws ParseException {
        HashMap<String, String> hm_ConflictTimes = new HashMap<>();
        final int TIME_START = 0;
        final int TIME_END = 1;

        if (!hm_Class.get(requestTime).equals("Available"))
            return false;


        LocalTime requestStartTime = null;
        LocalTime requestEndTime = null;
        for (Map.Entry<String, String> s : hm_Class.entrySet()
        ) {
            String[] arrsplit = s.getKey().split("-");
            String[] arrRequest = requestTime.split("-");

            LocalTime startTime = LocalTime.parse(parseMilitaryTime(arrsplit, TIME_START));
            LocalTime endTime = LocalTime.parse(parseMilitaryTime(arrsplit, TIME_END));
            requestStartTime = LocalTime.parse(parseMilitaryTime(arrRequest, TIME_START));
            requestEndTime = LocalTime.parse(parseMilitaryTime(arrRequest, TIME_END));

            //Finding all conflict times; ex: 8:30 & 10:30 insert every conflicting time into Hashmap
            if ((requestStartTime.isAfter(startTime) && requestStartTime.isBefore(endTime)) ||
                    (requestEndTime.isAfter(startTime) && requestEndTime.isBefore(endTime)) ||
                    (startTime.isAfter(requestStartTime) && startTime.isBefore(requestEndTime)) ||
                    (endTime.isAfter(requestStartTime) && endTime.isBefore(requestEndTime))
            ) {
                hm_ConflictTimes.put(startTime + "-" + endTime, s.getValue());
            }
        }
        //Check all conflicting times and see if they are all available
        if (conflictTimes(hm_ConflictTimes)) {
            hm_Class.put(requestTime, requestClassTitle);
            SCHEDULE_CLASS = requestClassTitle + " " + requestStartTime+"-"+requestEndTime;
            return true;
        } else
            return false;
    }

    //If any of the values in the Hashmap equate to anything other than 'Available', a class has already been
    //  been scheduled in this time slot
    public static boolean conflictTimes(HashMap<String, String> hm_ConflictTimes) {
        for (Map.Entry<String, String> s : hm_ConflictTimes.entrySet()
        ) {
            if (!s.getValue().equals("Available")) {
                return false;
            }
        }
        return true;
    }

    //Check classrooms A,B,C for MW and check if any are available
    public static boolean requestMW(Day clsMon, Day clsWed, String requestTime, String requestClassTitle, String requestProf) throws ParseException {
    //    System.out.println(" For Class A");
        boolean class_A =  class_Scheduler(clsMon.hm_ClassA, requestTime, requestClassTitle) &&
                            class_Scheduler(clsWed.hm_ClassA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(class_A)
            SCHEDULE_CLASS += " A ";

        if(!class_A){
    //        System.out.println(" For Class B:");
            class_B = class_Scheduler(clsMon.hm_ClassB, requestTime, requestClassTitle) &&
                    class_Scheduler(clsWed.hm_ClassB, requestTime, requestClassTitle);
            SCHEDULE_CLASS += " B ";
            if(!class_B){
    //            System.out.println(" For Class C:");
                class_C = class_Scheduler(clsMon.hm_ClassC, requestTime, requestClassTitle) &&
                        class_Scheduler(clsWed.hm_ClassC, requestTime, requestClassTitle);
                SCHEDULE_CLASS += " C ";
            }
        }
        SCHEDULE_CLASS += " MW " + requestProf;
        return class_A || class_B || class_C;
    }

    //Check classrooms A,B,C for TR and check if any are available
    public static boolean requestTR(Day clsTues, Day clsThurs, String requestTime, String requestClassTitle, String requestProf) throws ParseException {
   //     System.out.println(" For Class A");
        boolean class_A = class_Scheduler(clsTues.hm_ClassA, requestTime, requestClassTitle) &&
                            class_Scheduler(clsThurs.hm_ClassA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(class_A)
            SCHEDULE_CLASS += " A ";

        if(!class_A){
     //       System.out.println(" For Class B:");
            class_B = class_Scheduler(clsTues.hm_ClassB, requestTime, requestClassTitle) &&
                    class_Scheduler(clsThurs.hm_ClassB, requestTime, requestClassTitle);
            SCHEDULE_CLASS += " B ";
            if(!class_B){
      //          System.out.println(" For Class C:");
                class_C = class_Scheduler(clsTues.hm_ClassC, requestTime, requestClassTitle) &&
                        class_Scheduler(clsThurs.hm_ClassC, requestTime, requestClassTitle);
                SCHEDULE_CLASS += " C ";
            }
        }
        SCHEDULE_CLASS += " TR " + requestProf;
        return class_A || class_B || class_C;
    }

    //Check classrooms A,B,C for M,T,W,R and check if any are available
    public static boolean request_day(HashMap<String, String> classA, HashMap<String, String> classB,
                                      HashMap<String, String> classC,
                                      String requestTime, String requestClassTitle) throws ParseException {

   //     System.out.println(" For Class A");
        boolean class_A = class_Scheduler(classA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(class_A)
            SCHEDULE_CLASS += " A ";

        if(!class_A){
     //       System.out.println(" For Class B:");
            class_B = class_Scheduler(classB, requestTime, requestClassTitle);
            SCHEDULE_CLASS += " B ";
            if(!class_B){
     //           System.out.println(" For Class C:");
                class_C = class_Scheduler(classC, requestTime, requestClassTitle);
                SCHEDULE_CLASS += " C ";
            }
        }
        return class_A || class_B || class_C;
    }

    //Check classrooms A,B,C for F and check if any are available
    public static boolean requestF(Day clsFriday, String requestTime, String requestClassTitle, String requestProf) throws ParseException {
   //     System.out.println(" For Class A");
        boolean class_A = class_Scheduler(clsFriday.hm_ClassA, requestTime, requestClassTitle);
        boolean class_B = true;
        boolean class_C = true;

        if(class_A)
            SCHEDULE_CLASS += " A ";

        if(!class_A){
      //      System.out.println(" For Class B:");
            class_B = class_Scheduler(clsFriday.hm_ClassB, requestTime, requestClassTitle);
            SCHEDULE_CLASS += " B ";
            if(!class_B){
      //          System.out.println(" For Class C:");
                class_C = class_Scheduler(clsFriday.hm_ClassC, requestTime, requestClassTitle);
                SCHEDULE_CLASS += " C ";
            }
        }
        SCHEDULE_CLASS += " F " + requestProf;
        return class_A || class_B || class_C;
    }

    //Return all classes that can be scheduled
    public static ArrayList<String> getClasses(){
        return arlScheduledClasses;
    }
    //Return all classes that could not scheduled
    public static ArrayList<String> getUnavailableClasses(){
        return  arlCannotSchedule;
    }
}
