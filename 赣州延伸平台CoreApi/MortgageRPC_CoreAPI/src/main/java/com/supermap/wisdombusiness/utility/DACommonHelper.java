package com.supermap.wisdombusiness.utility;

//import java.util.Date;
//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

//import com.supermap.common.web.ResultMessage;
//import com.supermap.dossier.model.DAS_JNWJ_FJ;
//import com.supermap.dossier.model.FJS_FJ;

public class DACommonHelper {

    public static String getDAFLBH(String DAFLName)
    {
        if (DAFLName!= null && DAFLName !="" && DAFLName.contains("-"))
        {
            return DAFLName.split("-")[0];
        }
        else
        {
            return DAFLName;
        }
    }
}
