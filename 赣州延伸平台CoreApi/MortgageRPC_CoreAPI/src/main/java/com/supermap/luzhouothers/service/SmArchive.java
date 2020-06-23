package com.supermap.luzhouothers.service;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Component("smArchive")
public class SmArchive {
	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 档案查询
	 * 
	 * @param ps_name
	 *            String 权利人名称
	 * @param ps_idcardno
	 *            String 证件号码
	 * @param hs_location
	 *            String 房屋坐落
	 * @param re_housecertno
	 *            String 房屋证书（证明）号
	 * @param hs_buildarea
	 *            String 建筑面积
	 * @param pa_location
	 *            String 土地坐落
	 * @param re_landcertno
	 *            String 土地证书（证明）号
	 * @param pa_area
	 *            String 宗地面积
	 *            
	 * */
	public Message ArchiveList(int pageIndex, int pageSize, String ps_name,String ps_idcardno,
			String hs_location,String re_housecertno,String hs_buildarea, String pa_location, String re_landcertno, String pa_area) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		StringBuilder fromSql = new StringBuilder();
		StringBuilder nowhereString = new StringBuilder();

		if (ps_name != null && !ps_name.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("PS_NAME LIKE '%");
			nowhereString.append(ps_name.replace(" ", "%"));
			nowhereString.append("%'");
		}

		if (ps_idcardno != null && !ps_idcardno.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("PS_IDCARDNO LIKE '%");
			nowhereString.append(ps_idcardno.replace(" ", "%"));
			nowhereString.append("%'");
		}
		
		if (hs_location != null && !hs_location.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("HS_LOCATION LIKE '%");
			nowhereString.append(hs_location.replace(" ", "%"));
			nowhereString.append("%'");
		}
		
		if (re_housecertno != null && !re_housecertno.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("RE_HOUSECERTIFICATECODE LIKE '%");
			nowhereString.append(re_housecertno.replace(" ", "%"));
			nowhereString.append("%'");
		}
		
		if (hs_buildarea != null && !hs_buildarea.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("HS_BUILDAREA = ");
			nowhereString.append(hs_buildarea);
		}
		
		if (pa_location != null && !pa_location.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("PA_LOCATION LIKE '%");
			nowhereString.append(pa_location.replace(" ", "%"));
			nowhereString.append("%'");
		}
		
		if (re_landcertno != null && !re_landcertno.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("RE_LANDCERTIFICATECODE LIKE '%");
			nowhereString.append(re_landcertno.replace(" ", "%"));
			nowhereString.append("%'");
		}
		
		if (pa_area != null && !pa_area.equals("")) {
			if(nowhereString.length()>0)
			{
				nowhereString.append(" AND ");
			}
			nowhereString.append("PA_AREA = ");
			nowhereString.append(hs_buildarea);
		}

		fromSql.append(" FROM BDCK.BDCS_ARCHIVE_LZ T1");
		
		if (nowhereString.length() > 0) {
			fromSql.append(" where ");
			fromSql.append(nowhereString);
			
		} else {
			fromSql.append(" where 1=2");		
		}
		
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql.toString());
		listresult = dao.getPageDataByFullSql("SELECT T1.*,AC_ID AS ACTIVEID,DECODE(RE_REGSTATUS,0,'将登',1,'已登') AS DJZT "+fromSql.toString(), pageIndex,
				pageSize);
		

		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}

}
