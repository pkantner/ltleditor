package de.prob.ui.ltl.pattern;



public class PatternInfo {

	private String name;
	private int args;

	private boolean locked;

	private String description;
	private String code;

	public PatternInfo(String name, int args) {
		this.name = name;
		this.args = args;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getArgs() {
		return args;
	}

	public void setArgs(int args) {
		this.args = args;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
