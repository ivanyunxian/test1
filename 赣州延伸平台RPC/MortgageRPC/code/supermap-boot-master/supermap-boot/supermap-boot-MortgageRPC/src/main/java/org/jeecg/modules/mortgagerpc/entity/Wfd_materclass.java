package org.jeecg.modules.mortgagerpc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 流程资料目录模板表
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Data
@TableName("wfd_materclass")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wfd_materclass对象", description="流程资料目录模板表")
public class Wfd_materclass {
    
	/**目录ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "目录ID")
	private java.lang.String id;
	/**扩展*/
	@Excel(name = "扩展", width = 15)
    @ApiModelProperty(value = "扩展")
	private java.lang.String matedesc;
	/**目录序号*/
	@Excel(name = "目录序号", width = 15)
    @ApiModelProperty(value = "目录序号")
	private java.lang.Integer fileindex;
	/**目录名称*/
	@Excel(name = "目录名称", width = 15)
    @ApiModelProperty(value = "目录名称")
	private java.lang.String name;
	/**流程ID*/
	@Excel(name = "流程ID", width = 15)
    @ApiModelProperty(value = "流程ID")
	private java.lang.String procodeid;
	/**是否必须 0-否 1-是*/
	@Excel(name = "是否必须 0-否 1-是", width = 15)
    @ApiModelProperty(value = "是否必须 0-否 1-是")
	private java.lang.String required;
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
	private java.lang.String divisionCode;
}
