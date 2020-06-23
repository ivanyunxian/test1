/**   
 * 检查执行引擎
 * @Title: CheckEngine.java 
 * @Package com.supermap.realestate.registration.check 
 * @author liushufeng 
 * @date 2015年11月7日 下午6:47:50 
 * @version V1.0   
 */

package com.supermap.realestate.registration.check;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 检查执行引擎
 * @ClassName: CheckEngine
 * @author liushufeng
 * @date 2015年11月7日 下午6:47:50
 */
public class CheckEngine {

	private static final Log logger = LogFactory.getLog(CheckEngine.class);

	/**
	 * 执行检查规则,判断检查规则的类型：1 SQL走executeSqlCheck 2 CLASS走executeClassCheck
	 * @Title: ExecuteCheck
	 * @author:liushufeng
	 * @date：2015年11月7日 下午6:51:32
	 * @param rule
	 * @return
	 */
	public static ResultMessage executeCheck(CommonDao dao, CheckRule rule, HashMap<String, String> paramMap) {
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("false");
		try {
			if ("SQL".equals(rule.getEXECUTETYPE())) {
				boolean bcheck=executeSqlCheck(dao, rule, paramMap);
				if(bcheck){
					msg.setSuccess("true");
				}
			} else if ("CLASS".equals(rule.getEXECUTETYPE())) {
				msg=executeClassCheck(rule, paramMap);
			}
		} catch (ClassNotFoundException e) {
			logger.warn("执行检查规则时出错，找不到检查规则“" + rule.getNAME() + "“对应的执行类");
		} catch (InstantiationException e) {
			logger.warn("执行检查规则时出错，无法实例化检查规则“" + rule.getNAME() + "“对应的执行类");
		} catch (IllegalAccessException e) {
			logger.warn("执行检查规则时出错，检查规则“" + rule.getNAME() + "“对应的执行类没有实现CheckItem接口");
		} catch (ParseException e) {
			logger.warn("执行检查规则时出错，无法计算检查规则“" + rule.getNAME() + "“对应的结果表达式");
		}
		return msg;
	}

	/**
	 * 执行SQL检查
	 * @Title: executeSqlCheck
	 * @author:liushufeng
	 * @date：2015年11月7日 下午9:46:26
	 * @param rule
	 * @return
	 * @throws ParseException
	 */
	private static boolean executeSqlCheck(CommonDao dao, CheckRule rule, Map<String, String> paramMap) throws ParseException {
		String sql = rule.getEXECUTESQL();
		if (StringHelper.isEmpty(sql))
			return true;
		String resultexpr = rule.getSQLRESULTEXP();
		if (StringHelper.isEmpty(resultexpr))
			return false;
		long count = dao.getCountByFullSql(sql, paramMap);
		boolean result = calculateExpr(count, resultexpr);
		return result;
	}

	/**
	 * 对比SQL执行结果和规则结果表达式，得到检查结果true或false
	 * @Title: calculateExpr
	 * @author:liushufeng
	 * @date：2015年11月7日 下午9:49:44
	 * @param result
	 * @param strexpr
	 * @return
	 * @throws ParseException
	 */
	private static boolean calculateExpr(long result, String strexpr) throws ParseException {
		boolean b = true;
		Scope scope = Scope.create();
		Expression expr = (Expression) Parser.parse("RESULT" + strexpr, scope);
		Variable SFJS = scope.getVariable("RESULT");
		SFJS.setValue(result);
		double dresult = expr.evaluate();
		if (dresult > 0)
			b = true;
		else
			b = false;
		return b;
	}

	/**
	 * 执行基于CheckItem接口的类检查
	 * @Title: executeClassCheck
	 * @author:liushufeng
	 * @date：2015年11月7日 下午9:46:37
	 * @param rule
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private static ResultMessage executeClassCheck(CheckRule rule, HashMap<String, String> paramMap) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("true");
		String className = rule.getEXECUTECLASSNAME();
		if (StringHelper.isEmpty(className)){
			msg.setSuccess("false");
			return msg;
		}
		Class<?> t = Class.forName(className);
		CheckItem item = (CheckItem) t.newInstance();
		if (item != null)
			msg = item.check(paramMap);
		return msg;
	}
}
