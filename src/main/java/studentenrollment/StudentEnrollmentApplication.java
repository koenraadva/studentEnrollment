package studentenrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
@EnableVaadinManagedSecurity
@EnableJpaRepositories
public class StudentEnrollmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentEnrollmentApplication.class, args);
    }
    
    /**
    * Provide custom system messages to make sure the application is reloaded when the session expires.
    */
   @Bean
   SystemMessagesProvider systemMessagesProvider() {
       return new SystemMessagesProvider() {
           @Override
           public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
               CustomizedSystemMessages systemMessages = new CustomizedSystemMessages();
               systemMessages.setSessionExpiredNotificationEnabled(false);
               return systemMessages;
           }
       };
   }
   

   /**
    * Configure the authentication manager.
    */
   @Configuration
   static class AuthenticationConfiguration implements AuthenticationManagerConfigurer {
	   
		@Autowired
		private UserDetailsService customUserDetailsService;

		/*@Override
		public void configure(AuthenticationManagerBuilder auth)
				throws Exception {
			auth.inMemoryAuthentication().withUser("user").password("user")
					.roles("USER").and().withUser("admin").password("admin")
					.roles("ADMIN");
		 	*/
			
		@Override public void configure(AuthenticationManagerBuilder auth) throws Exception {
			 auth.userDetailsService(customUserDetailsService);
			 
		}
   }
}
