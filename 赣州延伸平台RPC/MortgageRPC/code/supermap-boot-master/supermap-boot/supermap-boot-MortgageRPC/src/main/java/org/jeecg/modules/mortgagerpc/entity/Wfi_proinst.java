package org.jeecg.modules.mortgagerpc.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 项目信息表
 * @Author: jeecg-boot
 * @Date:   2019-07-31
 * @Version: V1.0
 */
@Data
@TableName("wfi_proinst")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wfi_proinst对象", description="项目信息表")
public class Wfi_proinst {

	/**流程实例ID*/
	@Excel(name = "流程实例ID", width = 15)
	@ApiModelProperty(value = "流程实例ID")
	@TableId(type = IdType.UUID)
	private java.lang.String proinstId;
	/**流程编号*/
	@Excel(name = "流程编号", width = 15)
	@ApiModelProperty(value = "流程编号")
	private java.lang.String prodefId;
	/**员工编号*/
	@Excel(name = "员工编号", width = 15)
	@ApiModelProperty(value = "员工编号")
	private java.lang.String userId;
	/**区划代码*/
	@Excel(name = "区划代码", width = 15)
	@ApiModelProperty(value = "区划代码")
	private java.lang.String divisionCode;
	/**区划名称*/
	@Excel(name = "区划名称", width = 15)
	@ApiModelProperty(value = "区划名称")
	private java.lang.String divisionName;
	/**业务类型（0:个人，1:企业）*/
	@Excel(name = "业务类型（0:个人，1:企业）", width = 15)
	@ApiModelProperty(value = "业务类型（0:个人，1:企业）")
	private java.lang.String ywlx;
	/**登记类型 登记类型字典*/
	@Excel(name = "登记类型 登记类型字典", width = 15)
	@ApiModelProperty(value = "登记类型 登记类型字典")
	private java.lang.String djlx;
	/**权利类型 权利类型字典*/
	@Excel(name = "权利类型 权利类型字典", width = 15)
	@ApiModelProperty(value = "权利类型 权利类型字典")
	private java.lang.String qllx;
	/**是否合并证书 证书个数字典*/
	@Excel(name = "是否合并证书 证书个数字典", width = 15)
	@ApiModelProperty(value = "是否合并证书 证书个数字典")
	private java.lang.String sfhbzs;
	/**项目类型 用于区分不同的需求，12：通用的，11：鹰潭*/
	@Excel(name = "项目类型 用于区分不同的需求，12：通用的，11：鹰潭", width = 15)
	@ApiModelProperty(value = "项目类型 用于区分不同的需求，12：通用的，11：鹰潭")
	private java.lang.String xmlx;
	/**审核状态 -1：拟申请未提交到前置机，0：未审核，10：权籍调查审核通过，11：权籍调查审核驳回，20：项目受理审核通过，21：项目受理审核驳回*/
	@Excel(name = "审核状态 -1：未提交，0：未审核，11：已驳回，20：审核中，30：已登簿", width = 15)
	@ApiModelProperty(value = "审核状态 -1：未提交，0：未审核，11：已驳回，20：审核中，30：已登簿")
	@Dict(dicCode = "SHZT")
	private java.lang.Integer shzt;
	/**流水号 外网流水号*/
	@Excel(name = "流水号 外网流水号", width = 15)
	@ApiModelProperty(value = "流水号 外网流水号")
	private java.lang.String prolsh;
	/**流水号 内网创建项目生成的流水号*/
	@Excel(name = "流水号 内网创建项目生成的流水号", width = 15)
	@ApiModelProperty(value = "流水号 内网创建项目生成的流水号")
	private java.lang.String lsh;
	/**内网库流程ID 受理项目对应到内网库的流程ID*/
	@Excel(name = "内网库流程ID 受理项目对应到内网库的流程ID", width = 15)
	@ApiModelProperty(value = "内网库流程ID 受理项目对应到内网库的流程ID")
	private java.lang.String wfProdefid;
	/**内网流程名称 对应到内网的流程名称*/
	@Excel(name = "内网流程名称 对应到内网的流程名称", width = 15)
	@ApiModelProperty(value = "内网流程名称 对应到内网的流程名称")
	private java.lang.String wfProdefname;
	/**流程名称*/
	@Excel(name = "流程名称", width = 15)
	@ApiModelProperty(value = "流程名称")
	private java.lang.String prodefName;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
	@ApiModelProperty(value = "项目名称")
	private java.lang.String projectName;
	/**实例办理方式*/
	@Excel(name = "实例办理方式", width = 15)
	@ApiModelProperty(value = "实例办理方式")
	private java.lang.Integer transationType;
	/**流程编码*/
	@Excel(name = "流程编码", width = 15)
	@ApiModelProperty(value = "流程编码")
	private java.lang.String proinstCode;
	/**起始时间*/
	@Excel(name = "起始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "起始时间")
	private java.util.Date proinstStart;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "结束时间")
	private java.util.Date proinstEnd;
	/**流程实例状态*/
	@Excel(name = "流程实例状态", width = 15)
	@ApiModelProperty(value = "流程实例状态")
	private java.lang.Integer proinstStatus;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date creatDate;
	/**受理人员*/
	@Excel(name = "受理人员", width = 15)
	@ApiModelProperty(value = "受理人员")
	private java.lang.String acceptor;
	/**员工地区ID*/
	@Excel(name = "员工地区ID", width = 15)
	@ApiModelProperty(value = "员工地区ID")
	private java.lang.String staffDistid;
	/**流程实例类型*/
	@Excel(name = "流程实例类型", width = 15)
	@ApiModelProperty(value = "流程实例类型")
	private java.lang.Integer instanceType;
	/**发送消息状态 0：不需要发送消息，1：需要推送消息*/
	@Excel(name = "发送消息状态 0：不需要发送消息，1：需要推送消息", width = 15)
	@ApiModelProperty(value = "发送消息状态 0：不需要发送消息，1：需要推送消息")
	private java.lang.Integer sendMsg;
	/**项目备注*/
	@Excel(name = "项目备注", width = 15)
	@ApiModelProperty(value = "项目备注")
	private java.lang.String remarks;
	/**用户部门id*/
	@Excel(name = "用户部门id", width = 15)
    @ApiModelProperty(value = "用户部门id")
	private String departid;
	
	/**登簿时间*/
	@Excel(name = "登簿时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "登簿时间")
	private java.util.Date dbsj;

	/**业务来源（0：抵押平台，1：金融机构）*/
	@Excel(name = "业务来源", width = 15)
	@ApiModelProperty(value = "业务来源")
	private String ywly;
	/**机构流水号*/
	@Excel(name = "机构流水号", width = 15)
	@ApiModelProperty(value = "机构流水号")
	private String orgLsh;
	/**回调状态*/
	@Excel(name = "回调状态", width = 15)
	@ApiModelProperty(value = "回调状态")
	private String hdzt;
}
