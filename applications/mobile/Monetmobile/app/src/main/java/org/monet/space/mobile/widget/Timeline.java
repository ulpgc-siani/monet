package org.monet.space.mobile.widget;

import java.util.Date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import org.monet.space.mobile.R;

public class Timeline extends View {

  public static final int  STATE_NOT_STARTED        = 0;
  public static final int  STATE_PENDING            = 1;
  public static final int  STATE_WAITING            = 2;
  public static final int  STATE_ALERT              = 3;

  private Paint            mTimelinePaint;
  private Paint            mTimelineStrokePaint;
  private Paint            mSuggestedPeriodPaint;
  private Paint            mWorkingPeriodPaint;
  private Paint            mWorkingPeriodStrokePaint;
  private Paint            mDelayPaint;
  private Paint            mDelayMarkPaint;
  private Paint            mDelayMarkStrokePaint;
  private Paint            mDelayFinishedPaint;
  private Paint            mSeparatorPaint;
  private Paint            mNowMarkPaint;
  private Paint            mNowMarkStrokePaint;

  private int              mNowMarkAlertColor;
  private int              mNowMarkPendingColor;
  private int              mNowMarkWaitingColor;
  private int              mNowMarkNotStartedColor;
  
  private Date             suggestStartDate;
  private Date             suggestEndDate;
  private Date             startDate;
  private Date             endDate;
  private Date             now;

  private int              state                    = STATE_NOT_STARTED;

  private float            relativeSuggestStartDate = -1;
  private float            relativeSuggestEndDate   = -1;
  private float            relativeStartDate        = -1;
  private float            relativeEndDate          = -1;
  private float            relativeNow              = -1;

  public Timeline(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    init(context, attrs);
  }

  public Timeline(Context context, AttributeSet attrs) {
    super(context, attrs);

    init(context, attrs);
  }

  public Timeline(Context context) {
    super(context);

    init(context, null);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Timeline, 0, 0);

    try {
      mTimelinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mTimelinePaint.setColor(a.getColor(R.styleable.Timeline_timelineColor, 0xfff2f2f2));
      
      mTimelineStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mTimelineStrokePaint.setStyle(Paint.Style.STROKE);
      mTimelineStrokePaint.setStrokeWidth(1);
      mTimelineStrokePaint.setColor(a.getColor(R.styleable.Timeline_timelineStrokeColor, 0xff999999));

      mSuggestedPeriodPaint = new Paint(0);
      mSuggestedPeriodPaint.setColor(a.getColor(R.styleable.Timeline_suggestedPeriodColor, 0xff000090));

      mDelayPaint = new Paint(0);
      mDelayPaint.setColor(a.getColor(R.styleable.Timeline_delayColor, 0xFFFF0000));
      
      mDelayMarkPaint = new Paint(0);
      mDelayMarkPaint.setColor(a.getColor(R.styleable.Timeline_delayMarkColor, 0xFFFF0000));
      
      mDelayMarkStrokePaint = new Paint(0);
      mDelayMarkStrokePaint.setColor(a.getColor(R.styleable.Timeline_delayMarkStrokeColor, 0xFF7F7F7F));
      mDelayMarkStrokePaint.setStyle(Style.STROKE);
      mDelayMarkStrokePaint.setStrokeWidth(3);
      
      mDelayFinishedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mDelayFinishedPaint.setColor(a.getColor(R.styleable.Timeline_timelineColor, 0xff999999));

      mSeparatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mSeparatorPaint.setStyle(Style.STROKE);
      mSeparatorPaint.setStrokeWidth(1);
      mSeparatorPaint.setColor(a.getColor(R.styleable.Timeline_separatorColor, 0xffffffff));

      mWorkingPeriodPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mWorkingPeriodPaint.setColor(a.getColor(R.styleable.Timeline_workingPeriodColor, 0xffC4BD97));
      
      mWorkingPeriodStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mWorkingPeriodStrokePaint.setColor(a.getColor(R.styleable.Timeline_workingPeriodStrokeColor, 0xff999999));
      mWorkingPeriodStrokePaint.setStyle(Style.STROKE);

      mNowMarkAlertColor = a.getColor(R.styleable.Timeline_nowMarkAlertColor, 0xffFF0000);
      mNowMarkPendingColor = a.getColor(R.styleable.Timeline_nowMarkPendingColor, 0xff008000);
      mNowMarkWaitingColor = a.getColor(R.styleable.Timeline_nowMarkWaitingColor, 0xffFF6600);
      mNowMarkNotStartedColor = a.getColor(R.styleable.Timeline_nowMarkNotStartedColor, 0xff000090);

      mNowMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mNowMarkPaint.setColor(a.getColor(R.styleable.Timeline_nowMarkNotStartedColor, 0xff000090));

      mNowMarkStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mNowMarkStrokePaint.setColor(a.getColor(R.styleable.Timeline_nowMarkStrokeColor, 0xffffffff));
      mNowMarkStrokePaint.setStyle(Style.STROKE);
      mNowMarkStrokePaint.setStrokeWidth(8);

    } finally {
      a.recycle();
    }

//    setSuggestStartDate(new Date(113, 3, 10));
//    setSuggestEndDate(new Date(113, 3, 12));
//    setStartDate(new Date(113, 3, 5));
//    setEndDate(new Date(113, 3, 12));
    
    setState(STATE_PENDING);

  }

  public Date getSuggestStartDate() {
    return suggestStartDate;
  }

  public void setSuggestStartDate(Date suggestStartDate) {
    this.suggestStartDate = suggestStartDate;

    this.recalculateRelativePositions();
  }

  public Date getSuggestEndDate() {
    return suggestEndDate;
  }

  public void setSuggestEndDate(Date suggestEndDate) {
    this.suggestEndDate = suggestEndDate;

    this.recalculateRelativePositions();
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;

    this.recalculateRelativePositions();
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;

    this.recalculateRelativePositions();
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;

    int color;
    switch (this.state) {
      case STATE_ALERT:
        color = mNowMarkAlertColor;
        break;
      case STATE_PENDING:
        color = mNowMarkPendingColor;
        break;
      case STATE_WAITING:
        color = mNowMarkWaitingColor;
        break;
      default:
        color = mNowMarkNotStartedColor;
        break;
    }
    mNowMarkPaint.setColor(color);
  }

  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    this.recalculateRelativePositions();
  }

  private void recalculateRelativePositions() {
    long minTime, maxTime;

    this.now = new Date();

    if (suggestStartDate != null) {
      minTime = suggestStartDate.getTime();
      maxTime = suggestStartDate.getTime();
    } else if (suggestEndDate != null) {
      minTime = suggestEndDate.getTime();
      maxTime = suggestEndDate.getTime();
    } else if (startDate == null || endDate == null) {
      minTime = this.now.getTime();
      maxTime = this.now.getTime();
    } else if (startDate != null) {
      minTime = startDate.getTime();
      maxTime = startDate.getTime();
    } else if (endDate != null) {
      minTime = endDate.getTime();
      maxTime = endDate.getTime();
    } else {
      minTime = Long.MIN_VALUE;
      maxTime = Long.MAX_VALUE;
    }

    if (suggestEndDate != null) {
      if (minTime > suggestEndDate.getTime())
        minTime = suggestEndDate.getTime();
      if (maxTime < suggestEndDate.getTime())
        maxTime = suggestEndDate.getTime();
    }

    if (startDate != null) {
      if (minTime > startDate.getTime())
        minTime = startDate.getTime();
      if (maxTime < startDate.getTime())
        maxTime = startDate.getTime();
    }

    if (endDate != null) {
      if (minTime > endDate.getTime())
        minTime = endDate.getTime();
      if (maxTime < endDate.getTime())
        maxTime = endDate.getTime();
    }

    if (startDate == null || endDate == null) {
      if (minTime > now.getTime())
        minTime = now.getTime();
      if (maxTime < now.getTime())
        maxTime = now.getTime();
    }

    float totalTimeline = (float) (maxTime - minTime);
    if (suggestStartDate != null)
      this.relativeSuggestStartDate = (suggestStartDate.getTime() - minTime) / totalTimeline;
    if (suggestEndDate != null)
      this.relativeSuggestEndDate = (suggestEndDate.getTime() - minTime) / totalTimeline;
    if (startDate != null)
      this.relativeStartDate = (startDate.getTime() - minTime) / totalTimeline;
    if (endDate != null)
      this.relativeEndDate = (endDate.getTime() - minTime) / totalTimeline;

    this.relativeNow = totalTimeline == 0.0F ? 0.5F : (now.getTime() - minTime) / totalTimeline;

    this.invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int leftPad = getPaddingLeft();
    int rightPad = getPaddingRight();
    int topPad = getPaddingTop();
    int bottomPad = getPaddingBottom();

    int viewWidth = getWidth();
    int viewHeight = getHeight();

    int yMin = topPad + 8;
    int yMax = viewHeight - bottomPad - 8;

    int height = yMax - yMin;

    final int timelineTop = yMin  + (height >> 1);
    final int timelineBottom = yMin + height;
    
    final int timelineWorkingPeriodTop = yMin;
    final int timelineWorkingPeriodBottom = timelineTop;
    
    final int nowMarkRadius = (timelineBottom - timelineTop) >> 1;
    
    int xMin = leftPad + nowMarkRadius;
    int xMax = viewWidth - rightPad - nowMarkRadius;
    int width = xMax - xMin;
    
    final int timelineCenter = timelineBottom + ((timelineTop - timelineBottom) >> 1);

    int suggestStartDateX = (int) (this.relativeSuggestStartDate * width) + xMin;
    int suggestEndDateX = (int) (this.relativeSuggestEndDate * width) + xMin;
    int startDateX = this.relativeStartDate >= 0 ? (int) (this.relativeStartDate * width) + xMin : -1;
    int endDateX = this.relativeEndDate > 0 ? (int) (this.relativeEndDate * width) + xMin : -1;
    int todayX = (int) (this.relativeNow * width) + xMin;

    if (suggestStartDateX > -1 && suggestStartDateX > xMin) {
      canvas.drawRect(xMin, timelineTop, suggestStartDateX, timelineBottom, this.mTimelinePaint);
      canvas.drawRect(xMin, timelineTop, suggestStartDateX, timelineBottom, this.mTimelineStrokePaint);
    }
    
    if (suggestEndDateX > -1 && suggestEndDateX < xMax && endDateX > 0) {
      canvas.drawRect(suggestEndDateX, timelineTop, xMax, timelineBottom, mDelayFinishedPaint);
      canvas.drawRect(suggestEndDateX, timelineTop, xMax, timelineBottom, mSeparatorPaint);
    }

    if (suggestStartDateX > -1 && suggestEndDateX > -1) {
      canvas.drawRect(suggestStartDateX, timelineTop, suggestEndDateX, timelineBottom, this.mSuggestedPeriodPaint);
      canvas.drawRect(suggestStartDateX, timelineTop, suggestEndDateX, timelineBottom, this.mSeparatorPaint);
    } else {
      canvas.drawRect(xMin, timelineTop, xMax, timelineBottom, this.mTimelinePaint);
      canvas.drawRect(xMin, timelineTop, xMax, timelineBottom, this.mTimelineStrokePaint);
    }
    
    if(suggestEndDateX > -1 && todayX > suggestEndDateX && endDateX <= 0) {
      canvas.drawRect(suggestEndDateX, timelineTop, todayX, timelineBottom, mDelayPaint);
      canvas.drawRect(suggestEndDateX, timelineTop, todayX, timelineBottom, mTimelineStrokePaint);
    }

    if(startDateX > -1) {
      int workingPeriodEndX = endDateX > 0 ? endDateX : todayX;
      canvas.drawRect(startDateX, timelineWorkingPeriodTop, workingPeriodEndX, timelineWorkingPeriodBottom, mWorkingPeriodPaint);
      canvas.drawRect(startDateX, timelineWorkingPeriodTop, workingPeriodEndX, timelineWorkingPeriodBottom, mWorkingPeriodStrokePaint);
    }
    
    if(endDateX <= 0) {
      canvas.drawCircle(todayX, timelineCenter, nowMarkRadius, this.mNowMarkPaint);
      canvas.drawCircle(todayX, timelineCenter, nowMarkRadius, this.mNowMarkStrokePaint);
      
      if(todayX >= suggestEndDateX && suggestEndDateX > -1) {
        this.drawMarkUpper(canvas, todayX, timelineTop, timelineBottom, 15, this.mDelayMarkPaint, -1);
        this.drawMarkUpper(canvas, todayX, timelineTop, timelineBottom, 15, this.mDelayMarkStrokePaint, -1);
      }
    }
    
  }
  
  private void drawMarkUpper(Canvas canvas, int x, int yMin, int yMax, int size, Paint paint, int dir) {
    Path path = new Path();
    
    int doubleSize = size << 1;
    
    path.moveTo(x+size, yMin - doubleSize);
    path.rLineTo(doubleSize * dir, 0);
    path.rLineTo(0, size);
    path.rLineTo(size * dir * -1, size);
    path.rLineTo(size * dir * -1, -size);
    path.close();

    canvas.drawPath(path, paint);
  }

}
