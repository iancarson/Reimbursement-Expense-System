package com.andrew.ers.dto;

public class ReimbursementAction {
	public static final String approve = "approve";
	public static final String deny = "deny";
	
	public String action;
		
	public ReimbursementAction(String a) {
		this.action = a;
	}
	
	public ReimbursementAction() {}
	
	@Override
	public String toString() {
		return this.action;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReimbursementAction other = (ReimbursementAction) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		return result;
	}
	
}
