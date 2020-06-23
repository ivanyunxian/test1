package com.supermap.realestate.registration.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("workflows")
public class PageControlsConfig implements Serializable {
	/**
	 * 页面控制配置类，用于XML的反序列化。与工作流程配置类RegisterWorkFlow配置的页面对应 WUZHU
	 */
	private static final long serialVersionUID = 8417416623709184173L;

	/**
	 * 某个流程的配置项
	 */
	@XStreamImplicit(itemFieldName = "Workflow")
	private List<PageControlsItem> Workflows = new ArrayList<PageControlsItem>();

	public PageControlsConfig() {

	}

	public List<PageControlsItem> getWorkflows() {
		return Workflows;
	}

	public void setWorkflows(List<PageControlsItem> workflows) {
		Workflows = workflows;
	}
	public class ids implements Serializable {
		private static final long serialVersionUID = -8417416623706194272L;
		/**
		 * 隐藏ID集合,用#将各ID合并（之所以用#因为该字符在中文和英文下都是相同的）
		 */
		@XStreamAsAttribute
		private String hiddenids;
		@XStreamAsAttribute
		private String showids;
		@XStreamAsAttribute
		/**
		 * 禁用ID集合
		 */
		private String  disabledids;
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
		
		public String getShowids() {
			return showids;
		}
		public void setShowids(String showids) {
			this.showids = showids;
		}
	}
	public class PageControlsItem implements Serializable {
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
		private ids selectornames = new ids();
		/**
		 * 单元信息页禁用的控件ID列表
		 */
		@XStreamAlias("unitpagejsp")
		private ids unitpagejsps = new ids();
		/**
		 * 权利信息页禁用的控件ID列表
		 */
		@XStreamAlias("rightspagejsp")
		private ids rightspagejsps = new ids();
		/**
		 * 登记簿预览信息页禁用的控件ID列表
		 */
		@XStreamAlias("bookpagejsp")
		private ids bookpagejsps = new ids();

		public PageControlsItem() {

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ids getSelectornames() {
			return selectornames;
		}

		public void setSelectornames(ids selectornames) {
			this.selectornames = selectornames;
		}

		public ids getUnitpagejsps() {
			return unitpagejsps;
		}

		public void setUnitpagejsps(ids unitpagejsps) {
			this.unitpagejsps = unitpagejsps;
		}

		public ids getRightspagejsps() {
			return rightspagejsps;
		}

		public void setRightspagejsps(ids rightspagejsps) {
			this.rightspagejsps = rightspagejsps;
		}

		public ids getBookpagejsps() {
			return bookpagejsps;
		}

		public void setBookpagejsps(ids bookpagejsps) {
			this.bookpagejsps = bookpagejsps;
		}

		
	}
}
