package com.supermap.wisdombusiness.workflow.service.wfi;

import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.service.common.SmQueryPara;
@Component("smQueryBuilder")
public abstract class SmQueryBuilder {

	protected SmQueryPara m_qp = null;
	protected StringBuilder sb = new StringBuilder();

	public SmQueryBuilder(SmQueryPara param) {
		if (param != null) {
			m_qp = param;
		}
	}

	protected String BuildWhere() {
		sb.delete(0, sb.length());

		return "";

	}

	protected void SetAnd() {
		sb.append(" and ");
	}

	protected void SetActDefID() {
		if (m_qp.getActDef_ID() == null || m_qp.getActDef_ID().isEmpty()) {
			sb.append("");
			SetAnd();
		}
	}
}
