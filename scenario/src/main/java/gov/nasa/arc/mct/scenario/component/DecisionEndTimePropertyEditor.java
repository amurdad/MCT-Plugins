package gov.nasa.arc.mct.scenario.component;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.PropertyEditor;
import gov.nasa.arc.mct.scenario.util.DurationFormatter;
import gov.nasa.arc.mct.scenario.view.timeline.TimelineLocalControls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public final class DecisionEndTimePropertyEditor implements PropertyEditor<Object> {
	private DecisionComponent decisionComponent = null;

	public DecisionEndTimePropertyEditor(AbstractComponent component) {
		decisionComponent = (DecisionComponent)component;
	}

	@Override
	public String getAsText() {
		Date endDate = decisionComponent.getModel().getData().getEndTime();
		if (endDate == null)
			endDate = Calendar.getInstance().getTime();
		return DurationFormatter.formatDuration(endDate.getTime());//String.valueOf(FORMATTER.format(startDate));
	}

	/**
	 * Set and save the value in the business model.
	 * 
	 * @param newValue the new value
	 * @throws exception if the new value is invalid.  MCT platform will handle this exception and
	 * disallow the prospective edit.
	 */
	@Override
	public void setAsText(String newValue) throws IllegalArgumentException {
		String result = verify(newValue);
		if (result != null) {
			throw new IllegalArgumentException(result);
		}
		DecisionData businessModel = decisionComponent.getModel().getData();
		Date parsedDate = null;
		try {
			parsedDate = new Date( DurationFormatter.parse(newValue) );			
		} catch (ParseException e) {
			// Format verified in verify()
		}
		businessModel.setEndDate(parsedDate);
	}

	private String verify(String s) {
		assert s != null;
		if (s.isEmpty()) {
			return "Cannot be unspecified";
		}
		try {
			DurationFormatter.parse(s);
		} catch (ParseException e) {
			return "Date formatter error";
		}

		return null;
	}

	@Override
	public String getValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Object selection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object> getTags() {
		throw new UnsupportedOperationException();
	}
} 
