package org.jeecg.modules.mortgagerpc.entity;

import java.util.Date;

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
@TableName("BDC_SHYQZD")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BDC_SHYQZD对象", description="查询记录")
public class Bdc_shyqzd {

	/**主键ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**业务流水号*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
	private String enterpriseid;
	/**不动产单元号*/
	@Excel(name = "不动产单元号", width = 15)
    @ApiModelProperty(value = "不动产单元号")
	private String bdcdyh;
	/**坐落*/
	@Excel(name = "坐落", width = 15)
    @ApiModelProperty(value = "坐落")
	private String zl;
	/**预测建筑面积*/
	@Excel(name = "批准建筑面积", width = 15)
    @ApiModelProperty(value = "批准建筑面积")
	private Double pzmj;
	/**cfzt*/
	@Excel(name = "cfzt", width = 15)
    @ApiModelProperty(value = "cfzt")
	private String cfzt;
	/**抵押状态*/
	@Excel(name = "抵押状态", width = 15)
    @ApiModelProperty(value = "抵押状态")
	private String dyzt;
	/**异议状态*/
	@Excel(name = "异议状态", width = 15)
    @ApiModelProperty(value = "异议状态")
	private String yyzt;
	/**房源接口返回内容*/
	@Excel(name = "宗地接口返回内容", width = 15)
    @ApiModelProperty(value = "宗地接口返回内容")
	private String zdclob;
	/**操作人员*/
	@Excel(name = "操作人员", width = 15)
    @ApiModelProperty(value = "操作人员")
	private String operator;
	/**操作时间*/
	@Excel(name = "操作时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "操作时间")
	private Date createtime;
	/**宗地面积*/
	@Excel(name = "宗地面积", width = 15)
	@ApiModelProperty(value = "宗地面积")
	private Double zdmj;
	/**状态*/
	@Excel(name = "状态", width = 1)
    @ApiModelProperty(value = "状态")
	private String status;
}
