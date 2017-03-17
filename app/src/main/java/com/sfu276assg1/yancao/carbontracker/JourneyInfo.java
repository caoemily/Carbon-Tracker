package com.sfu276assg1.yancao.carbontracker;

import java.util.Date;

/**
 * Created by vu on 2017-03-16.
 */

public class JourneyInfo {
    String journeyName;
    String journeyDate;

    public JourneyInfo(){}

    public JourneyInfo(String journeyName, String journeyDate)
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
