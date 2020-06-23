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
 * @Description: 流程定义
 * @Author: jeecg-boot
 * @Date:   2019-07-31
 * @Version: V1.0
 */
@Data
@TableName("wfi_prodef")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wfi_prodef对象", description="流程定义")
public class Wfi_prodef {
    
	/**流程编号*/
	@Excel(name = "流程编号", width = 15)
    @ApiModelProperty(value = "流程编号")
	@TableId(value = "prodef_id",type = IdType.UUID)
	private java.lang.String prodefId;
	/**分类编码*/
	@Excel(name = "分类编码", width = 15)
    @ApiModelProperty(value = "分类编码")
	private java.lang.String prodefclassId;
	/**区划代码*/
	@Excel(name = "区划代码", width = 15)
    @ApiModelProperty(value = "区划代码")
	@Dict(dicCode = "DIVISION_CODE")
	private java.lang.String divisionCode;
	/**流程编码*/
	@Excel(name = "流程编码", width = 15)
    @ApiModelProperty(value = "流程编码")
	private java.lang.String prodefCode;
	/**流程名称*/
	@Excel(name = "流程名称", width = 15)
    @ApiModelProperty(value = "流程名称")
	private java.lang.String prodefName;
	/**流程状态*/
	@Excel(name = "流程状态", width = 15)
    @ApiModelProperty(value = "流程状态")
	private java.lang.Integer prodefStatus;
	/**业务类型*/
	@Excel(name = "业务类型", width = 15)
    @ApiModelProperty(value = "业务类型")
	private java.lang.String operationType;
	/**单元类型*/
	@Excel(name = "单元类型", width = 15)
    @ApiModelProperty(value = "单元类型")
	@Dict(dicCode = "BDCDYLX")
	private java.lang.String dylx;
	/**登记类型*/
	@Excel(name = "登记类型", width = 15)
    @ApiModelProperty(value = "登记类型")
	@Dict(dicCode = "DJLX")
	private java.lang.String djlx;
	/**权利类型*/
	@Excel(name = "权利类型", width = 15)
    @ApiModelProperty(value = "权利类型")
	@Dict(dicCode = "QLLX")
	private java.lang.String qllx;
	/**用户类型*/
	@Excel(name = "用户类型", width = 15)
    @ApiModelProperty(value = "用户类型")
	private java.lang.String yhlx;
	/**是即时时办理*/
	@Excel(name = "是即时时办理", width = 15)
    @ApiModelProperty(value = "是即时时办理")
	@Dict(dicCode = "yn")
	private java.lang.String sfjsbl;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
	@Dict(dicCode = "yn")
	private java.lang.String sfqy;
	/**流程索引*/
	@Excel(name = "流程索引", width = 15)
    @ApiModelProperty(value = "流程索引")
	private java.lang.Integer prodefIndex;
	/**流程描述*/
	@Excel(name = "流程描述", width = 15)
    @ApiModelProperty(value = "流程描述")
	private java.lang.String prodefDesc;
	/**公告模板*/
	@Excel(name = "公告模板", width = 15)
    @ApiModelProperty(value = "公告模板")
	private java.lang.String prodefTpl;
	/**模板路径*/
	@Excel(name = "模板路径", width = 15)
    @ApiModelProperty(value = "模板路径")
	private java.lang.String templateurl;
	/**受理信息表单地址*/
	@Excel(name = "受理信息表单地址", width = 15)
    @ApiModelProperty(value = "受理信息表单地址")
	private java.lang.String formurl;
	/**流程适用说明*/
	@Excel(name = "流程适用说明", width = 15)
    @ApiModelProperty(value = "流程适用说明")
	private java.lang.String sysm;
	/**一次性告知书文章的主键id*/
	@Excel(name = "一次性告知书文章的主键id", width = 15)
    @ApiModelProperty(value = "一次性告知书文章的主键id")
	private java.lang.String ycxgzsh;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createtime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date modifytime;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
	private java.lang.String versionno;
	/**权利人标识（是否需要权利人0-否，1-是）*/
	@Excel(name = "权利人标识（是否需要权利人0-否，1-是）", width = 15)
	@ApiModelProperty(value = "权利人标识（是否需要权利人0-否，1-是）")
	@Dict(dicCode = "yn")
	private java.lang.String qlrflage;
	/**义务人标识（是否需要义务人0-否，1-是）*/
	@Excel(name = "义务人标识（是否需要义务人0-否，1-是）", width = 15)
	@ApiModelProperty(value = "义务人标识（是否需要义务人0-否，1-是）")
	@Dict(dicCode = "yn")
	private java.lang.String ywrflage;
	/**抵押权人人标识（是否需要抵押权人0-否，1-是）*/
	@Excel(name = "抵押权人人标识（是否需要抵押权人0-否，1-是）", width = 15)
	@ApiModelProperty(value = "抵押权人人标识（是否需要抵押权人0-否，1-是）")
	@Dict(dicCode = "yn")
	private java.lang.String dyqrflage;
}
