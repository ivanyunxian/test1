package org.jeecg.modules.mortgagerpc.util;

import java.util.ArrayList;
import java.util.List;

import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zrz;
import org.jeecg.modules.mortgagerpc.model.BdcZrzTreeModel;


public class FindZdZrzChildrenUtil {

	
	 /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将Bdc_shyqzd类型的list集合转换成Bdc_zrzTreeModel类型的集合
	 * @param zrzList 
     */
    public static List<BdcZrzTreeModel> wrapTreeDataToTreeList(List<Bdc_shyqzd> zdlist, List<Bdc_zrz> zrzList) {
     // 在该方法每请求一次,都要对全局list集合进行一次清理
//        idList.clear();
        List<BdcZrzTreeModel> records = new ArrayList<>();
        for (int i = 0; i < zdlist.size(); i++) {
            Bdc_shyqzd zd = zdlist.get(i);
            new BdcZrzTreeModel(zd);
            BdcZrzTreeModel zdtemp  = new BdcZrzTreeModel(zd);
          //循环第二级zrz
            for(Bdc_zrz zrz :zrzList) {
            	if(zrz.getZdid().equals(zd.getId())) {
            		BdcZrzTreeModel zrztemp = new BdcZrzTreeModel(zrz);
            		zdtemp.getChildren().add(zrztemp);
            	}
            }
            records.add(zdtemp);
        }
        setEmptyChildrenAsNull(records);
        return records;
    }
    


	/**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<BdcZrzTreeModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
        	BdcZrzTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setIsLeaf(true);
            }else{
                setEmptyChildrenAsNull(model.getChildren());
                model.setIsLeaf(false);
            }
        }
    }
}
