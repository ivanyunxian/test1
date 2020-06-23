package com.supermap.wisdombusiness.synchroinline.model;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 证书信息
 * @author Administrator
 *
 */
public class CertModel
{
	private String ID;
	private String ESTATENO;
	private String SEQNO;
	private Integer MONTH;
	private Integer DAY;
	private Integer YEAR;
	private String CERTIFICATENO;
	private String PROSHOR;
	private String ADMINAREA;
	private String OBLIGEE;
	private String OTHER;
	private String OBLIGOR;
	private String LOCATION;
	private String RIGHTTYPE;
	private Integer ISSYNCHRO;
	private String COMSITUATION;
	private String RIGHTNATURE;
	private String USEFOR;
	private String AREA;
	private String TIMELIMITE;
	private String RIGHTOTHER;
	private String RIGHTCLASS;
	private String FJ;
	private String ZJH;
	/**
	 * @return the iD
	 */
	public String getID()
	{
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD)
	{
		ID = iD;
	}
	/**
	 * @return the eSTATENO
	 */
	public String getESTATENO()
	{
		return ESTATENO;
	}
	/**
	 * @param eSTATENO the eSTATENO to set
	 */
	public void setESTATENO(String eSTATENO)
	{
		ESTATENO = eSTATENO;
	}
	/**
	 * @return the sEQNO
	 */
	public String getSEQNO()
	{
		return SEQNO;
	}
	/**
	 * @param sEQNO the sEQNO to set
	 */
	public void setSEQNO(String sEQNO)
	{
		SEQNO = sEQNO;
	}
	/**
	 * @return the mONTH
	 */
	public Integer getMONTH()
	{
		return MONTH;
	}
	/**
	 * @param mONTH the mONTH to set
	 */
	public void setMONTH(Integer mONTH)
	{
		MONTH = mONTH;
	}
	/**
	 * @return the dAY
	 */
	public Integer getDAY()
	{
		return DAY;
	}
	/**
	 * @param dAY the dAY to set
	 */
	public void setDAY(Integer dAY)
	{
		DAY = dAY;
	}
	/**
	 * @return the yEAR
	 */
	public Integer getYEAR()
	{
		return YEAR;
	}
	/**
	 * @param yEAR the yEAR to set
	 */
	public void setYEAR(Integer yEAR)
	{
		YEAR = yEAR;
	}
	/**
	 * @return the cERTIFICATENO
	 */
	public String getCERTIFICATENO()
	{
		return CERTIFICATENO;
	}
	/**
	 * @param cERTIFICATENO the cERTIFICATENO to set
	 */
	public void setCERTIFICATENO(String cERTIFICATENO)
	{
		CERTIFICATENO = cERTIFICATENO;
	}
	/**
	 * @return the pROSHOR
	 */
	public String getPROSHOR()
	{
		return PROSHOR;
	}
	/**
	 * @param pROSHOR the pROSHOR to set
	 */
	public void setPROSHOR(String pROSHOR)
	{
		PROSHOR = pROSHOR;
	}
	/**
	 * @return the aDMINAREA
	 */
	public String getADMINAREA()
	{
		return ADMINAREA;
	}
	/**
	 * @param aDMINAREA the aDMINAREA to set
	 */
	public void setADMINAREA(String aDMINAREA)
	{
		ADMINAREA = aDMINAREA;
	}
	/**
	 * @return the oBLIGEE
	 */
	public String getOBLIGEE()
	{
		return OBLIGEE;
	}
	/**
	 * @param oBLIGEE the oBLIGEE to set
	 */
	public void setOBLIGEE(String oBLIGEE)
	{
		OBLIGEE = oBLIGEE;
	}
	/**
	 * @return the oTHER
	 */
	public String getOTHER()
	{
		return OTHER;
	}
	/**
	 * @param oTHER the oTHER to set
	 */
	public void setOTHER(String oTHER)
	{
		OTHER = oTHER;
	}
	/**
	 * @return the oBLIGOR
	 */
	public String getOBLIGOR()
	{
		return OBLIGOR;
	}
	/**
	 * @param oBLIGOR the oBLIGOR to set
	 */
	public void setOBLIGOR(String oBLIGOR)
	{
		OBLIGOR = oBLIGOR;
	}
	/**
	 * @return the lOCATION
	 */
	public String getLOCATION()
	{
		return LOCATION;
	}
	/**
	 * @param lOCATION the lOCATION to set
	 */
	public void setLOCATION(String lOCATION)
	{
		LOCATION = lOCATION;
	}
	/**
	 * @return the rIGHTTYPE
	 */
	public String getRIGHTTYPE()
	{
		return RIGHTTYPE;
	}
	/**
	 * @param rIGHTTYPE the rIGHTTYPE to set
	 */
	public void setRIGHTTYPE(String rIGHTTYPE)
	{
		RIGHTTYPE = rIGHTTYPE;
	}
	/**
	 * @return the iSSYNCHRO
	 */
	public Integer getISSYNCHRO()
	{
		return ISSYNCHRO;
	}
	/**
	 * @param iSSYNCHRO the iSSYNCHRO to set
	 */
	public void setISSYNCHRO(Integer iSSYNCHRO)
	{
		ISSYNCHRO = iSSYNCHRO;
	}
	/**
	 * @return the cOMSITUATION
	 */
	public String getCOMSITUATION()
	{
		return COMSITUATION;
	}
	/**
	 * @param cOMSITUATION the cOMSITUATION to set
	 */
	public void setCOMSITUATION(String cOMSITUATION)
	{
		COMSITUATION = cOMSITUATION;
	}
	/**
	 * @return the rIGHTNATURE
	 */
	public String getRIGHTNATURE()
	{
		return RIGHTNATURE;
	}
	/**
	 * @param rIGHTNATURE the rIGHTNATURE to set
	 */
	public void setRIGHTNATURE(String rIGHTNATURE)
	{
		RIGHTNATURE = rIGHTNATURE;
	}
	/**
	 * @return the uSEFOR
	 */
	public String getUSEFOR()
	{
		return USEFOR;
	}
	/**
	 * @param uSEFOR the uSEFOR to set
	 */
	public void setUSEFOR(String uSEFOR)
	{
		USEFOR = uSEFOR;
	}
	/**
	 * @return the aREA
	 */
	public String getAREA()
	{
		return AREA;
	}
	/**
	 * @param aREA the aREA to set
	 */
	public void setAREA(String aREA)
	{
		AREA = aREA;
	}
	/**
	 * @return the tIMELIMITE
	 */
	public String getTIMELIMITE()
	{
		return TIMELIMITE;
	}
	/**
	 * @param tIMELIMITE the tIMELIMITE to set
	 */
	public void setTIMELIMITE(String tIMELIMITE)
	{
		TIMELIMITE = tIMELIMITE;
	}
	/**
	 * @return the rIGHTOTHER
	 */
	public String getRIGHTOTHER()
	{
		return RIGHTOTHER;
	}
	/**
	 * @param rIGHTOTHER the rIGHTOTHER to set
	 */
	public void setRIGHTOTHER(String rIGHTOTHER)
	{
		RIGHTOTHER = rIGHTOTHER;
	}
	/**
	 * @return the rIGHTCLASS
	 */
	public String getRIGHTCLASS()
	{
		return RIGHTCLASS;
	}
	/**
	 * @param rIGHTCLASS the rIGHTCLASS to set
	 */
	public void setRIGHTCLASS(String rIGHTCLASS)
	{
		RIGHTCLASS = rIGHTCLASS;
	}
	/**
	 * @return the fJ
	 */
	public String getFJ()
	{
		return FJ;
	}
	/**
	 * @param fJ the fJ to set
	 */
	public void setFJ(String fJ)
	{
		FJ = fJ;
	}
	/**
	 * @return the zJH
	 */
	public String getZJH()
	{
		return ZJH;
	}
	/**
	 * @param zJH the zJH to set
	 */
	public void setZJH(String zJH)
	{
		ZJH = zJH;
	}
	
	public String create_insert_sql()
	{
		Class cls = this.getClass();
		Field[] fields = cls.getDeclaredFields();
		String[] columns = new String[fields.length];
		String[] placeholders = new String[fields.length];
		int index = 0;
		for (Field f : fields)
		{
			columns[index] = f.getName();
			placeholders[index] = "?";
			index++;
		}
		String sql = "insert into SMWB_INLINE.T_CERTIFICATE_INLINE (" + StringUtils.join(columns, ",") + ") values (" + StringUtils.join(placeholders, ",") + ")";
		return sql;
	}
	
	public void fillPreparedStatement(PreparedStatement ps) throws IllegalArgumentException, SQLException, IllegalAccessException
	{
		Class cls = this.getClass();
		Field[] fields = cls.getDeclaredFields();
		int index = 1;
		for(Field f : fields)
		{
			if(f.getType()==String.class)
			{
				ps.setString(index,getString(f.get(this)));
			}
			else if(f.getType()==Integer.class)
			{
				ps.setObject(index,this.getInt(f.get(this)),java.sql.Types.INTEGER);
			}
			else if(f.getType()==Date.class)
			{
				//
			}
			index++;
		}
	}
	
	private String getString(Object obj)
	{
		if(obj==null)
			return "";
		return obj.toString();
	}
	
	private Integer getInt(Object obj)
	{
		if(obj==null)
			return null;
		return Integer.valueOf(obj.toString());
	}
	
}
