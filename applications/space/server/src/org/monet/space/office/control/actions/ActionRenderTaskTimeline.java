/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package org.monet.space.office.control.actions;

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class ActionRenderTaskTimeline extends Action {

	private static final int TIMELINE_HEIGHT = 30;
	private static final int TIMELINE_WIDTH = 200;

	private static final int TIMELINE_COLOR = 0xfff2f2f2;
	private static final int TIMELINE_BORDER_COLOR = 0xff999999;
	private static final int SUGGESTED_PERIOD_COLOR = 0xff000090;
	private static final int WORKING_PERIOD_COLOR = 0xffC4BD97;
	private static final int WORKING_PERIOD_BORDER_COLOR = 0xff999999;
	private static final int DELAY_COLOR = 0xFFf2f2f2;
	private static final int DELAY_WHEN_FINISHED_COLOR = 0xfff2f2f2;
	private static final int DELAY_MARK_COLOR = 0xFFFF0000;
	private static final int DELAY_MARK_BORDER_COLOR = 0xFF7F7F7F;

	private static final int NOW_MARK_NOT_STARTED_COLOR = 0xff000090;
	private static final int NOW_MARK_WAITING_COLOR = 0xffFF6600;
	private static final int NOW_MARK_PENDING_COLOR = 0xff008000;
	private static final int NOW_MARK_ALERT_COLOR = 0xffFF0000;

	private static final int NOW_MARK_BORDER_COLOR = 0xffffffff;

	private TaskLayer taskLayer;

	private Paint mTimelinePaint;
	private Paint mTimelineStrokePaint;
	private Paint mSuggestedPeriodPaint;
	private Paint mWorkingPeriodPaint;
	private Paint mWorkingPeriodStrokePaint;
	private Paint mDelayPaint;
	private Paint mDelayMarkPaint;
	private Paint mDelayMarkStrokePaint;
	private Paint mDelayFinishedPaint;
	private Paint mNowMarkStrokePaint;

	private Paint mNowMarkAlertPaint;
	private Paint mNowMarkPendingPaint;
	private Paint mNowMarkWaitingPaint;
	private Paint mNowMarkNotStartedPaint;

	private Date suggestStartDate;
	private Date suggestEndDate;
	private Date startDate;
	private Date endDate;
	private Date now;

	private float relativeSuggestStartDate = -1;
	private float relativeSuggestEndDate = -1;
	private float relativeStartDate = -1;
	private float relativeEndDate = -1;
	private float relativeNow = -1;

	private String taskState;

	public ActionRenderTaskTimeline() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	public String execute() {
		String idTask = LibraryRequest.getParameter(Parameter.ID, this.request);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		Task task;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((idTask == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.RENDER_TASK_TIMELINE);
		}

		task = this.taskLayer.loadTask(idTask);

		this.suggestStartDate = task.getInternalStartSuggestedDate();
		this.suggestEndDate = task.getInternalEndSuggestedDate();
		this.startDate = task.getInternalStartDate();
		this.endDate = task.getInternalEndDate();
		this.taskState = task.getState();

		if (!this.componentSecurity.canRead(task, this.getAccount())) {
			throw new TaskAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idTask);
		}

		this.response.setContentType(mimeTypes.get("png"));
		this.response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		this.response.setHeader("Pragma", "no-cache");

		try {
			this.createImage(TIMELINE_WIDTH, TIMELINE_HEIGHT, this.response.getOutputStream());
		} catch (Exception oException) {
		}

		return null; // Avoid controller getting response writer
	}

	private void createImage(int width, int height, OutputStream outputStream) throws IOException {
		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = output.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		initPaints();
		calculateRelativePositions();
		draw(g, width, height);

		g.dispose();
		output.flush();
		ImageIO.write(output, "png", outputStream);
	}

	private void initPaints() {
		mTimelinePaint = new Color(TIMELINE_COLOR, true);
		mTimelineStrokePaint = new Color(TIMELINE_BORDER_COLOR, true);
		mSuggestedPeriodPaint = new Color(SUGGESTED_PERIOD_COLOR, true);
		mDelayPaint = new Color(DELAY_COLOR, true);
		mDelayFinishedPaint = new Color(DELAY_WHEN_FINISHED_COLOR, true);
		mDelayMarkPaint = new Color(DELAY_MARK_COLOR, true);
		mDelayMarkStrokePaint = new Color(DELAY_MARK_BORDER_COLOR, true);
		mWorkingPeriodPaint = new Color(WORKING_PERIOD_COLOR, true);
		mWorkingPeriodStrokePaint = new Color(WORKING_PERIOD_BORDER_COLOR, true);

		mNowMarkAlertPaint = new Color(NOW_MARK_ALERT_COLOR, true);
		mNowMarkPendingPaint = new Color(NOW_MARK_PENDING_COLOR, true);
		mNowMarkWaitingPaint = new Color(NOW_MARK_WAITING_COLOR, true);
		mNowMarkNotStartedPaint = new Color(NOW_MARK_NOT_STARTED_COLOR, true);

		mNowMarkStrokePaint = new Color(NOW_MARK_BORDER_COLOR, true);
	}

	private void calculateRelativePositions() {
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
	}

	protected void draw(Graphics2D g, int viewWidth, int viewHeight) {
		g.setBackground(new Color(0x00000000, true));

		final int height = viewHeight - 3;
		final int yMin = 2;

		final int timelineTop = yMin + (height >> 1);
		final int timelineBottom = yMin + height;

		final int timelineWorkingPeriodTop = yMin;
		final int timelineWorkingPeriodBottom = timelineTop;

		final int nowMarkRadius = (timelineBottom - timelineTop) >> 1;
		final int nowMarkStrokeWidth = nowMarkRadius >> 1;

		int xMin = nowMarkRadius + 1;
		int xMax = viewWidth - nowMarkRadius - 2;
		int width = xMax - xMin;

		final int timelineCenter = timelineBottom - ((timelineBottom - timelineTop) >> 1);

		int suggestStartDateX = (int) (this.relativeSuggestStartDate * width) + xMin;
		int suggestEndDateX = (int) (this.relativeSuggestEndDate * width) + xMin;
		int startDateX = this.relativeStartDate >= 0 ? (int) (this.relativeStartDate * width) + xMin : -1;
		int endDateX = this.relativeEndDate >= 0 ? (int) (this.relativeEndDate * width) + xMin : -1;
		int todayX = (int) (this.relativeNow * width) + xMin;

		if (suggestStartDateX > -1 && suggestStartDateX > xMin) {
			g.setPaint(this.mTimelinePaint);
			g.fillRect(xMin, timelineTop, suggestStartDateX - xMin, timelineBottom - timelineTop);
			g.setPaint(this.mTimelineStrokePaint);
			g.drawRect(xMin, timelineTop, suggestStartDateX - xMin, timelineBottom - timelineTop);
		}

		if (suggestEndDateX > -1 && suggestEndDateX < xMax && endDateX > 0) {
			g.setPaint(mDelayFinishedPaint);
			g.fillRect(suggestEndDateX, timelineTop, xMax - suggestEndDateX, timelineBottom - timelineTop);
			g.setPaint(mTimelineStrokePaint);
			g.drawRect(suggestEndDateX, timelineTop, xMax - suggestEndDateX, timelineBottom - timelineTop);
		}

		if (suggestStartDateX > -1 && suggestEndDateX > -1) {
			g.setPaint(this.mSuggestedPeriodPaint);
			g.fillRect(suggestStartDateX, timelineTop, suggestEndDateX - suggestStartDateX, timelineBottom - timelineTop);
			g.setPaint(this.mTimelineStrokePaint);
			g.drawRect(suggestStartDateX, timelineTop, suggestEndDateX - suggestStartDateX, timelineBottom - timelineTop);
		} else {
			g.setPaint(this.mTimelinePaint);
			g.fillRect(xMin, timelineTop, xMax - xMin, timelineBottom - timelineTop);
			g.setPaint(this.mTimelineStrokePaint);
			g.drawRect(xMin, timelineTop, xMax - xMin, timelineBottom - timelineTop);
		}

		if (suggestEndDateX > -1 && todayX > suggestEndDateX && endDateX <= 0) {
			g.setPaint(mDelayPaint);
			g.fillRect(suggestEndDateX, timelineTop, todayX - suggestEndDateX, timelineBottom - timelineTop);
			g.setPaint(mTimelineStrokePaint);
			g.drawRect(suggestEndDateX, timelineTop, todayX - suggestEndDateX, timelineBottom - timelineTop);
		}

		if (startDateX > -1) {
			int workingPeriodEndX = endDateX > 0 ? endDateX : todayX;
			g.setPaint(mWorkingPeriodPaint);
			g.fillRect(startDateX, timelineWorkingPeriodTop, workingPeriodEndX - startDateX, timelineWorkingPeriodBottom - timelineWorkingPeriodTop);
			g.setPaint(mWorkingPeriodStrokePaint);
			g.drawRect(startDateX, timelineWorkingPeriodTop, workingPeriodEndX - startDateX, timelineWorkingPeriodBottom - timelineWorkingPeriodTop);
		}

		if (endDateX <= 0) {
			if (this.taskState.equals(TaskState.PENDING) || this.taskState.equals(TaskState.NEW))
				g.setPaint(this.mNowMarkPendingPaint);
			else if (this.taskState.equals(TaskState.WAITING))
				g.setPaint(this.mNowMarkWaitingPaint);
			else if (this.taskState.equals(TaskState.FAILURE))
				g.setPaint(this.mNowMarkAlertPaint);
			else
				g.setPaint(this.mNowMarkNotStartedPaint);
			g.fillOval(todayX - nowMarkRadius, timelineCenter - nowMarkRadius, nowMarkRadius << 1, nowMarkRadius << 1);
			g.setPaint(this.mNowMarkStrokePaint);
			g.setStroke(new BasicStroke(nowMarkStrokeWidth));
			g.drawOval(todayX - nowMarkRadius, timelineCenter - nowMarkRadius, nowMarkRadius << 1, nowMarkRadius << 1);

			if (todayX >= suggestEndDateX && suggestEndDateX > -1) {
				this.drawMarkUpper(g, todayX, timelineTop, timelineBottom, nowMarkRadius, this.mDelayMarkPaint, -1, true);
				g.setStroke(new BasicStroke(nowMarkStrokeWidth >> 1));
				this.drawMarkUpper(g, todayX, timelineTop, timelineBottom, nowMarkRadius, this.mDelayMarkStrokePaint, -1, false);
			}
		}

	}

	private void drawMarkUpper(Graphics2D g, int x, int yMin, int yMax, int size, Paint paint, int dir, boolean fillMark) {
		Path2D path = new Path2D.Float();
		int doubleSize = size << 1;
		int pointX = x + size;
		int pointY = yMin - doubleSize;

		path.moveTo(pointX, pointY);
		pointX += doubleSize * dir;
		path.lineTo(pointX, pointY);
		pointY += size;
		path.lineTo(pointX, pointY);
		pointX += size * dir * -1;
		pointY += size;
		path.lineTo(pointX, pointY);
		pointX += size * dir * -1;
		pointY += -size;
		path.lineTo(pointX, pointY);
		path.closePath();

		g.setPaint(paint);
		if (fillMark)
			g.fill(path);
		else
			g.draw(path);
	}

}