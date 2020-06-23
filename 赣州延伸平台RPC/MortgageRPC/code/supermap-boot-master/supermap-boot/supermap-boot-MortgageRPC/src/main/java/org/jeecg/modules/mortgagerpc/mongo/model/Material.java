package org.jeecg.modules.mortgagerpc.mongo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

@Data
@TableName("SMMaterial")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="文件目录表", description="文件目录表")
public class Material {

	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "Material_ID")
	private String Material_ID;
	@Excel(name = "divisionCode", width = 15)
	private String lastMateria_ID;
	private String tenant_ID;
	private String ywlsh;
	private String material_Name;
	private String materialType;
	private String bdcdyh;
	private String bdcdyid;
	private String Object_Code1;
	private String Object_Code2;
	private String Object_Code3;
	private String Object_Code4;
	private String Object_Code5;
	private String divisionName;
	private String materiaVersion;
	private String divisionCode;
	private String infoDesc;
	private String createName;
	private Date createDate;
	private Integer materialCount;
	private Date update_Date;
}