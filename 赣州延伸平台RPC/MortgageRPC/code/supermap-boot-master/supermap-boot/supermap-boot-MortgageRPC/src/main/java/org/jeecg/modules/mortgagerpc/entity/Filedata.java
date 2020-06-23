package org.jeecg.modules.mortgagerpc.entity;

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
@TableName("filedata")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="filedata对象", description="模板附件资料表")
public class Filedata {

	/**编号 ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "编号 ID")
	private java.lang.String id;
	/**资料名称 NAME*/
	@Excel(name = "资料名称 NAME", width = 15)
    @ApiModelProperty(value = "资料名称 NAME")
	private java.lang.String name;
	/**路径 PATH*/
	@Excel(name = "路径 PATH", width = 15)
    @ApiModelProperty(value = "路径 PATH")
	private java.lang.String path;
	/**扩展名 SUFFIX*/
	@Excel(name = "扩展名 SUFFIX", width = 15)
    @ApiModelProperty(value = "扩展名 SUFFIX")
	private java.lang.String suffix;
	/**上传时间 CREATED*/
	@Excel(name = "上传时间 CREATED", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上传时间 CREATED")
	private java.util.Date created;
	/**OPBY*/
	@Excel(name = "OPBY", width = 15)
    @ApiModelProperty(value = "OPBY")
	private java.lang.String opby;
	/**状态 STATUS*/
	@Excel(name = "状态 STATUS", width = 15)
    @ApiModelProperty(value = "状态 STATUS")
	private java.lang.Integer status;
	/**文件序号*/
	@Excel(name = "文件序号", width = 15)
    @ApiModelProperty(value = "文件序号")
	private java.lang.Long fileindex;
	@Excel(name = "mongoid", width = 15)
	@ApiModelProperty(value = "mongoid")
	private java.lang.String mongoid;
}
