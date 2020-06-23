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
 * @Description: 申请人表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("bdc_sqr")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="bdc_sqr对象", description="申请人表")
public class Bdc_sqr {
    
	/**旧证号*/
	@Excel(name = "旧证号", width = 15)
    @ApiModelProperty(value = "旧证号")
	private java.lang.String jzh;
	/**共有份额*/
	@Excel(name = "共有份额", width = 15)
    @ApiModelProperty(value = "共有份额")
	private java.lang.String gyfe;
	/**法定代表人证件类型*/
	@Excel(name = "法定代表人证件类型", width = 15)
    @ApiModelProperty(value = "法定代表人证件类型")
	private java.lang.String fddbrzjlx;
	/**代理人姓名*/
	@Excel(name = "代理人姓名", width = 15)
    @ApiModelProperty(value = "代理人姓名")
	private java.lang.String dlrxm;
	/**代理机构名称*/
	@Excel(name = "代理机构名称", width = 15)
    @ApiModelProperty(value = "代理机构名称")
	private java.lang.String dljgmc;
	/**代理人联系电话*/
	@Excel(name = "代理人联系电话", width = 15)
    @ApiModelProperty(value = "代理人联系电话")
	private java.lang.String dlrlxdh;
	/**代理人证件类型*/
	@Excel(name = "代理人证件类型", width = 15)
    @ApiModelProperty(value = "代理人证件类型")
	@Dict(dicCode = "ZJLX")
	private java.lang.String dlrzjlx;
	/**代理人证件号码*/
	@Excel(name = "代理人证件号码", width = 15)
    @ApiModelProperty(value = "代理人证件号码")
	private java.lang.String dlrzjhm;
	/**权利面积*/
	@Excel(name = "权利面积", width = 15)
    @ApiModelProperty(value = "权利面积")
	private java.lang.String qlmj;
	/**权利比例*/
	@Excel(name = "权利比例", width = 15)
    @ApiModelProperty(value = "权利比例")
	private java.lang.String qlbl;
	/**共有方式*/
	@Excel(name = "共有方式", width = 15)
    @ApiModelProperty(value = "共有方式")
	@Dict(dicCode = "GYFS")
	private java.lang.String gyfs;
	/**法定代表人证件号码*/
	@Excel(name = "法定代表人证件号码", width = 15)
    @ApiModelProperty(value = "法定代表人证件号码")
	private java.lang.String fddbrzjhm;
	/**申请人类别*/
	@Excel(name = "申请人类别", width = 15)
    @ApiModelProperty(value = "申请人类别")
	private java.lang.String sqrlb;
	/**民族*/
	@Excel(name = "民族", width = 15)
    @ApiModelProperty(value = "民族")
	private java.lang.String nation;
	/**申请人共有关系（夫妻、父子、母子、兄弟、姐妹、其它）*/
	@Excel(name = "申请人共有关系（夫妻、父子、母子、兄弟、姐妹、其它）", width = 15)
    @ApiModelProperty(value = "申请人共有关系（夫妻、父子、母子、兄弟、姐妹、其它）")
	private java.lang.String sqrgygx;
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
	/**申请人id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "申请人id")
	private java.lang.String id;
	/**权利ID*/
	@Excel(name = "权利ID", width = 15)
    @ApiModelProperty(value = "权利ID")
	private java.lang.String qlid;
	/**流水号 外网流水号*/
	@Excel(name = "流水号 外网流水号", width = 15)
    @ApiModelProperty(value = "流水号 外网流水号")
	private java.lang.String prolsh;
	/**区划代码*/
	@Excel(name = "区划代码", width = 15)
    @ApiModelProperty(value = "区划代码")
	private java.lang.String divisionCode;
	/**申请人姓名*/
	@Excel(name = "申请人姓名", width = 15)
    @ApiModelProperty(value = "申请人姓名")
	private java.lang.String sqrxm;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	private java.lang.String xb;
	/**证件类型*/
	@Excel(name = "证件类型", width = 15)
    @ApiModelProperty(value = "证件类型")
	private java.lang.String zjlx;
	/**证件号*/
	@Excel(name = "证件号", width = 15)
    @ApiModelProperty(value = "证件号")
	private java.lang.String zjh;
	/**国家地区*/
	@Excel(name = "国家地区", width = 15)
    @ApiModelProperty(value = "国家地区")
	private java.lang.String gjdq;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
	private java.lang.String lxdh;
	/**通讯地址*/
	@Excel(name = "通讯地址", width = 15)
    @ApiModelProperty(value = "通讯地址")
	private java.lang.String txdz;
	/**法定代表人*/
	@Excel(name = "法定代表人", width = 15)
    @ApiModelProperty(value = "法定代表人")
	private java.lang.String fddbr;
	/**申请人类型*/
	@Excel(name = "申请人类型", width = 15)
    @ApiModelProperty(value = "申请人类型")
	private java.lang.String sqrlx;
	/**申请人类型*/
	@Excel(name = "不动产权证号", width = 15)
	@ApiModelProperty(value = "不动产权证号")
	private java.lang.String bdcqzh;
}
