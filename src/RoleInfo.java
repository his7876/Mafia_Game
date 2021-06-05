public abstract class RoleInfo {
	protected String userId;
	protected String role;
	protected String skill;
	
	public RoleInfo(String userId) {
		this.userId = userId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public void voting(String uid) {
		
	}
}
