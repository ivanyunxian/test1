package com.supermap.wisdombusiness.web.ui.tree;

public abstract class BaseTree implements java.io.Serializable {
	private static final long serialVersionUID = 3471803583069167148L;

	private String iconCls;

	private State state;

	private boolean checked;

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
