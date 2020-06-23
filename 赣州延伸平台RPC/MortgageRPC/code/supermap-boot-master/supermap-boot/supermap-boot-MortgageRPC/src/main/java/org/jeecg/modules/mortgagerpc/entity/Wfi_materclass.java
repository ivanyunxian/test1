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
 * @Description: 工作流资料目录
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Data
@TableName("Wfi_materclass")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Wfi_materclass对象", description="工作流资料目录")
public class Wfi_materclass {
    
	/**目录ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "目录ID")
	private String id;
	/**项目流水号*/
	@Excel(name = "项目流水号", width = 15)
    @ApiModelProperty(value = "项目流水号")
	private String prolsh;
	/**扩展*/
	@Excel(name = "扩展", width = 15)
    @ApiModelProperty(value = "扩展")
	private String matedesc;
	/**目录序号*/
	@Excel(name = "目录序号", width = 15)
    @ApiModelProperty(value = "目录序号")
	private Integer fileindex;
	/**目录名称*/
	@Excel(name = "目录名称", width = 15)
    @ApiModelProperty(value = "目录名称")
	private String name;
	/**流程ID*/
	@Excel(name = "流程ID", width = 15)
    @ApiModelProperty(value = "流程ID")
	private String procodeid;
	/**是否必须 0-否 1-是*/
	@Excel(name = "是否必须 0-否 1-是", width = 15)
    @ApiModelProperty(value = "是否必须 0-否 1-是")
	private String required;
	/**是否读取电子证照 0-否 1-是*/
	@Excel(name = "是否读取电子证照 0-否 1-是", width = 10)
    @ApiModelProperty(value = "是否读取电子证照 0-否 1-是")
	private java.lang.String ecert;
	/**电子证照类型 数据字典决定*/
	@Excel(name = "电子证照类型 数据字典决定", width = 10)
    @ApiModelProperty(value = "电子证照类型 数据字典决定")
	private java.lang.String ecertCode;
	/**行政区划*/
	@Excel(name = "行政区划", width = 15)
    @ApiModelProperty(value = "行政区划")
	private String divisionCode;
	@Excel(name = "附件数量", width = 15)
	@ApiModelProperty(value = "附件数量")
	private Integer count;

	@TableField(exist = false)
	private String materdataindex ;
}
