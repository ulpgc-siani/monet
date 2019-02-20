package org.monet.metamodel.interfaces;

import java.util.HashMap;
import java.util.Map.Entry;

public class Language {
	public static final int YEAR = -1;
	public static final int QUARTER = -2;
	public static final int MONTH = -3;
	public static final int WEEK = -4;
	public static final int DAY = -5;
	public static final int HOUR = -6;
	public static final int MINUTE = -7;
	public static final int SECOND = -8;
	public static final int TIME_SLOT = -9;
	public static final int HOUR_DAY = -10;
	public static final int WORKING_DAY = -11;
	public static final int DAY_WEEK = -12;
	public static final int QUARTER_YEAR = -13;
	public static final int MONTH_YEAR = -14;
	public static final int TASK_ORDER_TITLE = -15;
	public static final int SUGGESTED_START_DATE = -16;
	public static final int SUGGESTED_END_DATE = -17;
	public static final int COMMENTS = -18;
	public static final int URGENT = -19;

	protected HashMap<Integer, String> labelMap = new HashMap<Integer, String>();

	public String get(int labelId) {
		return this.labelMap.get(labelId);
	}

	public void init() {
		// implemented in specific language classes
	}

	public void init(String localeCode) {
		this.init();

		if (localeCode.contains("es")) {
			this.labelMap.put(YEAR, "Año");
			this.labelMap.put(QUARTER, "Trimestre");
			this.labelMap.put(MONTH, "Mes");
			this.labelMap.put(WEEK, "Semana");
			this.labelMap.put(DAY, "Día");
			this.labelMap.put(HOUR, "Hora");
			this.labelMap.put(MINUTE, "Minuto");
			this.labelMap.put(SECOND, "Segundo");
			this.labelMap.put(TIME_SLOT, "Franjas horarias");
			this.labelMap.put(HOUR_DAY, "Horas del día");
			this.labelMap.put(WORKING_DAY, "Laborales / No laborales");
			this.labelMap.put(DAY_WEEK, "Días de la semana");
			this.labelMap.put(QUARTER_YEAR, "Trimestres del año");
			this.labelMap.put(MONTH_YEAR, "Meses del año");

			this.labelMap.put(TASK_ORDER_TITLE, "Configuración de un encargo");
			this.labelMap.put(SUGGESTED_START_DATE, "Fecha de inicio propuesta");
			this.labelMap.put(SUGGESTED_END_DATE, "Fecha fin límite de realización");
			this.labelMap.put(COMMENTS, "Observaciones");
			this.labelMap.put(URGENT, "Es urgente");
		} else {
			this.labelMap.put(YEAR, "Year");
			this.labelMap.put(QUARTER, "Quarter");
			this.labelMap.put(MONTH, "Month");
			this.labelMap.put(WEEK, "Week");
			this.labelMap.put(DAY, "Day");
			this.labelMap.put(HOUR, "Hour");
			this.labelMap.put(MINUTE, "Minute");
			this.labelMap.put(SECOND, "Second");
			this.labelMap.put(TIME_SLOT, "Time slot");
			this.labelMap.put(HOUR_DAY, "Hours of day");
			this.labelMap.put(WORKING_DAY, "Working / Non working days");
			this.labelMap.put(DAY_WEEK, "Days of week");
			this.labelMap.put(QUARTER_YEAR, "Quarters of year");
			this.labelMap.put(MONTH_YEAR, "Months of year");

			this.labelMap.put(TASK_ORDER_TITLE, "Task order setup");
			this.labelMap.put(SUGGESTED_START_DATE, "Proposal start date");
			this.labelMap.put(SUGGESTED_END_DATE, "Limit deadline date");
			this.labelMap.put(COMMENTS, "Comments");
			this.labelMap.put(URGENT, "Is urgent");
		}
	}

	public void merge(Language language) {
		for (Entry<Integer, String> entry : this.labelMap.entrySet()) {
			this.labelMap.put(entry.getKey(), entry.getValue());
		}
	}

}
