import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Scheduler {


    final static String CLASSES_FILE = "src/JITClasses.txt";
    static ArrayList<String> arlList = new ArrayList<>();
    static ArrayList<String> classes_today = new ArrayList<>();
    static boolean blnSection1 = true;
    static boolean blnSection2 = true;
    static boolean blnSection3 = true;
    static boolean blnSection4 = true;

    public static void main(String[] args){
        readFile();

        ArrayList<String> arlSection1 = new ArrayList<>();
        ArrayList<String> arlSection2 = new ArrayList<>();
        ArrayList<String> arlSection3 = new ArrayList<>();
        ArrayList<String> arlSection4 = new ArrayList<>();

        arlSection1.add("8:30-10:30");
        arlSection1.add("9:00-10:30");
        arlSection1.add("10:00-11:30");
        arlSection1.add("9:00-11:00");
        arlSection1.add("10:00-12:00");
        arlSection1.add("9:00-10:00");
        arlSection1.add("10:00-11:00");

        arlSection2.add("10:30-12:30");
        arlSection2.add("10:00-11:30");
        arlSection2.add("11:00-12:30");
        arlSection2.add("12:00-1:30");
        arlSection2.add("9:00-11:00");
        arlSection2.add("10:00-12:00");
        arlSection2.add("11:00-1:00");
        arlSection2.add("12:00-2:00");
        arlSection2.add("9:00-10:00");
        arlSection2.add("10:00-11:00");
        arlSection2.add("11:00-12:00");
        arlSection2.add("12:00-1:00");

        arlSection3.add("12:30-2:30");
        arlSection3.add("12:00-1:30");
        arlSection3.add("1:00-2:30");
        arlSection3.add("2:00-3:30");
        arlSection3.add("11:00-1:00");
        arlSection3.add("12:00-2:00");
        arlSection3.add("1:00-3:00");
        arlSection3.add("2:00-4:00");
        arlSection3.add("11:00-12:00");
        arlSection3.add("12:00-1:00");
        arlSection3.add("1:00-2:00");
        arlSection3.add("2:00-3:00");

        arlSection4.add("2:30-4:30");
        arlSection4.add("2:00-3:30");
        arlSection4.add("3:00-4:30");
        arlSection4.add("1:00-3:00");
        arlSection4.add("2:00-4:00");
        arlSection4.add("2:00-3:00");
        arlSection4.add("3:00-4:00");

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


        LocalTime lt830 = LocalTime.parse("08:30");
        LocalTime lt900 = LocalTime.parse("09:00");
        LocalTime lt930 = LocalTime.parse("09:30");
        LocalTime lt1000 = LocalTime.parse("10:00");
        LocalTime lt1030 = LocalTime.parse("10:30");

        LocalTime lt230 = LocalTime.parse("14:30");
        LocalTime lt430 = LocalTime.parse("16:30");


        String requestTime = "08:30-10:30";
        try{
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");

          //  Date date = parseFormat.parse(test);
         //   System.out.println(displayFormat.format(date));

            for (Map.Entry<String,String> s: hm_Times.entrySet()
            ) {
                String[] arrsplit = s.getKey().split("-");
                String[] arrRequest = requestTime.split("-");

                if(arrsplit[0].equals("02:30") || arrsplit[0].equals("01:30") || arrsplit[0].equals("01:00") ||
                        arrsplit[0].equals("02:00") || arrsplit[0].equals("03:30") || arrsplit[0].equals("03:00") ||
                        arrsplit[0].equals("04:00") || arrsplit[0].equals("04:30")){
                    String temp = arrsplit[0] + " PM";
                    Date date = parseFormat.parse(temp);
                    arrsplit[0] = displayFormat.format(date);
                }
                if(arrsplit[1].equals("02:30") || arrsplit[1].equals("01:30") || arrsplit[1].equals("01:00") ||
                        arrsplit[1].equals("02:00") || arrsplit[1].equals("03:30") || arrsplit[1].equals("03:00") ||
                        arrsplit[1].equals("04:00") || arrsplit[1].equals("04:30")){
                    String temp = arrsplit[1] + " PM";
                    Date date = parseFormat.parse(temp);
                    arrsplit[1] = displayFormat.format(date);
                }
                if(arrRequest[0].equals("02:30") || arrRequest[0].equals("01:30") || arrRequest[0].equals("01:00") ||
                        arrRequest[0].equals("02:00") || arrRequest[0].equals("03:30") || arrRequest[0].equals("03:00") ||
                        arrRequest[0].equals("04:00") || arrRequest[0].equals("04:30")){
                    String temp = arrRequest[0] + " PM";
                    Date date = parseFormat.parse(temp);
                    arrRequest[0] = displayFormat.format(date);
                }
                if(arrRequest[1].equals("02:30") || arrRequest[1].equals("01:30") || arrRequest[1].equals("01:00") ||
                        arrRequest[1].equals("02:00") || arrRequest[1].equals("03:30") || arrRequest[1].equals("03:00") ||
                        arrRequest[1].equals("04:00") || arrRequest[1].equals("04:30")){
                    String temp = arrRequest[1] + " PM";
                    Date date = parseFormat.parse(temp);
                    arrRequest[1] = displayFormat.format(date);
                }

                LocalTime startTime = LocalTime.parse(arrsplit[0]);
                LocalTime endTime = LocalTime.parse(arrsplit[1]);
                LocalTime requestStartTime = LocalTime.parse(arrRequest[0]);
                LocalTime requestEndTime = LocalTime.parse(arrRequest[1]);

                //                                  10:00       12:00
                //checking if 10:30 is in-between startTime and endTime
            if((requestStartTime.isAfter(startTime) && requestStartTime.isBefore(endTime)) ||
                    (requestEndTime.isAfter(startTime) && requestEndTime.isBefore(endTime)) ||
                    (startTime.isAfter(requestStartTime) && startTime.isBefore(requestEndTime)) ||
                    (endTime.isAfter(requestStartTime) && endTime.isBefore(requestEndTime))
            ){
                System.out.println(requestTime + " is in between: " + startTime + "-" + endTime);
            }

            }
        } catch(Exception e){
            e.printStackTrace();
        }


















        String record1 = arlList.get(0);
        String record2 = arlList.get(1);
        String record3 = arlList.get(2);

        String[] arrSplit;
        arrSplit =  trimCourseID(record1).split("\\s+", 5);
        ArrayList<String> arrList = new ArrayList<>(Arrays.asList(arrSplit));

        System.out.println("Arrlist: " + arrList);

        String testTime = "10:00-11:30";

        boolean classA_Availability = true;
        boolean classB_Availability = true;
        boolean classC_Availability = true;



        Monday mondayAvailability = new Monday(blnSection1, blnSection2, blnSection3, blnSection4);


        for(int i = 0; i < 4; i++){
            arrSplit = trimCourseID(arlList.get(i)).split("\\s+", 5);
            String strTime = arrSplit[3] + "-" + arrSplit[4];
            System.out.println("REQUESTING: " + strTime);

            blnSection1 = arlSection1.contains(strTime);
            blnSection2 = arlSection2.contains(strTime);
            blnSection3 = arlSection3.contains(strTime);
            blnSection4 = arlSection4.contains(strTime);


            if(blnSection1 && blnSection2){
                //this time falls within both sections
            }
        }
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
}
