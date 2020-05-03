package TFG_project.HELPERS;

public class DateConverter {

    public static String intToStrTime(int time)
    {
        if(time <10)
            return "0" + time;
        else
            return String.valueOf(time);
    }
}
