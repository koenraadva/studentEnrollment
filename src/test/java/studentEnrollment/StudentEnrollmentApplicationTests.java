package studentEnrollment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import studentenrollment.StudentEnrollmentApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StudentEnrollmentApplication.class)
@WebAppConfiguration
public class StudentEnrollmentApplicationTests {

	@Test
	public void contextLoads() {
	}

}
