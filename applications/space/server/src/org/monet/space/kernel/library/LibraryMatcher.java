package org.monet.space.kernel.library;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LibraryMatcher {

	public static DateMatcher dateMatcher(String format) {
		return new LibraryMatcher.DateMatcher(format);
	}

	public static class DateMatcher implements Matcher, Transform<Date> {
		private String dateFormat;

		public DateMatcher(String format) {
			this.dateFormat = format;
		}

		@Override
		public Transform<Date> match(@SuppressWarnings("rawtypes") Class type) throws Exception {
			if (type == Date.class) {
				return this;
			}
			return null;
		}

		@Override
		public Date read(String value) throws Exception {
			SimpleDateFormat format = new SimpleDateFormat(this.dateFormat);
			return format.parse(value);
		}

		@Override
		public String write(Date value) throws Exception {
			SimpleDateFormat format = new SimpleDateFormat(this.dateFormat);
			return format.format(value);
		}
	}
}
