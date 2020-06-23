//package com.supermap.realestate.registration.check;
//
//import org.apache.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.supermap.realestate.registration.model.BDCS_XMXX;
//import com.supermap.realestate.registration.service.JYDJYTHDataSwapService;
//import com.supermap.realestate.registration.util.ConfigHelper;
//import com.supermap.realestate.registration.util.ConstValue.SF;
//import com.supermap.realestate.registration.util.Global;
//import com.supermap.realestate.registration.util.StringHelper;
//
///**
// * 通过切点完成交易登记一体化数据交换拦截判断校验 【这里用到的地方再启用，不要删除，交易一体化需要用于数据同步用】
// * @ClassName: BoardCheck
// * @author liushufeng
// * @date 2015年11月7日 下午6:56:30
// */
//@Component
//@Aspect
//public class JYDJYTHDataSwapPointCut {
//	public Logger logger = Logger.getLogger(JYDJYTHDataSwapPointCut.class);
//	@Autowired
//	private JYDJYTHDataSwapService jydjythDataSwapService;//交易登记一体化数据交互类
//	 
//	/**
//	 * 登簿后推送房屋状态给交易系统
//	 * @param pjp
//	 * @return
//	 * @throws Throwable
//	 */
//	@After(" execution(* com.supermap.realestate.registration.service.DBService.BoardBook*(..)) ")
//	public void pushHouseStateToJyyt(JoinPoint joinPoint) throws Throwable {
//		String XZQHDM =StringHelper.formatObject(ConfigHelper.getNameByValue("XZQHDM")); 
//		try {
//			if (XZQHDM.startsWith("4509")) {//玉林房地一体
//				
//				Object[] params = joinPoint.getArgs();
//				// 获取项目编号
//				String xmbh = StringHelper.formatObject(params[0]); 
//				BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
//				if(xmxx!=null)
//				{
//					if(SF.YES.Value.equals(xmxx.getSFDB()))
//					{ 
//						jydjythDataSwapService.writeBackHouseStateToFCJY(xmxx.getId()); 
//					}
//				} 
//			}
//		} catch (Exception e) {
//			logger.error("调用交易系统接口失败，无法将登记系统的房屋状态更新给交易网签子系统中，请联系管理员排查！！！！"+e.getMessage());
//			e.printStackTrace(); 
//		}
//	} 
//}
