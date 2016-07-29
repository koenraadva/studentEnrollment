package studentEnrollment.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message = "Username is required")
	@Size(min=4, max=20, message = "Username must be minimum 4 and maximum 8 characters")
	private String userName;
	
	@NotEmpty(message = "First Name is required")
	private String firstName;
	
	@NotEmpty(message = "Last Name is required")
	private String lastName;
	
	@NotEmpty(message = "Password is required")
	@Size(min=4, max=8, message = "Password must be minimum 4 and maximum 8 characters")
	private String password;
	
	@NotEmpty(message = "Email is required")
	@Email
	private String emailAddress;
	
	@NotNull(message = "Date of Birth is required")
	@Past
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dateOfBirth;
	
	//cascade : ensures roles are also persisted when student is saved
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> roles;
	
	public Student () {
	}
	
    public static Student createStudent(String userName, String emailAddress, String password) {
        Student student = new Student();

        student.userName = userName;
        student.emailAddress = emailAddress;
        //user.password = PasswordCrypto.getInstance().encrypt(password);
        student.password = password;

        if(student.roles == null) {
        	student.roles = new HashSet<UserRole>();
        }

        //create a new user with basic user privileges
        student.roles.add(
                new UserRole(
                        RoleEnum.USER.getRole(),
                        student
                ));

        return student;
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
}
