package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 部门配置表
 * @Author: jeecg-boot
 * @Date:   2019-09-10
 * @Version: V1.0
 */
@Data
@TableName("sys_depart_config")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_depart_config对象", description="部门配置表")
public class SysDepartConfig {
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**机构ID*/
	@Excel(name = "机构ID", width = 15)
    @ApiModelProperty(value = "机构ID")
	private String deptId;
	/**用户名（机构提供的用户名）*/
	@Excel(name = "用户名（机构提供的用户名）", width = 15)
    @ApiModelProperty(value = "用户名（机构提供的用户名）")
	private String username;
	/**密码（机构提供的密码）*/
	@Excel(name = "密码（机构提供的密码）", width = 15)
    @ApiModelProperty(value = "密码（机构提供的密码）")
	private String password;
	/**区划代码*/
	@Excel(name = "区划代码", width = 15)
    @ApiModelProperty(value = "区划代码")
	private String divisionCode;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private String name;
	/**参数值*/
	@Excel(name = "参数值", width = 15)
    @ApiModelProperty(value = "参数值")
	private String value;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
	private String type;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
	private Date createtime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String memo;
	/**系统参数代码*/
	@Excel(name = "系统参数代码", width = 15)
    @ApiModelProperty(value = "系统参数代码")
	private String configKey;
}
