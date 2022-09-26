import java.util.HashMap;
import java.util.Map;

/* Purpose is to check if any of the four sections are available */
public class Monday {

    boolean blnSection1;
    boolean blnSection2;
    boolean blnSection3;
    boolean blnSection4;

    public Monday(boolean blnSection1, boolean blnSection2, boolean blnSection3, boolean blnSection4) {
        this.blnSection1 = blnSection1;
        this.blnSection2 = blnSection2;
        this.blnSection3 = blnSection3;
        this.blnSection4 = blnSection4;
    }

    public void mondaySectionAvailability(HashMap<String, String> hm_section, String strRequestTime){
        if(hm_section.containsKey(strRequestTime)){
            for(Map.Entry<String, String> pair : hm_section.entrySet()){
                if(pair.getValue().equals("Available")){
                    System.out.println("This section is available");
                    pair.setValue(strRequestTime);
                } else {
                    System.out.println("This section is unavailable");
                    break;
                }
            }
        }
    }


}
