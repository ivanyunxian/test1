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
 * @Description: 收费定义关系表
 * @Author: jeecg-boot
 * @Date:   2019-09-23
 * @Version: V1.0
 */
@Data
@TableName("BDCS_SFGX")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BDCS_SFGX对象", description="收费定义关系表")
public class Bdcs_sfgx {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**业务流水号*/
	@Excel(name = "业务流水号", width = 15)
    @ApiModelProperty(value = "业务流水号")
	private String ywlsh;
	/**收费表的id*/
	@Excel(name = "收费表的id", width = 15)
    @ApiModelProperty(value = "收费表的id")
	private String sfdyId;
}
