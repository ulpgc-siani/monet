package org.monet.mobile.model;

import java.util.Date;


public class Task extends BaseModel {

    public String ID;
    public String Context;
    public String Code;
    public String Label;
    public String Description;

    public Double PositionLat;
    public Double PositionLon;

    public boolean Urgent;

    public String Comments;

    public int StepCount;

    public Long SuggestedStartDate;
    public Long SuggestedEndDate;

    public Date getSuggestedStartDate() {
        return (this.SuggestedStartDate == null) ? null : new Date(this.SuggestedStartDate);
    }

    public Date getSuggestedEndDate() {
        return (this.SuggestedEndDate == null) ? null : new Date(this.SuggestedEndDate);
    }

}
