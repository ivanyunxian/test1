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
 * @Description: 权利单元关系表
 * @Author: jeecg-boot
 * @Date:   2019-08-08
 * @Version: V1.0
 */
@Data
@TableName("BDC_QLDY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BDC_QLDY对象", description="权利单元关系表")
public class Bdc_qldy {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**prolsh*/
	@Excel(name = "prolsh", width = 15)
    @ApiModelProperty(value = "prolsh")
	private String prolsh;
	/**dyid*/
	@Excel(name = "dyid", width = 15)
    @ApiModelProperty(value = "dyid")
	private String dyid;
	/**qlid*/
	@Excel(name = "qlid", width = 15)
    @ApiModelProperty(value = "qlid")
	private String qlid;
	/**createtime*/
	@Excel(name = "createtime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createtime")
	private Date createtime;
	/**sqrid*/
	@Excel(name = "sqrid", width = 15)
	@ApiModelProperty(value = "sqrid")
	private String sqrid;
	/**附属权利ID*/
	@Excel(name = "fsqlid", width = 15)
	@ApiModelProperty(value = "fsqlid")
	private String fsqlid;
}
