/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studentEnrollment.views;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import studentEnrollment.Sections;
import studentEnrollment.forms.StudentForm;
import studentEnrollment.model.Student;
import studentEnrollment.model.StudentRepository;

/**
 * View that is available for all users.
 *
 */
@Secured({"ROLE_USER", "ROLE_ADMIN"})
@SpringView(name = "student")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Student View")
@FontAwesomeIcon(FontAwesome.ARCHIVE)
public class StudentView extends VerticalLayout implements View {

	private BeanFieldGroup<Student> studentBinder;
	private StudentForm studentForm;
	private Button editButton;
	private Button saveButton;
	private Button cancelButton;
	private String userName;
	private Student student;
	
    @Autowired
	public StudentView(StudentRepository studentRepo, VaadinSecurity vaadinSecurity) {
    	setMargin(true);
		setSpacing(true);
		
		// create a toolbar
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSizeFull();
		addComponent(toolbar);
		Label toolbarLabel = new Label("Student View");
		toolbarLabel.setSizeFull();
		toolbar.addComponent(toolbarLabel);
		toolbar.setExpandRatio(toolbarLabel, 1);
		toolbar.setSpacing(true);
		
		//create the form
		studentForm = new StudentForm();
		studentForm.setSizeFull();
		addComponent(studentForm);
		
		//get the Student logged in
		userName = vaadinSecurity.getAuthentication().getName();
		student = studentRepo.findByUserName(userName);
		
		// bind the student fields to the form
		studentBinder = new BeanFieldGroup<Student>(Student.class);
		studentBinder.setItemDataSource(student);
		studentBinder.bindMemberFields(studentForm);
		studentBinder.setReadOnly(true);
		
		// add the buttons to the toolbar
		editButton = new Button(FontAwesome.PENCIL_SQUARE_O);
		editButton.setDescription("Edit");
		editButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				studentBinder.setReadOnly(false);
				studentBinder.getField("userName").setReadOnly(true);
				studentForm.enableValidationMessages();
				adjustButtonState();
			}
		});
		saveButton = new Button(FontAwesome.SAVE);
		saveButton.setDescription("Save");
		saveButton.setEnabled(false);
		saveButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (studentBinder.isValid()) {
					try {
						studentBinder.commit();
					} catch (CommitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					studentBinder.setReadOnly(true);
					adjustButtonState();
					
					//save the changes
					Student student = studentBinder.getItemDataSource().getBean();
					studentRepo.save(student);
				} else {
					studentForm.enableValidationMessages();
				}
			}
		});
		cancelButton = new Button(FontAwesome.TIMES);
		cancelButton.setDescription("Cancel");
		cancelButton.setEnabled(false);
		cancelButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				studentBinder.setReadOnly(true);
				adjustButtonState();
				
				//ignore changes or creation
				studentBinder.discard();
			}
		});
		
		toolbar.addComponent(editButton);
		toolbar.addComponent(saveButton);
		toolbar.addComponent(cancelButton);
		adjustButtonState();
		
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
    
	private void adjustButtonState() {
		boolean readOnly = studentBinder.isReadOnly();
		saveButton.setEnabled(!readOnly);
		cancelButton.setEnabled(!readOnly);
		editButton.setEnabled(readOnly);
	}
}
