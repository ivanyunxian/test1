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
 * @Description: 收费定义表
 * @Author: jeecg-boot
 * @Date:   2019-09-23
 * @Version: V1.0
 */
@Data
@TableName("BDCS_SFDY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BDCS_SFDY对象", description="收费定义表")
public class Bdcs_sfdy {
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**收费大类名称*/
	@Excel(name = "收费大类名称", width = 15)
    @ApiModelProperty(value = "收费大类名称")
	private String sfdlmc;
	/**收费小类名称*/
	@Excel(name = "收费小类名称", width = 15)
    @ApiModelProperty(value = "收费小类名称")
	private String sfxlmc;
	/**收费科目名称*/
	@Excel(name = "收费科目名称", width = 15)
    @ApiModelProperty(value = "收费科目名称")
	private String sfkmmc;
	/**收费类型 %SFLX%*/
	@Excel(name = "收费类型 %SFLX%", width = 15)
    @ApiModelProperty(value = "收费类型 %SFLX%")
	private String sflx;
	/**收费基数*/
	@Excel(name = "收费基数", width = 15)
    @ApiModelProperty(value = "收费基数")
	private java.math.BigDecimal sfjs;
	/**面积基数*/
	@Excel(name = "面积基数", width = 15)
    @ApiModelProperty(value = "面积基数")
	private java.math.BigDecimal mjjs;
	/**面积增量*/
	@Excel(name = "面积增量", width = 15)
    @ApiModelProperty(value = "面积增量")
	private java.math.BigDecimal mjzl;
	/**收费增量*/
	@Excel(name = "收费增量", width = 15)
    @ApiModelProperty(value = "收费增量")
	private java.math.BigDecimal sfzl;
	/**增量费用上限*/
	@Excel(name = "增量费用上限", width = 15)
    @ApiModelProperty(value = "增量费用上限")
	private java.math.BigDecimal zlfysx;
	/**收费比例*/
	@Excel(name = "收费比例", width = 15)
    @ApiModelProperty(value = "收费比例")
	private java.math.BigDecimal sfbl;
	/**计算公式*/
	@Excel(name = "计算公式", width = 15)
    @ApiModelProperty(value = "计算公式")
	private String jsgs;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
	private Date creatTime;
	/**收费单位*/
	@Excel(name = "收费单位", width = 15)
    @ApiModelProperty(value = "收费单位")
	private String sfdw;
	/**bz*/
	@Excel(name = "bz", width = 15)
    @ApiModelProperty(value = "bz")
	private String bz;
	/**计算方式*/
	@Excel(name = "计算方式", width = 15)
    @ApiModelProperty(value = "计算方式")
	private String caltype;
	/**计算SQL语句*/
	@Excel(name = "计算SQL语句", width = 15)
    @ApiModelProperty(value = "计算SQL语句")
	private String sqlexp;
	/**计算个数的SQL语句*/
	@Excel(name = "计算个数的SQL语句", width = 15)
    @ApiModelProperty(value = "计算个数的SQL语句")
	private String cacsql;
	/**收费标志码*/
	@Excel(name = "收费标志码", width = 15)
    @ApiModelProperty(value = "收费标志码")
	private String symbol;
	/**收费部门*/
	@Excel(name = "收费部门", width = 15)
    @ApiModelProperty(value = "收费部门")
	private String sfbmmc;
	/**收费统计标志*/
	@Excel(name = "收费统计标志", width = 15)
    @ApiModelProperty(value = "收费统计标志")
	private String tjbz;
}
