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
 * @Description: 权利相关
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("bdc_ql")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="bdc_ql对象", description="权利相关")
public class Bdc_ql {
    
	/**权利ID*/
	@Excel(name = "权利ID", width = 15)
    @ApiModelProperty(value = "权利ID")
	@TableId(type = IdType.UUID)
	private java.lang.String qlid;
	/**权利ID*/
	@Excel(name = "附属权利ID", width = 15)
    @ApiModelProperty(value = "附属权利ID")
	@TableId(type = IdType.UUID)
	private java.lang.String fsqlid;
	/**流程实例ID*/
	@Excel(name = "流程实例ID", width = 15)
    @ApiModelProperty(value = "流程实例ID")
	private java.lang.String proinstId;
	/**不动产单元号，关联单元表*/
	@Excel(name = "不动产单元号，关联单元表", width = 15)
    @ApiModelProperty(value = "不动产单元号，关联单元表")
	private java.lang.String bdcdyh;
	/**流水号 外网流水号*/
	@Excel(name = "流水号 外网流水号", width = 15)
    @ApiModelProperty(value = "流水号 外网流水号")
	private java.lang.String prolsh;
	/**区县代码*/
	@Excel(name = "区县代码", width = 15)
    @ApiModelProperty(value = "区县代码")
	private java.lang.String divisionCode;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
    @ApiModelProperty(value = "合同号")
	private java.lang.String hth;
	/**A.8权利类型字典表*/
	@Excel(name = "A.8权利类型字典表", width = 15)
    @ApiModelProperty(value = "A.8权利类型字典表")
	private java.lang.String qllx;
	/**表A.21 登记类型字典表*/
	@Excel(name = "表A.21 登记类型字典表", width = 15)
    @ApiModelProperty(value = "表A.21 登记类型字典表")
	private java.lang.String djlx;
	/**登记原因*/
	@Excel(name = "登记原因", width = 15)
    @ApiModelProperty(value = "登记原因")
	private java.lang.String djyy;
	/**权利起始时间*/
	@Excel(name = "权利起始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "权利起始时间")
	private java.util.Date qlqssj;
	/**权利结束时间*/
	@Excel(name = "权利结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "权利结束时间")
	private java.util.Date qljssj;
	/**抵押人*/
	@Excel(name = "抵押人", width = 15)
    @ApiModelProperty(value = "抵押人")
	private java.lang.String dyr;
	/**抵押评估价值*/
	@Excel(name = "抵押评估价值", width = 15)
    @ApiModelProperty(value = "抵押评估价值")
	private java.lang.Double dypgjz;
	/**抵押金额*/
	@Excel(name = "抵押金额", width = 15)
    @ApiModelProperty(value = "抵押金额")
	private java.lang.Double dyje;
	/**抵押类型*/
	@Excel(name = "抵押类型", width = 15)
    @ApiModelProperty(value = "抵押类型")
	@Dict(dicCode = "DYFS")
	private java.lang.String dylx;
	/**抵押期限*/
	@Excel(name = "抵押期限", width = 15)
    @ApiModelProperty(value = "抵押期限")
	private java.lang.String dyqx;
	/**登记机构*/
	@Excel(name = "登记机构", width = 15)
    @ApiModelProperty(value = "登记机构")
	private java.lang.String djjg;
	/**登簿人*/
	@Excel(name = "登簿人", width = 15)
    @ApiModelProperty(value = "登簿人")
	private java.lang.String dbr;
	/**登记时间*/
	@Excel(name = "登记时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "登记时间")
	private java.util.Date djsj;
	/**附记*/
	@Excel(name = "附记", width = 15)
    @ApiModelProperty(value = "附记")
	private java.lang.String fj;
	/**权属状态*/
	@Excel(name = "权属状态", width = 15)
    @ApiModelProperty(value = "权属状态")
	private java.lang.Integer qszt;
	/**权利类型名称*/
	@Excel(name = "权利类型名称", width = 15)
    @ApiModelProperty(value = "权利类型名称")
	private java.lang.String qllxmc;
	/**证书编号*/
	@Excel(name = "证书编号", width = 15)
    @ApiModelProperty(value = "证书编号")
	private java.lang.String zsbh;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	private java.lang.String zt;
	/**A.59持证方式字典表*/
	@Excel(name = "A.59持证方式字典表", width = 15)
    @ApiModelProperty(value = "A.59持证方式字典表")
	private java.lang.String czfs;
	/**有效标志*/
	@Excel(name = "有效标志", width = 15)
    @ApiModelProperty(value = "有效标志")
	private java.lang.String yxbz;
	/**A.60证书版式字典表*/
	@Excel(name = "A.60证书版式字典表", width = 15)
    @ApiModelProperty(value = "A.60证书版式字典表")
	private java.lang.String zsbs;
	/**注销业务号*/
	@Excel(name = "注销业务号", width = 15)
    @ApiModelProperty(value = "注销业务号")
	private java.lang.String zxdyywh;
	/**注销抵押原因*/
	@Excel(name = "注销抵押原因", width = 15)
    @ApiModelProperty(value = "注销抵押原因")
	private java.lang.String zxdyyy;
	/**注销时间*/
	@Excel(name = "注销时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "注销时间")
	private java.util.Date zxsj;
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
	/**注销附记*/
	@Excel(name = "注销附记", width = 15)
	@ApiModelProperty(value = "注销附记")
	private java.lang.String zxfj;
	/**抵押物类型--DYBDCLX*/
	@Excel(name = "抵押物类型", width = 15)
	@ApiModelProperty(value = "抵押物类型")
	private java.lang.String dywlx;
	/**债权单位--JEDW*/
	@Excel(name = "债权单位", width = 15)
	@ApiModelProperty(value = "债权单位")
	private java.lang.String zqdw;
	/**取得价格**/
	@Excel(name = "取得价格", width = 15)
    @ApiModelProperty(value = "取得价格")
	private java.lang.Double qdjg;
	/**土地使用权人**/
	@Excel(name = "土地使用权人", width = 15)
    @ApiModelProperty(value = "土地使用权人")
	private java.lang.String tdshyqr;
}
