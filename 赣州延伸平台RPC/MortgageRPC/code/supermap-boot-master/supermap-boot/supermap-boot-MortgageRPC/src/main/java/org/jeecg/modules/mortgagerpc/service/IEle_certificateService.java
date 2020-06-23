/**
 * 
 */
package org.jeecg.modules.mortgagerpc.service;

import java.io.File;
import java.util.List;

import org.jeecg.modules.mortgagerpc.entity.Bdc_sqr;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;

/**
 * @author Administrator
 *
 */
public interface IEle_certificateService {

	List<File> getEcert(List<Bdc_sqr> sqrList, Wfi_materclass wfi_materclass);

}
