package com.sfu276assg1.yancao.UI;

import java.util.Date;

/**
 * Created by vu on 2017-03-16.
 */

public class JourneyInfoForListView {
    String journeyName;
    String journeyDate;

    public JourneyInfoForListView(){}

    public JourneyInfoForListView(String journeyName, String journeyDate)
    {
        this.journeyName = journeyName;
        this.journeyDate = journeyDate;
    }

    public String getJourneyName() {
        return journeyName;
    }

    public void setJourneyName(String journeyName) {
        this.journeyName = journeyName;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }
}
