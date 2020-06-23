/**   
* TODO:@liushufeng:请描述这个文件
* @Title: RTCONSTRAINT.java 
* @Package com.supermap.realestate.registration.constraint 
* @author liushufeng 
* @date 2016年3月28日 下午1:52:33 
* @version V1.0   
*/

package com.supermap.realestate.registration.constraint;

import java.util.Date;

/** TODO:@liushufeng:请描述这个类或接口"RTCONSTRAINT"
 * @ClassName: RTCONSTRAINT 
 * @author liushufeng 
 * @date 2016年3月28日 下午1:52:33  
 */
public interface RTCONSTRAINT {
	
	public abstract String getId();

	public abstract String getCONSTRAINTTYPE();

	public abstract String getCONSTRAINTID();

	public abstract String getRESULTTIP();

	public abstract String getCHECKLEVEL();

	public abstract Date getCREATETIME();

	public abstract Date getMODIFYTIME();

	public abstract String getCREATOR();

	public abstract String getLASTMODIFIER();

	public abstract String getTIPSQL();
}
