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
package studentenrollment.views;

import java.util.List;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import studentenrollment.Sections;
import studentenrollment.forms.StudentForm;
import studentenrollment.model.Student;
import studentenrollment.model.StudentRepository;

import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * View that is available for the administrator only.
 *
 */
@Secured({ "ROLE_ADMIN" })
@SpringView(name = "admin")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Administrator View")
@FontAwesomeIcon(FontAwesome.ARCHIVE)
public class AdminView extends VerticalLayout implements View {

	private BeanFieldGroup<Student> studentBinder;
	private StudentForm studentForm;
	private Grid studentGrid;
	private Button createButton;
	private Button deleteButton;
	private Button editButton;
	private Button saveButton;
	private Button cancelButton;
	private HorizontalLayout detailbar;
	private List<Student> studentList;

	@Autowired
	public AdminView(StudentRepository studentRepo) {
		setMargin(true);
		setSpacing(true);
		
		// prepare our container
		studentList = studentRepo.findAll();
		BeanItemContainer<Student> container = new BeanItemContainer<Student>(Student.class, studentList);
		
		// create a toolbar
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSizeFull();
		addComponent(toolbar);
		Label toolbarLabel = new Label("Administrator View");
		toolbarLabel.setSizeFull();
		toolbar.addComponent(toolbarLabel);
		toolbar.setExpandRatio(toolbarLabel, 1);
		toolbar.setSpacing(true);

		// add the crud buttons
		createButton = new Button(FontAwesome.PLUS);
		createButton.setDescription("Add");
		createButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				//studentBinder.setItemDataSource(new Student());
				studentBinder.setItemDataSource(Student.createStudent("","",""));
				studentBinder.bindMemberFields(studentForm);
				studentBinder.setReadOnly(false);
				studentForm.disableValidationMessages();
				setVisibility(true);
				adjustButtonState();
			}
		});
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
		deleteButton = new Button(FontAwesome.TRASH_O);
		deleteButton.setDescription("Delete");
		deleteButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(UI.getCurrent(), "Delete Student", "You want to confirm deletion ?", "Yes", "No", 
				        new ConfirmDialog.Listener() {
				            public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				                    // Confirmed to continue
				                	studentRepo.delete(studentBinder.getItemDataSource().getBean());
				                	container.removeItem(studentBinder.getItemDataSource().getBean());
				                	studentGrid.deselect(studentBinder.getItemDataSource().getBean());
				                }
				            }
				});
				
			}
		});
		toolbar.addComponent(createButton);
		toolbar.addComponent(editButton);
		toolbar.addComponent(deleteButton);

		// add and format the grid
		studentGrid = new Grid("Students");
		studentGrid.setContainerDataSource(container);
		studentGrid.setColumnOrder("userName", "firstName", "lastName",
				"emailAddress", "dateOfBirth");
		studentGrid.removeColumn("id");
		studentGrid.removeColumn("password");
		studentGrid.removeColumn("roles");
		studentGrid.getColumn("firstName").setExpandRatio(1);
		studentGrid.getColumn("lastName").setExpandRatio(1);
		studentGrid.setColumnReorderingAllowed(true);
		studentGrid.setSizeFull();
		addComponent(studentGrid);
		setExpandRatio(studentGrid, 1);

		// bind the student fields to the form
		studentBinder = new BeanFieldGroup<Student>(Student.class);

		// action when a student has been selected
		studentGrid.addSelectionListener(selectionEvent -> {
			if (studentGrid.getSelectedRow() == null) {
				setVisibility(false);
			} else {
				setVisibility(true);
				studentBinder.setItemDataSource(container.getItem(studentGrid
						.getSelectedRow()));
				studentBinder.bindMemberFields(studentForm);
				studentBinder.setReadOnly(true);
			}
		});

		// the detailbar contains the form and the save/cancel buttons
		detailbar = new HorizontalLayout();
		detailbar.setSizeFull();
		detailbar.setSpacing(true);
		addComponent(detailbar);

		// add the form, but hide and disable
		studentForm = new StudentForm();
		studentForm.setSizeFull();
		detailbar.addComponent(studentForm);
		detailbar.setExpandRatio(studentForm, 1);

		// and now the buttons
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
					if (student.getId()==null) {
						studentRepo.save(student);
						container.addBean(student);
					} else {
						studentRepo.save(student);
					}
					//studentList = studentRepo.findAll();
					studentGrid.scrollTo(student);
					studentGrid.select(student);
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
		detailbar.addComponent(saveButton);
		detailbar.addComponent(cancelButton);

		// initially hide the studentForm and disable buttons
		setVisibility(false);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
	}

	public void setVisibility(boolean visible) {
		detailbar.setVisible(visible);
		studentForm.setEnabled(visible);
	}
	
	private void adjustButtonState() {
		boolean readOnly = studentBinder.isReadOnly();
		saveButton.setEnabled(!readOnly);
		cancelButton.setEnabled(!readOnly);
		createButton.setEnabled(readOnly);
		editButton.setEnabled(readOnly);
		deleteButton.setEnabled(readOnly);
		studentGrid.setEnabled(readOnly);
	}
}
