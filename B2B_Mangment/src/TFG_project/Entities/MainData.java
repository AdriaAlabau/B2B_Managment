package TFG_project.Entities;

import java.util.List;

public class MainData {

    private static MainData mainData;
    private Integer nSessions;
    private List<Sessio> duradaSessio;

    public static MainData SharedInstance()
    {
        if(mainData == null)
            mainData = new MainData();

        return mainData;
    }

    private MainData()
    {
        //inicialitzem
    }
}
