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
 * @Description: 接口注册管理表
 * @Author: jeecg-boot
 * @Date:   2019-09-20
 * @Version: V1.0
 */
@Data
@TableName("SYS_APIS")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SYS_APIS对象", description="接口注册管理表")
public class Sys_apis {
    
	/**id主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id主键")
	private String id;
	/**父级id*/
	@Excel(name = "父级id", width = 15)
    @ApiModelProperty(value = "父级id")
	private String parentId;
	/**接口名称*/
	@Excel(name = "接口名称", width = 15)
    @ApiModelProperty(value = "接口名称")
	private String name;
	/**接口编码*/
	@Excel(name = "接口编码", width = 15)
    @ApiModelProperty(value = "接口编码")
	private String apicode;
	/**接口地址*/
	@Excel(name = "接口地址", width = 15)
    @ApiModelProperty(value = "接口地址")
	private String apipath;
	/**接口描述*/
	@Excel(name = "接口描述", width = 15)
    @ApiModelProperty(value = "接口描述")
	private String apidesc;
	/**接口状态（1-启用，0-不启用）*/
	@Excel(name = "接口状态（1-启用，0-不启用）", width = 15)
    @ApiModelProperty(value = "接口状态（1-启用，0-不启用）")
	private String state;
	/**创建人员*/
	@Excel(name = "创建人员", width = 15)
    @ApiModelProperty(value = "创建人员")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
}
