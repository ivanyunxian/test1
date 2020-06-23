package org.jeecg.modules.mortgagerpc.mongo.model;

///*****************************************
//* AutoGenerate by CodeTools 2019/6/17 
//* ----------------------------------------
//* Public Entity T_CertCopy 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("SMMaterial")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="实体文件表", description="实体文件表")
public class MaterialFile{

	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "Material_ID")
	private String id;
		private String File_ID;
		private String wjly;
		private String tenant_ID;
		private String LastMateria_ID;
		private String Ywlsh;
		private String bdcdyh;
		private String bdcdyid;
		private String Object_Code1;
		private String Object_Code2;
		private String Object_Code3;
		private String Object_Code4;
		private String Object_Code5;
		private String MaterialFile_Type;
		private String MaterialFile_Name;
		private String MaterialFile_State;
		private String MaterialFile_Path;
		private String MaterialFile_Ext;
		private String MaterialFile_Size;
		private String MaterialFile_Number;
		private String MaterialFile_Identifier;
		private String MaterialFile_MD5;
		private String MaterialFile_Version;
		private String MaterialFile_Desc;
		private String create_Name;
		private Date create_Date;
		private Date update_Date;
		private String MaterialFile_IP;
		private String MaterialFile_Port;
}

