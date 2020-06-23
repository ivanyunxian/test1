package org.jeecg.modules.system.entity;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 接口申请内容表
 * @Author: jeecg-boot
 * @Date:   2019-09-20
 * @Version: V1.0
 */
@Data
@TableName("SYS_API_APPLY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SYS_API_APPLY对象", description="接口申请内容表")
public class Sys_api_apply {
    
	/**主键id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
	private String id;
	/**申请人名称*/
	@Excel(name = "申请人名称", width = 15)
    @ApiModelProperty(value = "申请人名称")
	private String sqrname;
	/**所属机构名称*/
	@Excel(name = "所属机构名称", width = 15)
    @ApiModelProperty(value = "所属机构名称")
	private String ssjgmc;
	/**申请起始时间*/
	@Excel(name = "申请起始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "申请起始时间")
	private Date sqqssj;
	/**申请结束时间*/
	@Excel(name = "申请结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "申请结束时间")
	private Date sqjssj;
	/**申请理由*/
	@Excel(name = "申请理由", width = 15)
    @ApiModelProperty(value = "申请理由")
	private String sqly;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
    @ApiModelProperty(value = "审核状态")
	private String shzt;
	/**审核人员id*/
	@Excel(name = "审核人员id", width = 15)
    @ApiModelProperty(value = "审核人员id")
	private String shryid;
	/**审核人员名称*/
	@Excel(name = "审核人员名称", width = 15)
    @ApiModelProperty(value = "审核人员名称")
	private String shryname;
	/**审核意见*/
	@Excel(name = "审核意见", width = 15)
    @ApiModelProperty(value = "审核意见")
	private String shyj;
	/**申请日期*/
	@Excel(name = "申请日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "申请日期")
	private Date sqrq;
	/**审核日期*/
	@Excel(name = "审核日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核日期")
	private Date shrq;
}
