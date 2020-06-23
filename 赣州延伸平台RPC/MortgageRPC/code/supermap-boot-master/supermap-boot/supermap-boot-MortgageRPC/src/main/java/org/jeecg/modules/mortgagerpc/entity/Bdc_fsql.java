package org.jeecg.modules.mortgagerpc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 附属权利表
 * @Author: jeecg-boot
 * @Date:   2019-08-28
 * @Version: V1.0
 */
@Data
@TableName("BDC_FSQL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BDC_FSQL对象", description="附属权利表")
public class Bdc_fsql {
    
	/**附属权利ID*/
	@Excel(name = "附属权利ID", width = 15)
    @ApiModelProperty(value = "附属权利ID")
	@TableId(type = IdType.UUID)
	private String fsqlid;
	/**权利ID*/
	@Excel(name = "权利ID", width = 15)
    @ApiModelProperty(value = "附属权利ID")
	private String qlid;
	/**业务流水号*/
	@Excel(name = "业务流水号", width = 15)
    @ApiModelProperty(value = "业务流水号")
	private String prolsh;
	/**抵押人*/
	@Excel(name = "抵押人", width = 15)
    @ApiModelProperty(value = "抵押人")
	private String dyr;
	/**债务人*/
	@Excel(name = "债务人", width = 15)
    @ApiModelProperty(value = "债务人")
	private String zwr;
	/**债务人证件号*/
	@Excel(name = "债务人证件号", width = 15)
    @ApiModelProperty(value = "债务人证件号")
	private String zwrzjh;
	/**在建建筑物抵押范围*/
	@Excel(name = "在建建筑物抵押范围", width = 15)
    @ApiModelProperty(value = "在建建筑物抵押范围")
	private String zjjzwdyfw;
	/**最高债权确定事实*/
	@Excel(name = "最高债权确定事实", width = 15)
    @ApiModelProperty(value = "最高债权确定事实")
	private String zgzqqdss;
	/**单个被担保主债权数额*/
	@Excel(name = "单个被担保主债权数额", width = 15)
    @ApiModelProperty(value = "单个被担保主债权数额")
	private java.lang.Double dgbdbzzqse;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
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
	/**抵押物类型--DYBDCLX*/
	@Excel(name = "抵押物类型", width = 15)
	@ApiModelProperty(value = "抵押物类型")
	private java.lang.String dywlx;
	/**房屋性质--FWXZ*/
	@Excel(name = "房屋性质", width = 15)
	@ApiModelProperty(value = "房屋性质")
	private java.lang.String fwxz;
}
