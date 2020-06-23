package org.jeecg.modules.mortgagerpc.entity;

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
 * @Description: 上一手权利信息
 * @Author: jeecg-boot
 * @Date:   2019-09-18
 * @Version: V1.0
 */
@Data
@TableName("BDC_LAST_QL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BDC_LAST_QL对象", description="上一手权利信息")
public class Bdc_last_ql {
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
	private String prolsh;
	/**dyid*/
	@Excel(name = "dyid", width = 15)
    @ApiModelProperty(value = "dyid")
	private String dyid;
	/**抵押人*/
	@Excel(name = "抵押人", width = 15)
    @ApiModelProperty(value = "抵押人")
	private String dyr;
	/**区划代码*/
	@Excel(name = "区划代码", width = 15)
    @ApiModelProperty(value = "区划代码")
	private String divisionCode;
	/**抵押物类型*/
	@Excel(name = "抵押物类型", width = 15)
    @ApiModelProperty(value = "抵押物类型")
	private String dywlx;
	/**坐落*/
	@Excel(name = "坐落", width = 15)
    @ApiModelProperty(value = "坐落")
	private String zl;
	/**权利起始时间*/
	@Excel(name = "权利起始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "权利起始时间")
	private Date qlqssj;
	/**权利结束时间*/
	@Excel(name = "权利结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "权利结束时间")
	private Date qljssj;
	/**抵押方式*/
	@Excel(name = "抵押方式", width = 15)
    @ApiModelProperty(value = "抵押方式")
	private String dyfs;
	/**抵押金额*/
	@Excel(name = "抵押金额", width = 15)
    @ApiModelProperty(value = "抵押金额")
	private java.lang.Double dyje;
	/**持证方式*/
	@Excel(name = "持证方式", width = 15)
    @ApiModelProperty(value = "持证方式")
	private String czfs;
	/**是否合并证书*/
	@Excel(name = "是否合并证书", width = 15)
    @ApiModelProperty(value = "是否合并证书")
	private String sfhbzs;
	/**抵押评估价值*/
	@Excel(name = "抵押评估价值", width = 15)
    @ApiModelProperty(value = "抵押评估价值")
	private java.lang.Double dypgjz;
	/**在建建筑物抵押范围*/
	@Excel(name = "在建建筑物抵押范围", width = 15)
    @ApiModelProperty(value = "在建建筑物抵押范围")
	private String zjjzwdyfw;
	/**最高债权确定事实*/
	@Excel(name = "最高债权确定事实", width = 15)
    @ApiModelProperty(value = "最高债权确定事实")
	private String zgzqqdss;
	/**债务人*/
	@Excel(name = "债务人", width = 15)
    @ApiModelProperty(value = "债务人")
	private String zwr;
	/**登记原因*/
	@Excel(name = "登记原因", width = 15)
    @ApiModelProperty(value = "登记原因")
	private String djyy;
	/**附记*/
	@Excel(name = "附记", width = 15)
    @ApiModelProperty(value = "附记")
	private String fj;
	/**注销抵押原因*/
	@Excel(name = "注销抵押原因", width = 15)
    @ApiModelProperty(value = "注销抵押原因")
	private String zxdyyy;
	/**注销附记*/
	@Excel(name = "注销附记", width = 15)
    @ApiModelProperty(value = "注销附记")
	private String zxfj;
}
