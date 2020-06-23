package org.jeecg.modules.mortgagerpc.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 李海东
 * @created by 2019/9/24 10:45
 */
@Data
public class Bdcs_sfdyVO {
    /**
     * 主键ID
     */
    private String key;
    /**
     * 权利人名称
     */
    private String qlrmc;
    /**
     * 收费大类名称
     */
    private String sfdlmc;
    /**
     * 收费小类名称
     */
    private String sfxlmc;
    /**
     * 收费科目名称
     */
    private String sfkmmc;
    /**
     * 收费类型 %SFLX%
     */
    private String sflx;
    /**
     * 收费基数
     */
    private java.math.BigDecimal sfjs;
    /**
     * 面积基数
     */
    private java.math.BigDecimal mjjs;
    /**
     * 面积增量
     */
    private java.math.BigDecimal mjzl;
    /**
     * 收费增量
     */
    private java.math.BigDecimal sfzl;
    /**
     * 增量费用上限
     */
    private java.math.BigDecimal zlfysx;
    /**
     * 收费比例
     */
    private java.math.BigDecimal sfbl;
    /**
     * 计算公式
     */
    private String jsgs;
    /**
     * 创建时间
     */
    private Date creatTime;
    /**
     * 收费单位
     */
    private String sfdw;
    /**
     * bz
     */
    private String bz;
    /**
     * 计算方式
     */
    private String caltype;
    /**
     * 计算SQL语句
     */
    private String sqlexp;
    /**
     * 计算个数的SQL语句
     */
    private String cacsql;
    /**
     * 收费标志码
     */
    private String symbol;
    /**
     * 收费部门
     */
    private String sfbmmc;
    /**
     * 收费统计标志
     */
    private String tjbz;
    /**
     * 是否选中
     */
    private boolean checked = false;
    /**
     * 关系 
     */
}
