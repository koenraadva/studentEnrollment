/**
 * 
 */
package studentenrollment.model;

/**
 * @author koenraadva
 *
 */
public enum RoleEnum {
	ALL("ROLE_ALL"), ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private String role;

    private RoleEnum(String s) {
        role = s;
    }

    public String getRole() {
        return role;
    }
}
