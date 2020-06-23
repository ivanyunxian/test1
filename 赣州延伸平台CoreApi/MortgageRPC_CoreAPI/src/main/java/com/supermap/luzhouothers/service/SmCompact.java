package com.supermap.luzhouothers.service;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Component("smCompact")
public class SmCompact {
	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 合同查询
	 * 
	 * @param name
	 *            String 购房人姓名
	 * @param idno
	 *            String 购房人证件号码
	 * @param htbh
	 *            String 合同编号
	 * @param zl
	 *            String 坐落
	 * @param querytype
	 *            String 查询方式【true 模糊查询，false 精确查询】
	 * */
	public Message CompactList(int pageIndex, int pageSize, String name,
			String idno, String htbh, String zl, String querytype) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		StringBuilder fromSql = new StringBuilder();
		StringBuilder yfromSql = new StringBuilder();
		StringBuilder xfromSql = new StringBuilder();
		StringBuilder nowhereString = new StringBuilder();
		StringBuilder hnowhereString = new StringBuilder();
		StringBuilder pnowhereString = new StringBuilder();

		if (htbh != null && !htbh.equals("")) {
			nowhereString.append("CONTRACTNO LIKE '%");
			nowhereString.append(htbh.replace(" ", "%"));
			nowhereString.append("%'");
		}

		if (zl != null && !zl.equals("")) {
			hnowhereString.append("ZL LIKE '%");
			hnowhereString.append(zl.replace(" ", "%"));
			hnowhereString.append("%'");
		}

		if (name != null && !name.equals("")) {
			pnowhereString.append("OWNER_NAME LIKE '%");
			pnowhereString.append(name.replace(" ", "%"));
			pnowhereString.append("%'");
		}

		if (idno != null && !idno.equals("")) {
			if (pnowhereString.length() > 0) {
				pnowhereString.append(" AND ");
			}
			pnowhereString.append("IDNO like '%");
			pnowhereString.append(name.replace(" ", "%"));
			pnowhereString.append("%'");
		}

		if (hnowhereString.length() > 0) {
			if (nowhereString.length() > 0) {
				nowhereString.append(" AND ");
			}
			nowhereString
					.append(" ID IN (SELECT ID FROM BDCK.BDCS_COMPACT_H WHERE ");
			nowhereString.append(hnowhereString.toString());
			nowhereString.append(")");
		}

		if (pnowhereString.length() > 0) {
			if (nowhereString.length() > 0) {
				nowhereString.append(" and ");
			}
			nowhereString
					.append(" ID IN (SELECT ID FROM BDCK.BDCS_COMPACT_OWNERINFO WHERE ");
			nowhereString.append(pnowhereString.toString());
			nowhereString.append(")");
		}
		
		yfromSql.append(" FROM BDCK.BDCS_YCOMPACT ");
		xfromSql.append(" FROM BDCK.BDCS_XCOMPACT ");

		if (nowhereString.length() > 0) {
			yfromSql.append(" where ");
			yfromSql.append(nowhereString);
			xfromSql.append(" where ");
			xfromSql.append(nowhereString);
		} else {
			yfromSql.append(" where 1=2");
			xfromSql.append(" where 1=2");
		}
		
		fromSql.append(" FROM (SELECT CONTRACTNO,REALTYPEID,ID,PRICE,AMOUNT,to_char(CREATEDATETIME,'yyyy-MM-dd hh24:mi:ss') CREATEDATETIME,to_char(APPLYTIME,'yyyy-MM-dd hh24:mi:ss') APPLYTIME,to_char(COMMITDATETIME,'yyyy-MM-dd hh24:mi:ss') COMMITDATETIME,DECODE(BASTATUS,-1,'已审核',0,'审核中') BASTATUS,'期房签约' AS ACTIVE_TYPE,to_char(CONTRACTDATETIME,'yyyy-MM-dd') CONTRACTDATETIME ");
		fromSql.append(yfromSql).append(" UNION ALL ");
		fromSql.append("SELECT CONTRACTNO,REALTYPEID,ID,PRICE,AMOUNT,to_char(CREATEDATETIME,'yyyy-MM-dd hh24:mi:ss') CREATEDATETIME,to_char(APPLYTIME,'yyyy-MM-dd hh24:mi:ss') APPLYTIME,to_char(COMMITDATETIME,'yyyy-MM-dd hh24:mi:ss') COMMITDATETIME,DECODE(BASTATUS,-1,'已审核',0,'审核中') BASTATUS,DECODE(REALTYPEID,164801,'现房签约',164802,'二手房网上签约') AS ACTIVE_TYPE,to_char(CONTRACTDATETIME,'yyyy-MM-dd') CONTRACTDATETIME ");
		fromSql.append(xfromSql).append(")");

		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql.toString());
		listresult = dao.getPageDataByFullSql("SELECT * "+fromSql.toString(), pageIndex,
				pageSize);
		
		
		if (listresult != null && listresult.size() > 0) {
			for (@SuppressWarnings("rawtypes")
			Map map : listresult) {
				if (map.containsKey("ID")) {
					String id = map.get("ID").toString();
					String realtypeid = map.get("REALTYPEID").toString();
					if (id != null) {
						/**房屋相关面积	**/
						StringBuilder hsb=new StringBuilder();
						hsb.append("SELECT ZL,BUILDAREA JZMJ,USEAREA TNMJ,PUBLICAREA FTMJ,PART FH FROM BDCK.BDCS_COMPACT_H WHERE ID=");
						hsb.append(id);
						hsb.append(" AND ISVALID=-1");
						List<Map> hList=dao.getDataListByFullSql(hsb.toString());
						StringBuilder zlsb=new StringBuilder();
						StringBuilder areasb=new StringBuilder();
						StringBuilder fhsb=new StringBuilder();
						for(int i=0;i<hList.size();i++)
						{
							Map hmap=hList.get(i);
							if(i==0)
							{
								zlsb.append(hmap.get("ZL").toString());
								areasb.append(hmap.get("JZMJ").toString());
								fhsb.append(hmap.get("FH").toString());
							}
							else
							{
								zlsb.append(",").append(hmap.get("ZL").toString());
								areasb.append(",").append(hmap.get("JZMJ").toString());
								fhsb.append(",").append(hmap.get("FH").toString());
							}
							
						}
						
						map.put("ZL", zlsb.toString());
						map.put("JZMJ", areasb.toString());	
						map.put("FH", fhsb.toString());	
						
						/**购房人相关**/
						StringBuilder psb=new StringBuilder();
						psb.append("SELECT OWNER_NAME,IDNO FROM BDCK.BDCS_COMPACT_OWNERINFO  WHERE OWNER_TYPE IN (10,11,20,21) AND ID = ");
						psb.append(id);
						List<Map> pList=dao.getDataListByFullSql(psb.toString());
						StringBuilder pnamesb=new StringBuilder();
						StringBuilder pcodesb=new StringBuilder();
						for(int i=0;i<pList.size();i++)
						{
							Map pmap=pList.get(i);
							if(i==0)
							{
								pnamesb.append(pmap.get("OWNER_NAME").toString());
								pcodesb.append(pmap.get("IDNO").toString());
							}
							else
							{
								pnamesb.append(",").append(pmap.get("OWNER_NAME").toString());
								pcodesb.append(",").append(pmap.get("IDNO").toString());
							}
						}
						
						
						
						map.put("GFRMC", pnamesb.toString());	
						map.put("ZJH", pcodesb.toString());	
					}
				}
			}
		}

		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
	public Message CompactInfo(String id,String realtypeid) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		StringBuilder fullSql = new StringBuilder();
		
		fullSql.append("SELECT CONTRACTNO,REALTYPEID,ID,PRICE,AMOUNT,to_char(CREATEDATETIME,'yyyy-MM-dd hh24:mi:ss') CREATEDATETIME,to_char(APPLYTIME,'yyyy-MM-dd hh24:mi:ss') APPLYTIME,to_char(COMMITDATETIME,'yyyy-MM-dd hh24:mi:ss') COMMITDATETIME,DECODE(BASTATUS,-1,'已审核',0,'审核中') BASTATUS,DECODE(REALTYPEID,163801,'期房签约',164801,'现房签约',164802,'二手房网上签约') AS ACTIVE_TYPE,to_char(CONTRACTDATETIME,'yyyy-MM-dd') CONTRACTDATETIME,CALPRICETYPE,PAYTYPE,FIRSTPAYRATIO,LOANBANK,TO_CHAR(DELIVERDATE,'yyyy-MM-dd') DELIVERDATE FROM ");
		/**期房**/
		if(realtypeid.equals("163801"))
		{
			fullSql.append(" BDCK.BDCS_YCOMPACT ");
		}
		else
		{
			fullSql.append(" BDCK.BDCS_XCOMPACT ");
		}
		
		fullSql.append(" WHERE ID=").append(id);

		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		
		listresult = dao.getDataListByFullSql(fullSql.toString());
			
		if (listresult != null && listresult.size() > 0) {
			for (@SuppressWarnings("rawtypes")
			Map map : listresult) {
				
					if (id != null) {
						/**房屋相关面积	**/
						StringBuilder hsb=new StringBuilder();
						hsb.append("SELECT ZL,BUILDAREA JZMJ,USEAREA TNMJ,PUBLICAREA FTMJ,PART FH,FLOORON,FLOORONEND,STRUCTURE,DESIGNUSAGE,TYPE  FROM BDCK.BDCS_COMPACT_H WHERE ID=");
						hsb.append(id);
						hsb.append(" AND ISVALID=-1");
						List<Map> hList=dao.getDataListByFullSql(hsb.toString());
						StringBuilder zlsb=new StringBuilder();
						StringBuilder jzmjsb=new StringBuilder();
						StringBuilder tnmjsb=new StringBuilder();
						StringBuilder ftmjsb=new StringBuilder();
						StringBuilder fhsb=new StringBuilder();
						StringBuilder onsb=new StringBuilder();
						StringBuilder endsb=new StringBuilder();
						StringBuilder strusb=new StringBuilder();
						StringBuilder usesb=new StringBuilder();
						StringBuilder typesb=new StringBuilder();
						
						for(int i=0;i<hList.size();i++)
						{
							Map hmap=hList.get(i);
							if(i==0)
							{
								zlsb.append(hmap.get("ZL").toString());
								jzmjsb.append(hmap.get("JZMJ").toString());
								tnmjsb.append(hmap.get("TNMJ").toString());
								ftmjsb.append(hmap.get("FTMJ").toString());
								fhsb.append(hmap.get("FH").toString());
								onsb.append(hmap.get("FLOORON").toString());
								endsb.append(hmap.get("FLOORONEND").toString());
								strusb.append(hmap.get("STRUCTURE").toString());
								usesb.append(hmap.get("DESIGNUSAGE").toString());
								//typesb.append(hmap.get("TYPE").toString());
							}
							else
							{
								zlsb.append(",").append(hmap.get("ZL").toString());
								jzmjsb.append(",").append(hmap.get("JZMJ").toString());
								tnmjsb.append(",").append(hmap.get("TNMJ").toString());
								ftmjsb.append(",").append(hmap.get("FTMJ").toString());
								fhsb.append(",").append(hmap.get("FH").toString());							
								onsb.append(",").append(hmap.get("FLOORON").toString());
								endsb.append(",").append(hmap.get("FLOORONEND").toString());
								strusb.append(",").append(hmap.get("STRUCTURE").toString());
								usesb.append(",").append(hmap.get("DESIGNUSAGE").toString());
								//typesb.append(",").append(hmap.get("TYPE").toString());
							}
							
						}
						
						map.put("ZL", zlsb.toString());
						map.put("JZMJ", jzmjsb.toString());	
						map.put("TNMJ", tnmjsb.toString());	
						map.put("FTMJ", ftmjsb.toString());	
						map.put("FH", fhsb.toString());	
						
						map.put("FLOORON", onsb.toString());
						map.put("FLOORONEND", endsb.toString());	
						map.put("STRUCTURE", strusb.toString());	
						map.put("DESIGNUSAGE", usesb.toString());	
						map.put("TYPE", typesb.toString());	
						
						/**购房人相关**/
						StringBuilder psb=new StringBuilder();
						psb.append("SELECT OWNER_NAME,IDNO,IDTYPE FROM BDCK.BDCS_COMPACT_OWNERINFO  WHERE OWNER_TYPE IN (10,11,20,21) AND ID = ");
						psb.append(id);
						List<Map> pList=dao.getDataListByFullSql(psb.toString());
						StringBuilder pnamesb=new StringBuilder();
						StringBuilder idtypesb=new StringBuilder();
						StringBuilder pcodesb=new StringBuilder();
						for(int i=0;i<pList.size();i++)
						{
							Map pmap=pList.get(i);
							if(i==0)
							{
								pnamesb.append(pmap.get("OWNER_NAME").toString());
								idtypesb.append(pmap.get("IDTYPE").toString());
								pcodesb.append(pmap.get("IDNO").toString());
							}
							else
							{
								pnamesb.append(",").append(pmap.get("OWNER_NAME").toString());
								idtypesb.append(",").append(pmap.get("IDTYPE").toString());
								pcodesb.append(",").append(pmap.get("IDNO").toString());
							}
						}
						
						/**出卖人相关**/
						StringBuilder ssb=new StringBuilder();
						ssb.append("SELECT OWNER_NAME,IDNO,IDTYPE FROM BDCK.BDCS_COMPACT_OWNERINFO  WHERE OWNER_TYPE IN (30,31,40,41) AND ID = ");
						ssb.append(id);
						List<Map> sList=dao.getDataListByFullSql(ssb.toString());
						StringBuilder snamesb=new StringBuilder();
						StringBuilder sidtypesb=new StringBuilder();
						StringBuilder scodesb=new StringBuilder();
						for(int i=0;i<sList.size();i++)
						{
							Map smap=sList.get(i);
							if(i==0)
							{
								snamesb.append(smap.get("OWNER_NAME").toString());
								sidtypesb.append(smap.get("IDTYPE").toString());
								scodesb.append(smap.get("IDNO").toString());
							}
							else
							{
								snamesb.append(",").append(smap.get("OWNER_NAME").toString());
								sidtypesb.append(",").append(smap.get("IDTYPE").toString());
								scodesb.append(",").append(smap.get("IDNO").toString());
							}
						}
						
						map.put("GFRMC", pnamesb.toString());
						map.put("ZJLX", idtypesb.toString());
						map.put("ZJH", pcodesb.toString());
						map.put("CMRMC", snamesb.toString());
						map.put("CMRZJLX", sidtypesb.toString());
						map.put("CMRZJH", scodesb.toString());
				}
			}
		}

		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}

}
