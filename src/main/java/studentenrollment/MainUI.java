package studentEnrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "")
@Title("Student Enrollment")
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

	private static final long serialVersionUID = -1333257394815562012L;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	VaadinSecurity vaadinSecurity;

	@Autowired
    EventBus.SessionEventBus eventBus;

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		Responsive.makeResponsive(this);
		setLocale(vaadinRequest.getLocale());
		getPage().setTitle("Student Enrollment Application");

		// Let's register a custom error handler to make the 'access denied'
		// messages a bit friendlier.
		setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (SecurityExceptionUtils.isAccessDeniedException(event
						.getThrowable())) {
					Notification
							.show("Sorry, you don't have access to do that.");
				} else {
					super.error(event);
				}
			}
		});
		
		if (vaadinSecurity.isAuthenticated()) {
			showMainScreen();
		} else {
			showLoginScreen();
		}
	}

	@Override
    public void attach() {
        super.attach();
        eventBus.subscribe(this);
    }

    @Override
    public void detach() {
        eventBus.unsubscribe(this);
        super.detach();
    }
	
	protected void showMainScreen() {
		setContent(applicationContext.getBean(MainScreen.class));
	}

	protected void showLoginScreen() {
		setContent(applicationContext.getBean(LoginScreen.class));
	}

	@EventBusListenerMethod
    void onLogin(SuccessfulLoginEvent loginEvent) {
        if (loginEvent.getSource().equals(this)) {
            access(new Runnable() {
                @Override
                public void run() {
                    showMainScreen();
                }
            });
        } else {
            // We cannot inject the Main Screen if the event was fired from another UI, since that UI's scope would be active
            // and the main screen for that UI would be injected. Instead, we just reload the page and let the init(...) method
            // do the work for us.
            getPage().reload();
        }
    }

}