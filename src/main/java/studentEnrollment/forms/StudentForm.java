package studentEnrollment.forms;

import studentEnrollment.model.Student;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class StudentForm extends GridLayout {
	
	@PropertyId("userName")
	private TextField userNameField = new TextField("User Name");
	
	@PropertyId("firstName")
	private TextField firstNameField = new TextField("First Name");
	
	@PropertyId("lastName")
	private TextField lastNameField = new TextField("Last Name");
	
	@PropertyId("password")
	private TextField passwordField = new TextField("Password");
	
	@PropertyId("emailAddress")
	private TextField emailAddressField = new TextField("Email Address");
	
	@PropertyId("dateOfBirth")
	private PopupDateField dateOfBirthField = new PopupDateField("Date of Birth");
	
	public StudentForm() {

		// define grid layout
		super(4,4);
		setColumnExpandRatio(0, 20);
		setColumnExpandRatio(1, 80);
		setColumnExpandRatio(2, 20);
		setColumnExpandRatio(3, 80);
		setSizeFull();
		setMargin(false);
		setSpacing(true);
		setCaption("Student Details");
		
		//expandRatio only on the last row makes it occupies bottom space
		//Height of rows above remain fixed 
		setRowExpandRatio(3, 1.5f);
		
		//addStyleName("data-form");
		
		userNameField.setWidth("100%");
		userNameField.setNullRepresentation("");
		userNameField.setReadOnly(true);
		
		firstNameField.setWidth("100%");
		firstNameField.setNullRepresentation("");
		
		lastNameField.setWidth("100%");
		lastNameField.setNullRepresentation("");
		
		passwordField.setNullRepresentation("");
		
		emailAddressField.setWidth("100%");
		emailAddressField.setNullRepresentation("");
		
		dateOfBirthField.setResolution(Resolution.DAY);
	
		disableValidationMessages();
		
		addComponent(userNameField,0,0,1,0);
		addComponent(firstNameField,0,1,1,1);
		addComponent(lastNameField,0,2,1,2);
		addComponent(passwordField,2,0,2,0);
		addComponent(emailAddressField,2,1,3,1);
		addComponent(dateOfBirthField,2,2,3,2);
		
	}
	
	public void enableValidationMessages() {
		
		userNameField.setValidationVisible(true);
		firstNameField.setValidationVisible(true);
		lastNameField.setValidationVisible(true);
		passwordField.setValidationVisible(true);
		emailAddressField.setValidationVisible(true);
		dateOfBirthField.setValidationVisible(true);
		
		
	}
	
	public void disableValidationMessages() {
		
		userNameField.setValidationVisible(false);
		firstNameField.setValidationVisible(false);
		lastNameField.setValidationVisible(false);
		passwordField.setValidationVisible(false);
		emailAddressField.setValidationVisible(false);
		dateOfBirthField.setValidationVisible(false);
		
		
	}

}
