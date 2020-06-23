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
@TableName("BDC_ZRZ")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BDC_ZRZ对象", description="查询记录")
public class Bdc_zrz {

	/**主键ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**业务流水号*/
	@Excel(name = "企业id", width = 50)
    @ApiModelProperty(value = "企业id")
	private String enterpriseid;
	/**不动产单元id*/
	@Excel(name = "不动产单元id", width = 50)
    @ApiModelProperty(value = "不动产单元id")
	private String bdcdyid;
	/**不动产单元号*/
	@Excel(name = "不动产单元号", width = 100)
    @ApiModelProperty(value = "不动产单元号")
	private String bdcdyh;
	/**坐落*/
	@Excel(name = "坐落", width = 500)
    @ApiModelProperty(value = "坐落")
	private String zl;
	@Excel(name = "自然幢号", width = 50)
    @ApiModelProperty(value = "自然幢号")
	private String zrzh;
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
	/**宗地id*/
	@Excel(name = "宗地id", width = 15)
    @ApiModelProperty(value = "宗地id")
	private String zdid;
	
}
