package org.jeecg.modules.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("sys_user_zrz")
public class SysUserZrz implements Serializable{

private static final long serialVersionUID = 1L;
	
	/**主键id*/
    @TableId(type = IdType.UUID)
	private String id;
	/**用户id*/
	private String userId;
	/**自然幢id*/
	private String zrzId;
	public SysUserZrz(String id, String userId, String zrzId) {
		super();
		this.id = id;
		this.userId = userId;
		this.zrzId = zrzId;
	}

	public SysUserZrz(String id, String zrzId) {
		this.userId = id;
		this.zrzId = zrzId;
	}
}
