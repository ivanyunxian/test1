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
 * @Description: 证书信息表
 * @Author: jeecg-boot
 * @Date:   2019-08-29
 * @Version: V1.0
 */
@Data
@TableName("Bdc_zs")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Bdc_zs对象", description="证书信息表")
public class Bdc_zs {
    
	/**证书ID*/
	@TableId
	@Excel(name = "证书ID", width = 15)
    @ApiModelProperty(value = "证书ID")
	private String zsid;
	/**权利ID*/
	@Excel(name = "权利ID", width = 15)
    @ApiModelProperty(value = "权利ID")
	private String qlid;
	/**申请人id*/
	@Excel(name = "申请人id", width = 15)
    @ApiModelProperty(value = "申请人id")
	private String sqrid;
	/**流水号 外网流水号*/
	@Excel(name = "流水号 外网流水号", width = 15)
    @ApiModelProperty(value = "流水号 外网流水号")
	private String prolsh;
	/**区划代码*/
	@Excel(name = "区划代码", width = 15)
    @ApiModelProperty(value = "区划代码")
	private String divisionCode;
	/**证书编号*/
	@Excel(name = "证书编号", width = 15)
    @ApiModelProperty(value = "证书编号")
	private String zsbh;
	/**不动产权证号*/
	@Excel(name = "不动产权证号", width = 15)
    @ApiModelProperty(value = "不动产权证号")
	private String bdcqzh;
	/**缮证时间，成功生成证书的时间*/
	@Excel(name = "缮证时间，成功生成证书的时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "缮证时间，成功生成证书的时间")
	private Date szsj;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
	private String versionno;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
	private Date modifytime;
	/**不动产单元号*/
	@Excel(name = "不动产单元号", width = 15)
    @ApiModelProperty(value = "不动产单元号")
	private String bdcdyh;
	/**产权证号*/
	@Excel(name = "产权证号", width = 15)
    @ApiModelProperty(value = "产权证号")
	private String cqzh;
	/**附记*/
	@Excel(name = "附记", width = 15)
    @ApiModelProperty(value = "附记")
	private String fj;
	/**证书封面_日*/
	@Excel(name = "证书封面_日", width = 15)
    @ApiModelProperty(value = "证书封面_日")
	private String fmDay;
	/**证书封面_月*/
	@Excel(name = "证书封面_月", width = 15)
    @ApiModelProperty(value = "证书封面_月")
	private String fmMonth;
	/**证书封面_年*/
	@Excel(name = "证书封面_年", width = 15)
    @ApiModelProperty(value = "证书封面_年")
	private String fmYear;
	/**共有情况*/
	@Excel(name = "共有情况", width = 15)
    @ApiModelProperty(value = "共有情况")
	private String gyqk;
	/**面积*/
	@Excel(name = "面积", width = 15)
    @ApiModelProperty(value = "面积")
	private String mj;
	/**年度*/
	@Excel(name = "年度", width = 15)
    @ApiModelProperty(value = "年度")
	private String nd;
	/**区划简称*/
	@Excel(name = "区划简称", width = 15)
    @ApiModelProperty(value = "区划简称")
	private String qhjc;
	/**区划名称*/
	@Excel(name = "区划名称", width = 15)
    @ApiModelProperty(value = "区划名称")
	private String qhmc;
	/**权利类型*/
	@Excel(name = "权利类型", width = 15)
    @ApiModelProperty(value = "权利类型")
	private String qllx;
	/**权利其他状况*/
	@Excel(name = "权利其他状况", width = 15)
    @ApiModelProperty(value = "权利其他状况")
	private String qlqtzk;
	/**权利人*/
	@Excel(name = "权利人", width = 15)
    @ApiModelProperty(value = "权利人")
	private String qlr;
	/**权利性质*/
	@Excel(name = "权利性质", width = 15)
    @ApiModelProperty(value = "权利性质")
	private String qlxz;
	/**使用期限*/
	@Excel(name = "使用期限", width = 15)
    @ApiModelProperty(value = "使用期限")
	private String syqx;
	/**原不动产权证号*/
	@Excel(name = "原不动产权证号", width = 15)
    @ApiModelProperty(value = "原不动产权证号")
	private String ybdcqzh;
	/**用途*/
	@Excel(name = "用途", width = 15)
    @ApiModelProperty(value = "用途")
	private String yt;
	/**坐落*/
	@Excel(name = "坐落", width = 15)
    @ApiModelProperty(value = "坐落")
	private String zl;
	/**证书证明类型（ZS,证书；ZM，证明）*/
	@Excel(name = "证书证明类型（ZS,证书；ZM，证明）", width = 15)
    @ApiModelProperty(value = "证书证明类型（ZS,证书；ZM，证明）")
	private String type;
	/**zmqlhsx*/
	@Excel(name = "zmqlhsx", width = 15)
    @ApiModelProperty(value = "zmqlhsx")
	private String zmqlhsx;
	/**ywr*/
	@Excel(name = "ywr", width = 15)
    @ApiModelProperty(value = "ywr")
	private String ywr;
	/**qt*/
	@Excel(name = "qt", width = 15)
    @ApiModelProperty(value = "qt")
	private String qt;
	/**dbjg*/
	@Excel(name = "dbjg", width = 15)
    @ApiModelProperty(value = "dbjg")
	private String dbjg;
	/**year*/
	@Excel(name = "year", width = 15)
    @ApiModelProperty(value = "year")
	private String year;
	/**month*/
	@Excel(name = "month", width = 15)
    @ApiModelProperty(value = "month")
	private String month;
	/**day*/
	@Excel(name = "day", width = 15)
    @ApiModelProperty(value = "day")
	private String day;
	/**sjc*/
	@Excel(name = "sjc", width = 15)
    @ApiModelProperty(value = "sjc")
	private String sjc;
	/**sxh*/
	@Excel(name = "sxh", width = 15)
    @ApiModelProperty(value = "sxh")
	private String sxh;
	
	@Excel(name = "filename", width = 15)
    @ApiModelProperty(value = "filename")
	private String filename;
	
	@Excel(name = "mongoid", width = 15)
    @ApiModelProperty(value = "mongoid")
	private String mongoid;
	
	@Excel(name = "pdf", width = 15)
    @ApiModelProperty(value = "pdf")
	private String pdf;
}
