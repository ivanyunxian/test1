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
 * @Description: 审核过程意见表
 * @Author: jeecg-boot
 * @Date:   2019-08-30
 * @Version: V1.0
 */
@Data
@TableName("WFI_SLXMSH")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WFI_SLXMSH对象", description="审核过程意见表")
public class Wfi_slxmsh {
    
	/**主键id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
	private String id;
	/**业务流水号*/
	@Excel(name = "业务流水号", width = 15)
    @ApiModelProperty(value = "业务流水号")
	private String prolsh;
	/**审核人员*/
	@Excel(name = "审核人员", width = 15)
    @ApiModelProperty(value = "审核人员")
	private String shry;
	/**审核意见*/
	@Excel(name = "审核意见", width = 15)
    @ApiModelProperty(value = "审核意见")
	private String shyj;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核时间")
	private Date shsj;
	/**项目审核状态*/
	@Excel(name = "项目审核状态", width = 15)
    @ApiModelProperty(value = "项目审核状态")
	@Dict(dicCode = "SHZT")
	private Integer shzt;
}
