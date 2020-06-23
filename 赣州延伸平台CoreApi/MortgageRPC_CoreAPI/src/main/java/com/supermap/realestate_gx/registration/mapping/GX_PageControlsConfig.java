package com.supermap.realestate_gx.registration.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("workflows")
public class GX_PageControlsConfig implements Serializable {
	/**
	 * 页面控制配置类，用于XML的反序列化。与工作流程配置类RegisterWorkFlow配置的页面对应 WUZHU
	 */
	private static final long serialVersionUID = 8417416623709184173L;

	/**
	 * 某个流程的配置项
	 */
	@XStreamImplicit(itemFieldName = "Workflow")
	private List<GX_PageControlsItem> Workflows = new ArrayList<GX_PageControlsItem>();

	public GX_PageControlsConfig() {

	}

	public List<GX_PageControlsItem> getWorkflows() {
		return Workflows;
	}

	public void setWorkflows(List<GX_PageControlsItem> workflows) {
		Workflows = workflows;
	}
	public class GX_ids implements Serializable {
		private static final long serialVersionUID = -8417416623706194272L;
		/**
		 * 隐藏ID集合,用#将各ID合并（之所以用#因为该字符在中文和英文下都是相同的）
		 */
		@XStreamAsAttribute
		private String hiddenids;
		@XStreamAsAttribute
		/**
		 * 禁用ID集合
		 */
		private String  disabledids;
		@XStreamAsAttribute
		/**
		 * 显示ID集合
		 */
		private String  abledids;
		public String getHiddenids() {
			return hiddenids;
		}
		public void setHiddenids(String hiddenids) {
			this.hiddenids = hiddenids;
		}
		public String getDisabledids() {
			return disabledids;
		}
		public void setDisabledids(String disabledids) {
			this.disabledids = disabledids;
		}
		public String getAbledids() {
			return abledids;
		}
		public void setAbledids(String abledids) {
			this.abledids = abledids;
		}

	}
	public class GX_PageControlsItem implements Serializable {
		private static final long serialVersionUID = -8417416623706184272L;
		/**
		 * 流程名称与工作流程配置类RegisterWorkFlow配置的页面对应
		 */
		@XStreamAsAttribute
		private String name;
		/**
		 * 选择器禁用的控件ID列表
		 */
		@XStreamAlias("selectorname")
		private GX_ids selectornames = new GX_ids();
		/**
		 * 单元信息页禁用的控件ID列表
		 */
		@XStreamAlias("unitpagejsp")
		private GX_ids unitpagejsps = new GX_ids();
		/**
		 * 权利信息页禁用的控件ID列表
		 */
		@XStreamAlias("rightspagejsp")
		private GX_ids rightspagejsps = new GX_ids();
		/**
		 * 登记簿预览信息页禁用的控件ID列表
		 */
		@XStreamAlias("bookpagejsp")
		private GX_ids bookpagejsps = new GX_ids();

		public GX_PageControlsItem() {

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public GX_ids getSelectornames() {
			return selectornames;
		}

		public void setSelectornames(GX_ids selectornames) {
			this.selectornames = selectornames;
		}

		public GX_ids getUnitpagejsps() {
			return unitpagejsps;
		}

		public void setUnitpagejsps(GX_ids unitpagejsps) {
			this.unitpagejsps = unitpagejsps;
		}

		public GX_ids getRightspagejsps() {
			return rightspagejsps;
		}

		public void setRightspagejsps(GX_ids rightspagejsps) {
			this.rightspagejsps = rightspagejsps;
		}

		public GX_ids getBookpagejsps() {
			return bookpagejsps;
		}

		public void setBookpagejsps(GX_ids bookpagejsps) {
			this.bookpagejsps = bookpagejsps;
		}

		
	}
}
