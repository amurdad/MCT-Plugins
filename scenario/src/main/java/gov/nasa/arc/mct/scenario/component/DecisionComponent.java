package gov.nasa.arc.mct.scenario.component;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.JAXBModelStatePersistence;
import gov.nasa.arc.mct.components.ModelStatePersistence;
import gov.nasa.arc.mct.components.PropertyDescriptor;
import gov.nasa.arc.mct.components.PropertyDescriptor.VisualControlDescriptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DecisionComponent extends AbstractComponent implements DurationCapability {

	private final AtomicReference<DecisionModelRole> model = new AtomicReference<DecisionModelRole>(new DecisionModelRole());
	
	public DecisionData getData() {
		return getModel().getData();
	}
	
	public String getDisplay(){
		return this.getDisplayName();
	}
	
	@Override
	protected <T> T handleGetCapability(Class<T> capability) {
		// Note: Don't report self as capability until initialized.
		if (capability.isAssignableFrom(getClass()) && getData().getEndTime() != null) {
			return capability.cast(this);
		}
		if (capability.isAssignableFrom(ModelStatePersistence.class)) {
		    JAXBModelStatePersistence<DecisionModelRole> persistence = new JAXBModelStatePersistence<DecisionModelRole>() {

				@Override
				protected DecisionModelRole getStateToPersist() {
					return model.get();
				}

				@Override
				protected void setPersistentState(DecisionModelRole modelState) {
					model.set(modelState);
				}

				@Override
				protected Class<DecisionModelRole> getJAXBClass() {
					return DecisionModelRole.class;
				}
		        
			};
			
			return capability.cast(persistence);
		}
		return null;
	}
	
	public DecisionModelRole getModel() {
		return model.get();
	}

	@Override
	public List<PropertyDescriptor> getFieldDescriptors()  {

		// Provide an ordered list of fields to be included in the MCT Platform's InfoView.
		List<PropertyDescriptor> fields = new ArrayList<PropertyDescriptor>();


		// Describe MyData's field "doubleData". 
		// We specify a mutable text field.  The control display's values are maintained in the business model
		// via the PropertyEditor object.  When a new value is to be set, the editor also validates the prospective value.
		PropertyDescriptor startTime = new PropertyDescriptor("Start Time", 
				new DecisionStartTimePropertyEditor(this),  VisualControlDescriptor.TextField);
		startTime.setFieldMutable(true);
		PropertyDescriptor endTime = new PropertyDescriptor("End Time", 
				new DecisionEndTimePropertyEditor(this),  VisualControlDescriptor.TextField);
		endTime.setFieldMutable(true);

		fields.add(startTime);
		fields.add(endTime);

		return fields;
	}

	@Override
	public long getStart() {
		return getData().getStartTime().getTime();
	}

	@Override
	public long getEnd() {
		return getData().getEndTime().getTime();
	}

	@Override
	public void setStart(long start) {
		getData().setStartDate(new Date(start));
		save();
	}

	@Override
	public void setEnd(long end) {
		getData().setEndDate(new Date(end));
		save();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

}