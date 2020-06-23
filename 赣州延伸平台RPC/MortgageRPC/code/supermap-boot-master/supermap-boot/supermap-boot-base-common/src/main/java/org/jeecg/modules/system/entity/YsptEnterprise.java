package org.jeecg.modules.system.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("yspt_enterprise")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="yspt_enterprise对象", description="企业信息表")
public class YsptEnterprise {

	@Excel(name = "ID", width = 64)
	@ApiModelProperty(value = "ID")
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	
	@Excel(name = "企业名称", width = 150)
	@ApiModelProperty(value = "企业名称")
	private java.lang.String enterpriseName;
	
	@Excel(name = "企业统一社会信用代码", width = 50)
	@ApiModelProperty(value = "企业统一社会信用代码")
	private java.lang.String enterpriseCode;
	
	@Excel(name = "企业地址", width = 500)
	@ApiModelProperty(value = "企业地址")
	private java.lang.String enterpriseAddress;
	
	@Excel(name = "法定代表人姓名", width = 150)
	@ApiModelProperty(value = "法定代表人姓名")
	private java.lang.String frdbxm;
	
	@Excel(name = "法定代表人证件号码", width = 20)
	@ApiModelProperty(value = "法定代表人证件号码")
	private java.lang.String frdbzjhm;
	
	@Excel(name = "注册者姓名", width = 150)
	@ApiModelProperty(value = "注册者姓名")
	private java.lang.String registerName;
	
	@Excel(name = "注册者证件号码", width = 20)
	@ApiModelProperty(value = "注册者证件号码")
	private java.lang.String registerZjhm;
	
	@Excel(name = "注册者手机号码", width = 20)
	@ApiModelProperty(value = "注册者手机号码")
	private java.lang.String registerPhone;
	
	@Excel(name = "状态", width = 2)
	@ApiModelProperty(value = "状态")
	private java.lang.String status;
	
	@Excel(name = "删除标记", width = 1)
	@ApiModelProperty(value = "删除标记")
	private java.lang.String delFlag;
	
	@Excel(name = "创建者", width = 32)
	@ApiModelProperty(value = "创建者")
	private java.lang.String createBy;
	
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	
	@Excel(name = "修改者", width = 32)
	@ApiModelProperty(value = "修改者")
	private java.lang.String updateBy;
	
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date updateTime;
	
	@Excel(name = "内网反馈字段", width = 2000)
	@ApiModelProperty(value = "内网反馈字段")
	private java.lang.String msg;
	
	@Excel(name = "驳回验证码", width = 20)
	@ApiModelProperty(value = "驳回验证码")
	private java.lang.String bhYzm;
	
	
}
