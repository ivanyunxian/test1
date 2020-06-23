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
import org.bson.types.Code;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 单元信息表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("bdc_dy")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="bdc_dy对象", description="单元信息表")
public class Bdc_dy {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**bdcdyid*/
	@Excel(name = "bdcdyid", width = 15)
    @ApiModelProperty(value = "bdcdyid")
	private java.lang.String bdcdyid;
	/**ysdm*/
	@Excel(name = "ysdm", width = 15)
    @ApiModelProperty(value = "ysdm")
	private java.lang.String ysdm;
	/**bdcdyh*/
	@Excel(name = "bdcdyh", width = 15)
    @ApiModelProperty(value = "bdcdyh")
	private java.lang.String bdcdyh;
	/**xmbh*/
	@Excel(name = "xmbh", width = 15)
    @ApiModelProperty(value = "xmbh")
	private java.lang.String xmbh;
	/**fwbm*/
	@Excel(name = "fwbm", width = 15)
    @ApiModelProperty(value = "fwbm")
	private java.lang.String fwbm;
	/**zrzbdcdyid*/
	@Excel(name = "zrzbdcdyid", width = 15)
    @ApiModelProperty(value = "zrzbdcdyid")
	private java.lang.String zrzbdcdyid;
	/**zddm*/
	@Excel(name = "zddm", width = 15)
    @ApiModelProperty(value = "zddm")
	private java.lang.String zddm;
	/**zdbdcdyid*/
	@Excel(name = "zdbdcdyid", width = 15)
    @ApiModelProperty(value = "zdbdcdyid")
	private java.lang.String zdbdcdyid;
	/**区县代码*/
	@Excel(name = "区县代码", width = 15)
    @ApiModelProperty(value = "区县代码")
	private java.lang.String divisionCode;
	/**流水号 外网流水号*/
	@Excel(name = "流水号 外网流水号", width = 15)
    @ApiModelProperty(value = "流水号 外网流水号")
	private java.lang.String prolsh;
	/**zrzh*/
	@Excel(name = "zrzh", width = 15)
    @ApiModelProperty(value = "zrzh")
	private java.lang.String zrzh;
	/**zl*/
	@Excel(name = "zl", width = 15)
    @ApiModelProperty(value = "zl")
	private java.lang.String zl;
	/**mjdw*/
	@Excel(name = "mjdw", width = 15)
    @ApiModelProperty(value = "mjdw")
	@Dict(dicCode = "MJDW")
	private java.lang.String mjdw;
	/**zcs*/
	@Excel(name = "zcs", width = 15)
    @ApiModelProperty(value = "zcs")
	private java.lang.Integer zcs;
	/**hh*/
	@Excel(name = "hh", width = 15)
    @ApiModelProperty(value = "hh")
	private java.lang.Integer hh;
	/**shbw*/
	@Excel(name = "shbw", width = 15)
    @ApiModelProperty(value = "shbw")
	private java.lang.String shbw;
	/**hx*/
	@Excel(name = "hx", width = 15)
    @ApiModelProperty(value = "hx")
	private java.lang.String hx;
	/**hxjg*/
	@Excel(name = "hxjg", width = 15)
    @ApiModelProperty(value = "hxjg")
	private java.lang.String hxjg;
	/**fwyt*/
	@Excel(name = "fwyt", width = 15)
    @ApiModelProperty(value = "fwyt")
	@Dict(dicCode = "FWYT")
	private java.lang.String fwyt;
	/**mj*/
	@Excel(name = "mj", width = 15)
    @ApiModelProperty(value = "mj")
	private java.lang.Double mj;
	/**tnjzmj*/
	@Excel(name = "tnjzmj", width = 15)
    @ApiModelProperty(value = "tnjzmj")
	private java.lang.Double tnjzmj;
	/**ycjzmj 预测建筑面积*/
	@Excel(name = "ycjzmj", width = 15)
	@ApiModelProperty(value = "ycjzmj")
	private java.lang.Double ycjzmj;
	/**yctnjzmj 预测套内建筑面积*/
	@Excel(name = "yctnjzmj", width = 15)
	@ApiModelProperty(value = "yctnjzmj")
	private java.lang.Double yctnjzmj;
	/**ftjzmj*/
	@Excel(name = "ftjzmj", width = 15)
    @ApiModelProperty(value = "ftjzmj")
	private java.lang.Double ftjzmj;
	/**gytdmj*/
	@Excel(name = "gytdmj", width = 15)
    @ApiModelProperty(value = "gytdmj")
	private java.lang.Double gytdmj;
	/**fttdmj*/
	@Excel(name = "fttdmj", width = 15)
    @ApiModelProperty(value = "fttdmj")
	private java.lang.Double fttdmj;
	/**dytdmj*/
	@Excel(name = "dytdmj", width = 15)
    @ApiModelProperty(value = "dytdmj")
	private java.lang.Double dytdmj;
	/**tdsyqr*/
	@Excel(name = "tdsyqr", width = 15)
    @ApiModelProperty(value = "tdsyqr")
	private java.lang.String tdsyqr;
	/**fdcjyjg*/
	@Excel(name = "fdcjyjg", width = 15)
    @ApiModelProperty(value = "fdcjyjg")
	private java.lang.Double fdcjyjg;
	/**ghyt*/
	@Excel(name = "ghyt", width = 15)
    @ApiModelProperty(value = "ghyt")
	@Dict(dicCode = "FWYT")
	private java.lang.String ghyt;
	/**fwjg*/
	@Excel(name = "fwjg", width = 15)
    @ApiModelProperty(value = "fwjg")
	@Dict(dicCode = "FWJG")
	private java.lang.String fwjg;
	/**jgsj*/
	@Excel(name = "jgsj", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "jgsj")
	private java.util.Date jgsj;
	/**fwlx*/
	@Excel(name = "fwlx", width = 15)
    @ApiModelProperty(value = "fwlx")
	private java.lang.String fwlx;
	/**fwxz*/
	@Excel(name = "fwxz", width = 15)
    @ApiModelProperty(value = "fwxz")
	@Dict(dicCode = "FWXZ")
	private java.lang.String fwxz;
	/**zdmj*/
	@Excel(name = "zdmj", width = 15)
    @ApiModelProperty(value = "zdmj")
	private java.lang.Double zdmj;
	/**cqly*/
	@Excel(name = "cqly", width = 15)
    @ApiModelProperty(value = "cqly")
	private java.lang.String cqly;
	/**qtgsd*/
	@Excel(name = "qtgsd", width = 15)
    @ApiModelProperty(value = "qtgsd")
	private java.lang.String qtgsd;
	/**qtgsx*/
	@Excel(name = "qtgsx", width = 15)
    @ApiModelProperty(value = "qtgsx")
	private java.lang.String qtgsx;
	/**qtgsn*/
	@Excel(name = "qtgsn", width = 15)
    @ApiModelProperty(value = "qtgsn")
	private java.lang.String qtgsn;
	/**qtgsb*/
	@Excel(name = "qtgsb", width = 15)
    @ApiModelProperty(value = "qtgsb")
	private java.lang.String qtgsb;
	/**fcfht*/
	@Excel(name = "fcfht", width = 15)
    @ApiModelProperty(value = "fcfht")
	private java.lang.String fcfht;
	/**zt*/
	@Excel(name = "zt", width = 15)
    @ApiModelProperty(value = "zt")
	private java.lang.Integer zt;
	/**yxbz*/
	@Excel(name = "yxbz", width = 15)
    @ApiModelProperty(value = "yxbz")
	private java.lang.String yxbz;
	/**cqzt*/
	@Excel(name = "cqzt", width = 15)
    @ApiModelProperty(value = "cqzt")
	private java.lang.String cqzt;
	/**dyzt*/
	@Excel(name = "dyzt", width = 15)
    @ApiModelProperty(value = "dyzt")
	private java.lang.String dyzt;
	/**xzzt*/
	@Excel(name = "xzzt", width = 15)
    @ApiModelProperty(value = "xzzt")
	private java.lang.String xzzt;
	/**blzt*/
	@Excel(name = "blzt", width = 15)
    @ApiModelProperty(value = "blzt")
	private java.lang.String blzt;
	/**yyzt*/
	@Excel(name = "yyzt", width = 15)
    @ApiModelProperty(value = "yyzt")
	private java.lang.String yyzt;
	/**fh*/
	@Excel(name = "fh", width = 15)
    @ApiModelProperty(value = "fh")
	private java.lang.String fh;
	/**djzt*/
	@Excel(name = "djzt", width = 15)
    @ApiModelProperty(value = "djzt")
	private java.lang.String djzt;
	/**bgzt*/
	@Excel(name = "bgzt", width = 15)
    @ApiModelProperty(value = "bgzt")
	private java.lang.String bgzt;
	/**关联ID*/
	@Excel(name = "关联ID", width = 15)
    @ApiModelProperty(value = "关联ID")
	private java.lang.String relationid;
	/**2、权利性质*/
	@Excel(name = "2、权利性质", width = 15)
    @ApiModelProperty(value = "2、权利性质")
	@Dict(dicCode = "QLXZ")
	private java.lang.String qlxz;
	/**土地使用权起始日期*/
	@Excel(name = "土地使用权起始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "土地使用权起始日期")
	private java.util.Date tdsyqqsrq;
	/**土地使用权终止日期*/
	@Excel(name = "土地使用权终止日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "土地使用权终止日期")
	private java.util.Date tdsyqzzrq;
	/**土地使用年限*/
	@Excel(name = "土地使用年限", width = 15)
    @ApiModelProperty(value = "土地使用年限")
	private java.lang.Integer tdsynx;
	/**起始层*/
	@Excel(name = "起始层", width = 15)
    @ApiModelProperty(value = "起始层")
	private java.lang.Double qsc;
	/**终止层*/
	@Excel(name = "终止层", width = 15)
    @ApiModelProperty(value = "终止层")
	private java.lang.Double zzc;
	/**所在层*/
	@Excel(name = "所在层", width = 15)
    @ApiModelProperty(value = "所在层")
	private java.lang.String szc;
	/**单元号*/
	@Excel(name = "单元号", width = 15)
    @ApiModelProperty(value = "单元号")
	private java.lang.String dyh;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String bz;
	/**createtime*/
	@Excel(name = "createtime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createtime")
	private java.util.Date createtime;
	/**modifytime*/
	@Excel(name = "modifytime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "modifytime")
	private java.util.Date modifytime;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
	private java.lang.String versionno;
	/**单个被担保债权数额*/
	@Excel(name = "单个被担保债权数额", width = 15)
	@ApiModelProperty(value = "单个被担保债权数额")
	private java.lang.Double dgbdbzzqse;
	/**房源接口返回内容*/
	@Excel(name = "房源接口返回内容", width = 15)
	@ApiModelProperty(value = "房源接口返回内容")
	private String houseclob;
	/**房屋产别*/
	@Excel(name = "房屋产别", width = 15)
	@ApiModelProperty(value = "房屋产别")
	@Dict(dicCode = "FWCB")
	private String fwcb;
	/**房屋产别*/
	@Excel(name = "房屋土地用途", width = 15)
	@ApiModelProperty(value = "房屋土地用途")
	@Dict(dicCode = "FWTDYT")
	private String fwtdyt;
}
