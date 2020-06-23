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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 风险防控
 * @Author: jeecg-boot
 * @Date:   2019-09-28
 * @Version: V1.0
 */
@Data
@TableName("Bdc_riskinfo")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Bdc_riskinfo对象", description="风险防控")
public class Bdc_riskinfo {
    
	/**权利id*/
	@Excel(name = "权利id", width = 15)
    @ApiModelProperty(value = "权利id")
	private String qlid;
	/**不动产单元号*/
	@Excel(name = "不动产单元号", width = 15)
    @ApiModelProperty(value = "不动产单元号")
	private String bdcdyh;
	/**不动产权证号*/
	@Excel(name = "不动产权证号", width = 15)
    @ApiModelProperty(value = "不动产权证号")
	private String bdcqzh;
	/**登记单元id*/
	@Excel(name = "登记单元id", width = 15)
    @ApiModelProperty(value = "登记单元id")
	private String djdyid;
	/**坐落*/
	@Excel(name = "坐落", width = 15)
    @ApiModelProperty(value = "坐落")
	private String zl;
	/**业务来源（0：登记系统，1：最多跑一次平台，2：远程报件，3：抵押平台，4：微信，5：金融机构api，6：公积金api ）*/
	@Excel(name = "业务来源（0：登记系统，1：最多跑一次平台，2：远程报件，3：抵押平台，4：微信，5：金融机构api，6：公积金api ）", width = 15)
    @ApiModelProperty(value = "业务来源（0：登记系统，1：最多跑一次平台，2：远程报件，3：抵押平台，4：微信，5：金融机构api，6：公积金api ）")
	private String ywly;
	/**内网流水号*/
	@Excel(name = "内网流水号", width = 15)
    @ApiModelProperty(value = "内网流水号")
	private String ywlsh;
	/**外流水号*/
	@Excel(name = "外流水号", width = 15)
    @ApiModelProperty(value = "外流水号")
	private String wlsh;
	/**单元状态（YY，异议登记；CF，查封；XZ，限制；DY抵押）*/
	@Excel(name = "单元状态（YY，异议登记；CF，查封；XZ，限制；DY抵押）", width = 15)
    @ApiModelProperty(value = "单元状态（YY，异议登记；CF，查封；XZ，限制；DY抵押）")
	private String dyzt;
	/**登簿时间*/
	@Excel(name = "登簿时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "登簿时间")
	private Date dbsj;
	/**行政区代码*/
	@Excel(name = "行政区代码", width = 15)
    @ApiModelProperty(value = "行政区代码")
	private String divisionCode;
	/**部门id*/
	@Excel(name = "部门id", width = 15)
    @ApiModelProperty(value = "部门id")
	private String departid;
	/**限制类型*/
	@Excel(name = "限制类型", width = 15)
    @ApiModelProperty(value = "限制类型")
	private String xzlx;
	/**限制起始日期*/
	@Excel(name = "限制起始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "限制起始日期")
	private Date xzqsrq;
	/**限制终止日期*/
	@Excel(name = "限制终止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "限制终止日期")
	private Date xzzzrq;
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
}
