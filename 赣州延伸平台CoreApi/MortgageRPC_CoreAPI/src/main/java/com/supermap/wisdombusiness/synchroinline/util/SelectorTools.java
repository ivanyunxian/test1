package com.supermap.wisdombusiness.synchroinline.util;

import com.supermap.internetbusiness.util.ManualException;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

import javax.persistence.Column;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectorTools {

    /**
     * 表名和所有字段名的映射Map
     */
    private static Map<String, String> entityfieldnames = new HashMap<String, String>();

    public static Message queryDataForRunOnce(String selector, Map<String, String> param) throws Exception {
        // 返回的结果对象
        Message msg = new Message();

        CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
        String qzhcxms = ConfigHelper.getNameByValue("QZHCXMS");
        if(StringHelper.isEmpty(qzhcxms)){
            qzhcxms="/";
        }
        // 返回的结果对象
        // 选择器配置
        SelectorConfig config = HandlerFactory.getSelectorByName(selector);
        // 查询结果列表
        List<Map> listresult = null;
        // 不动产单元类型
        ConstValue.BDCDYLX bdcdylx = ConstValue.BDCDYLX.initFrom(config.getBdcdylx());
        // 来源
        ConstValue.DJDYLY ly = ConstValue.DJDYLY.initFrom(config.getLy());
        // 单元实体名
        String unitentityName = bdcdylx.getTableName(ly);
        // 查询结果记录总数
        long count = 0;
        // 选择单元
        if (config.isSelectbdcdy()) {
            String fullentityname = "com.supermap.realestate.registration.model." + unitentityName;
            Class<?> T = Class.forName(fullentityname);
            javax.persistence.Table tableanno = T.getAnnotation(javax.persistence.Table.class);
            String tablename = tableanno.schema() + "." + tableanno.name();

            String dyfieldsname = getTableFieldsName2(unitentityName);

            String sql = "from " + tablename + " dy where 1>0 ";
            if (config.isUseconfigsql()) {
                sql = " from (" + config.getConfigsql();
            }
            for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
                String name = config.getQueryconfig().getFields().get(i).getFieldname();
                String value = param.get(name);
                if (!StringHelper.isEmpty(value)) {
                    String _cond = "";
                    if(("BDCQZH").equals(name.toUpperCase())){}else{
                        _cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = to_single_byte(" + value + ")' ";
                    }if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !ConstValue.SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
                        _cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " like '%" + value + "%' ";
                    } else {
                        _cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ";
                    }
                    sql = sql + _cond;
                }
            }
            List<Map> list = new ArrayList<Map>();
            Map<String,String> map = new HashMap<String, String>();

            if (!StringHelper.isEmpty(config.getCondition())) {
                sql += " " + config.getCondition();
            }
            if (config.isUseconfigsql()) {
                sql += ")";
            }
            count = dao.getCountByFullSql(sql);
            String orderby = " ";
            if (config.getSortfields() != null && config.getSortfields().size() > 0) {
                orderby = " ORDER BY ";
                for (SelectorConfig.SortField sortfield : config.getSortfields()) {
                    if (!StringHelper.isEmpty(sortfield.getEntityname())) {
                        orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
                    } else {
                        orderby = orderby + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
                    }
                }
                if (orderby.endsWith(",")) {
                    orderby = orderby.substring(0, orderby.length() - 1);
                }
            }
            String fullsql = "select " + dyfieldsname + " " + sql + orderby;
            if (config.isUseconfigsql()) {
                String ssql = "select * " + sql + orderby;
                listresult = dao.getDataListByFullSql(ssql);
            } else {
                listresult = dao.getDataListByFullSql(fullsql);
            }
        } else if (config.isSelectql()) { // 选择权利
            msg.setWhether(config.isSelectql());
            String fullSql = "";
            String fromSql = "";
            if (!config.isUseconfigsql()) {
                /* 实体对应的表名(前面加用户名.) */
                unitentityName = "BDCK." + bdcdylx.getTableName(ConstValue.DJDYLY.XZ);
                /* 表名+'_'+字段名 */
                String dyfieldsname = getTableFieldsName(bdcdylx.getTableName(ConstValue.DJDYLY.XZ), "DY");
                String qlfieldsname = getTableFieldsName("BDCS_QL_XZ", "QL");
                String fsqlfieldsname = getTableFieldsName("BDCS_FSQL_XZ", "FSQL");
                String zddyfieldsname = " ZDDY.ZL AS ZDDY_ZDZL,ZDDY.BDCDYH AS ZDDY_ZDBDCDYH ";
                String zrzdyfieldsname = " ZRZDY.ZL AS ZRZDY_ZRZZL,ZRZDY.BDCDYH AS ZRZDY_ZRZBDCDYH,ZRZDY.ZRZH AS ZRZDY_ZRZH ";

                // 先构造查询字段
                // 再构造出from后边，where前边的表
                // 再构造where条件

                /* SELECT 字段部分 */
                StringBuilder builder2 = new StringBuilder();
                builder2.append("select ").append(dyfieldsname).append(",").append(qlfieldsname).append(",").append(fsqlfieldsname);
                String selectstr = builder2.toString();

                /* FROM 后边的表语句 */
                /* 不跟权利人表做连接了 */
                StringBuilder builder = new StringBuilder();
                builder.append(" from {0} DY").append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
                        .append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid").append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID ");

                StringBuilder builder4 = new StringBuilder();
                if(config.getName().equals("XZDJ_FWSYQSelector")||config.getName().equals("XZDJ_JTTDFWSYQSelector")||config.getName().equals("XZDJ_ZJDFWSYQSelector")){
                    builder.append(" left join bdck.bdcs_shyqzd_xz zddy on dy.zdbdcdyid=zddy.bdcdyid left join bdck.bdcs_zrz_xz zrzdy on dy.zrzbdcdyid=zrzdy.bdcdyid ");
                    builder4.append(",").append(zddyfieldsname).append(",").append(zrzdyfieldsname);
                    selectstr += builder4.toString();
                }
                /* WHERE 条件语句 */
                StringBuilder builder3 = new StringBuilder();
                builder3.append(" where 1>0");
                if (!StringHelper.isEmpty(config.getCondition())) {
                    builder3.append(config.getCondition());
                }

                for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
                    String name = config.getQueryconfig().getFields().get(i).getFieldname();
                    String value = param.get(name);
                    if (!StringHelper.isEmpty(value)) {
                        String entname = config.getQueryconfig().getFields().get(i).getEntityname();
                        if (!StringHelper.isEmpty(entname)) {
                            // 如果包括权利人相关的查询，再连接权利人表
                            if (entname.toUpperCase().equals("QLR")) {
                                if(value.indexOf(qzhcxms) == -1){
                                    if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !ConstValue.SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
                                        builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  INSTR(" + name + ",'" + value + "')>0) ");
                                    } else {
                                        builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " = '" + value + "') ");
                                    }
                                }else if(("BDCQZH").equals(name.toUpperCase())){
                                    String[] list = value.split(qzhcxms);
                                    String newvalue="";
                                    for (String string : list) {
                                        newvalue+="'" + string + "',";
                                    }
                                    newvalue = newvalue.substring(0, newvalue.length()-1);
                                    builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ");
                                }
                            }else if(entname.toUpperCase().equals("ZDDY")||entname.toUpperCase().equals("ZRZDY")){
                                if(("ZDZL").equals(name.toUpperCase())){
                                    builder3.append(" and  exists(select 1 from bdck.bdcs_shyqzd_xz where zl like'%"+value+"%' and bdcdyid=dy.zdbdcdyid) ");
                                }else if(("ZDBDCDYH").equals(name.toUpperCase())){
                                    builder3.append(" and  exists(select 1 from bdck.bdcs_shyqzd_xz where bdcdyh like '%"+value+"%' and bdcdyid=dy.zdbdcdyid) ");
                                }else if(("ZRZH").equals(name.toUpperCase())){
                                    builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where zrzh like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
                                }else if(("ZRZZL").equals(name.toUpperCase())){
                                    builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where zl like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
                                }else if(("ZRZBDCDYH").equals(name.toUpperCase())){
                                    builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where bdcdyh like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
                                }else{
                                    builder3.append(" ");
                                }
                            }else {
                                if(value.indexOf(qzhcxms) == -1){
                                    if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !ConstValue.SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
                                        builder3.append(" and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ");
                                    } else {
                                        builder3.append(" and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ");
                                    }
                                }else if(("BDCQZH").equals(name.toUpperCase())){
                                    String[] list = value.split(qzhcxms);
                                    String newvalue="";
                                    for (String string : list) {
                                        newvalue+="'" + string + "',";
                                    }
                                    newvalue = newvalue.substring(0, newvalue.length()-1);
                                    builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ");
                                }
                            }
                        }
                    }
                }

                String fromstr = builder.toString();
                fromstr = MessageFormat.format(fromstr, unitentityName);

                builder3.append(" and ql.qllx='" + config.getSelectqllx() + "'");
                String wherestr = builder3.toString();

                fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
                fullSql = selectstr + fromstr + wherestr;
            } else {
                String configsql = config.getConfigsql();
                for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
                    String name = config.getQueryconfig().getFields().get(i).getFieldname();
                    String value = param.get(name);
                    if (!StringHelper.isEmpty(value)) {
                        String entname = config.getQueryconfig().getFields().get(i).getEntityname();
                        if (!StringHelper.isEmpty(entname)) {
                            // 如果包括权利人相关的查询，再连接权利人表
                            if (entname.toUpperCase().equals("QLR")) {
                                if(value.indexOf(qzhcxms) == -1){
                                    if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !ConstValue.SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
                                        configsql += " and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  INSTR(" + name + ",'" + value + "')>0) ";
                                    } else {
                                        configsql += " and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " = '" + value + "') ";
                                    }
                                }else if(("BDCQZH").equals(name.toUpperCase())){
                                    String[] list = value.split(qzhcxms);
                                    String newvalue="";
                                    for (String string : list) {
                                        newvalue+="'" + string + "',";
                                    }
                                    newvalue = newvalue.substring(0, newvalue.length()-1);
                                    configsql +=" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ";
                                }
                            } else {
                                if(value.indexOf(qzhcxms) == -1){
                                    if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !ConstValue.SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
                                        configsql += " and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ";
                                    } else {
                                        configsql += " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ";
                                    }
                                }else if(("BDCQZH").equals(name.toUpperCase())){
                                    String[] list = value.split(qzhcxms);
                                    String newvalue="";
                                    for (String string : list) {
                                        newvalue+="'" + string + "',";
                                    }
                                    newvalue = newvalue.substring(0, newvalue.length()-1);
                                    configsql +=" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ";
                                }
                            }
                        }
                    }
                }
                fromSql = "from (" + configsql + ")";
                fullSql = configsql;
            }
            count = dao.getCountByFullSql(fromSql);
            if (count > 0) {
                String orderby = " ";
                if (config.getSortfields() != null && config.getSortfields().size() > 0) {
                    orderby = " ORDER BY ";
                    for (SelectorConfig.SortField sortfield : config.getSortfields()) {
                        orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
                    }
                    if (orderby.endsWith(",")) {
                        orderby = orderby.substring(0, orderby.length() - 1);
                    }
                }
                fullSql = fullSql + orderby;
                listresult = dao.getDataListByFullSql(fullSql);
                listresult = revmoeprefix(listresult);
            } else {
                throw new ManualException("从选择器中无法查询到此权利，请联系管理员");
            }
        }
        if(count <= 0) {
            throw new ManualException("从单元选择器中无法查询到此单元，请联系管理员");
        }

        msg.setTotal(count);
        msg.setRows(listresult);
        return msg;
    }

    /**
     * 内部方法：获取表的所有字段字符串
     * @Title: getTableFieldsName
     * @param tableName
     * @param prefix
     * @return
     * @throws ClassNotFoundException
     */
    private static String getTableFieldsName(String tableName, String prefix) throws ClassNotFoundException {
        String str = "";
        if (!entityfieldnames.containsKey(tableName.toUpperCase())) {
            StringBuilder builder = new StringBuilder();
            Map<String,String> map = new HashMap<String,String>();
            Class<?> t = Class.forName("com.supermap.realestate.registration.model." + tableName.toUpperCase());
            Method[] mds = t.getDeclaredMethods();
            for (Method md : mds) {
                Column c = md.getAnnotation(Column.class);
                if (c != null&&!"hjson".equalsIgnoreCase(c.name())) {
                    //如果包含相同字段，跳出本次循环
                    if (map.containsKey(c.name())) {
                        continue;
                    }
                    if (md.getReturnType() != null && md.getReturnType().getName() != null) {
                        if (md.getReturnType().getName().toUpperCase().contains("[LJAVA.LANG.BYTE")) {
                            continue;
                        }
                    }
                    if (c.name().toUpperCase().equals("ZSEWM") || c.name().toUpperCase().equals("FCFHT") || c.name().toUpperCase().equals("FCFHTWJ")) {
                        continue;
                    } else if (c.name().toUpperCase().equals("CFSJ")) {
                        builder.append("to_char(" + prefix + "." + c.name() + ",'yyyy-MM-dd') as " + prefix + "_" + c.name() + ",");
                    } else {
                        if("QL".equals(prefix)&&c.name().toUpperCase().equals("BDCDYID")){
                            continue;
                        }
                        builder.append(prefix + "." + c.name() + " as " + prefix + "_" + c.name() + ",");
                    }
                    map.put(c.name(), c.name());
                }
            }
            str = builder.toString();
            str = str.substring(0, str.length() - 1);
            entityfieldnames.put(tableName.toUpperCase(), str);
        }
        str = entityfieldnames.get(tableName.toUpperCase());
        return str;
    }

    /**
     * 内部方法：获取表的所有字段字符串，不加前缀的
     * @Title: getTableFieldsName2
     * @param tableName
     * @return
     * @throws ClassNotFoundException
     */
    private static String getTableFieldsName2(String tableName) throws ClassNotFoundException {
        String str = "";
        StringBuilder builder = new StringBuilder();
        Class<?> t = Class.forName("com.supermap.realestate.registration.model." + tableName.toUpperCase());
        Method[] mds = t.getDeclaredMethods();
        for (Method md : mds) {
            Column c = md.getAnnotation(Column.class);
            if (c != null) {
                if (c.name().toUpperCase().equals("FCFHTWJ") || c.name().toUpperCase().equals("FCFHT") || c.name().toUpperCase().equals("FCFHTWJ")||c.name().toUpperCase().equals("GLZRZID")) {
                    continue;
                } else {
                    builder.append(c.name() + ",");
                }
            }
        }
        str = builder.toString();
        str = str.substring(0, str.length() - 1);

        return str;
    }

    /**
     * 内部方法：移除查询结果中的表名前缀
     * @Title: revmoeprefix
     * @param listresult
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<Map> revmoeprefix(List<Map> listresult) {
        List<Map> list = new ArrayList<Map>();
        for (Map map : listresult) {
            Map mp = new HashMap<String, Object>();
            for (Object str : map.keySet()) {
                if (((String) str).toUpperCase().equals("DY_QLLX") || ((String) str).toUpperCase().equals("FSQL_BDCDYH") || ((String) str).toUpperCase().equals("FSQL_ZL"))
                    continue;
                String strnew = replaceXHX((String) str);
                try {
                    // 权利类型，要用权利表里的
                    if (!mp.containsKey(strnew)) {
                        mp.put(strnew, map.get(str));
                    }
                } catch (Exception e) {
                    System.out.println(str);
                }
            }
            list.add(mp);
        }
        return list;
    }

    /**
     * 内部方法：分离表别名和字段名从类似 DY_DJDYID中提取出DJDYID
     * @Title: replaceXHX
     * @param str
     * @return
     */
    private static String replaceXHX(String str) {
        String strnew = "";
        try {
            // TODO 暂时这样处理 字段：所在层， 不从附属权利表中取 ，从单元（户）表里取 2015-7-21
            if (str.toUpperCase().indexOf("FSQL_SZC") > -1) {
                return "";
            }
            int index = str.indexOf("_");
            if (index > 0 && index < str.length() - 1) {
                strnew = str.substring(index + 1, str.length());
            }
        } catch (Exception e) {
            System.out.println("出错了:" + e.getMessage());
        }
        return strnew;
    }
}
