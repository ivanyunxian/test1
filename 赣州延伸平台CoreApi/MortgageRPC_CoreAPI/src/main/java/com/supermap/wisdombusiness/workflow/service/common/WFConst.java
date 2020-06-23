package com.supermap.wisdombusiness.workflow.service.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.realestate.registration.util.ConfigHelper;


public class WFConst {

	/**
	 * 状态枚举 　<模块_状态值>;
	 *
	 * @author yizy
	 *
	 */
	// 操作类型
	public static enum Operate_Type {

		/** 正常 */
		NormalOperate(1),
		/** 申请驳回 */
		AskForOverRule(2),
		/** 不同意驳回 */
		NotAgreeToOverRule(4),
		/** 已经被驳回 */
		PRODUCT_smallPicture(9),
		/**撤回**/
		WithDraw(11),
		/** 驳回标志*/
		PRODUCT_anglesPicture(31),
		/** 申请委托 **/
		AskForPassWork(17),
		/** 被督办 */
		HaveSupervised(16),
		/** 绿色通道 */
		HavePriviledge(21),
		/**转办**/
		HaveTurn(30),
		/**申请挂起**/
		ApplyHungUp(40),
		/**锁定**/
		HaveLock(47);
		public int value;

		Operate_Type(int value) {
			this.value = value;
		}
	}

	/**
	 * 实例类型
	 * */
	public static enum Instance_Type {
		/** 正常实例 */
		Instance_Normal(1),
		/** 驳回实例 */
		Instance_OverRule(2),
		/**转出到驳回*/
		Instance_OtherOverRule(3),
		/** 挂起实例 */
		Instance_HangingUp(4),
		/** 锁定实例 */
		Instance_Locked(5),
		/** 超期实例 */
		Instance_OverDue(6),
		/** 绿色通道 */
		Instance_Priviledge(7),
		/** 督办实例 */
		Instance_Supervised(8),
		/** 延期实例 */
		Instance_ProLong(9),
		/** 自动转出实例 */
		Instance_AutoPass(10),
		/**随机实例 */
		Instance_Ramdom(11),
		/** 注销实例 */
		Instance_LogOut(20),
		/** 急件实例 */
		Instance_Require(40),
		/** 加急实例 */
		Instance_Urgent(40);
		
		
		public int value;

		Instance_Type(int value) {
			this.value = value;
		}
	}

	/**
	 * 实例状态
	 * */
	public static enum Instance_Status {
		/** 已办结 */
		Instance_havedone(0),
		
		/** 等待受理 */
		Instance_NotAccept(1),
		/** 正在办理 */
		Instance_doing(2),
		/** 已经转出 */
		Instance_Passing(3),
		/** 等待所有活动转出 */
		Instance_WaitForAccept(4),
		/** 等待所有活动驳回 */
		Instance_WaitForOverRule(5),
		/** 注销实例 */
		Instance_LogOut(6),
        /**转出成功,仅用于转出状态返回*/
		Instance_Success(7),
		/**待办，在办合并，仅用于项目列表*/
		Instance_doAndDoing(8),
		
		/** 质检 */
		QualityTest(10),
		/**撤回**/
		WithDraw(11),
		/**督办用于项目列表*/
		Instance_DB(13),
		/**环节已转出，档案未接受*/
		WaitAccept(14),
		/**环节实例已创建，档案未接受*/
		BeforAccept(15),
		/**取消的活动实例类型用于并发活动驳回*/
		cancelAcinst(16),
		/**吉林房产退件状态*/
		cancelByFangchan(17);
		public int value;

		Instance_Status(int value) {
			this.value = value;
		}
	}
	/**
	 * 活动状态
	 * @author yiziyu
	 *
	 */
	public static enum ActInst_Status{
		/**正常*/
		Normal(1),
		/**挂起*/
		HangUp(2),
		/**延期*/
		Delay(3);
	
		public int value;
		ActInst_Status(int value) {
			this.value = value;
		}
	}
	/**
	 * 活动类型
	 * */
	public static enum ActDef_Type {
		/** 手工开始 */
		ProcessStart(1010),
		/**自动开始*/
		ProcessStartAutomation(1020),
		/** 交互活动 */
		Activity(2010),
		/** 自动活动 */
		ActivityAutomation(2020),
		/** 聚合活动 */
		ActivityMerge(2030),
		/**并发活动*/
		ConditionAnd(3010),
		/**分支条件*/
		ConditionOr(3020),
		/**自动条件*/
		ConditionWhere(3030),
		/**嵌入流程*/
		SubProcess(4010),
		/**手工结束*/
		ProcessEnd(5010),
		/**自动结束*/
		processEndAutomation(5020),
		/** 协办活动 */
		AllDoAct(9),
		/** 不计时活动 **/
		PauseTimeAct(13), 
		/** 可跳过互动 **/
		CanPass(15),
		/** 可注销活动 **/
		CanLonout(17),
		/**可随机活动*/
		CanRadom(18),
		/** 审批活动 */
		AuditingAct(1);
		public int value;

		ActDef_Type(int value) {
			this.value = value;
		}
	}

	/**
	 * 节假日类型
	 * 
	 * */
	public static enum HolidayType {
		/**法定节假日 */
		LegalHoliday(1),
		/** 周末*/
		WeekEnd(3), 
		/** 其他假期*/
		OtherHoliday(4);

		public int value;

		HolidayType(int value) {
			this.value = value;
		}

	}

	/**
	 * 项目列表类型
	 * 
	 * */
	public static enum ProjectListType {

		Normal(0), NotAccept(1), AskForOverRule(2), Doing(3), AskForHungUp(5), AskForProlong(
				8), HaveOverRule(9), HaveHang(10), HaveProlong(11), HaveOverTime(
				13), Priviledge(14), WaitForApprove(15), AcceptNewProject(16), Supervised(
				16), AskForPass(17), HavePass(18), AskForLogOut(19), WaitForProblem(
				20), HaveProblem(21), HavePassOver(25), Pause(50), DeptProjectLists(
				60), NotNormal(70);
		public int value;

		ProjectListType(int value) {
			this.value = value;
		}

	}
	/**
	 * 判断是否可以驳回返回类型枚举
	 * 
	 * */
	public  enum SmActInstOverRuleReturnType{
		/**不能驳回起始活动*/
		CannotOverRuleStartAct(1),
		/**不能直接驳回，需要审批*/
		CannotOverRuleDirectly(2),
		/**并发活动不能跳跃驳回*/
		CannotOverRuleSkip(3),
		/** 活动设置不可直接驳回*/
		CannotOverRuleActDef(4),
		/**活动设置不可跳跃驳回*/
		CannotOverRuleSkipActDef(5),
		/** 已经成功驳回*/
		OverRuled(6),
		/**活动设置可跳跃驳回*/
		CanOverRuleSkipActDef(7);
		public int value;

		SmActInstOverRuleReturnType(int value) {
			this.value = value;
		}
	}
	/**
	 * 判断是否可以注销活动
	 * */
	public  enum SmActInstLogOutReturnType{
		/***可以注销/
		CanLogOutAct(1),
		/**不能注销*/
		CannotLogOutAct(2);
		public int value;

		SmActInstLogOutReturnType(int value) {
			this.value = value;
		}
	}
	/**
	 * 直接转出返回类型枚举
	 * */
	public static enum SmActInstPassOverReturnType
	{
		/***  此中类型流程不可转出*/
		TheProTypeCannotPassOver(1),
		/*** 当前活动不符合转出条件*/
		TheActHaveNotEnoughCondition(2),
		/***  需要选择新流程*/
        RequireSelectNewProDef(3),
        /***  本项目已经办结*/
		TheProHaveDone(4),
		/*** 需要选择路由或者员工（不能直接转出）*/
		RequireSelectRouteOrStaff(5),
		/*** 可办结活动*/
        CanDoneAct(6),
        /***  已经成功地直接转出*/
		PassOveredDirectly(7);
		public int value;

		SmActInstPassOverReturnType(int value) {
			this.value = value;
		}
	}
	
	/**收件资料状态*/
	public static enum MateralStatus {
		/**创建资料目录*/
		CeratMateral(1),
		/**收件成功*/
		AcceotMateral(2),
		/**上传附件*/
		UploadMatetal(3),
		/**待接收*/
		NoReceive(4),
		/*已接收*/
		Received(5),
		/*台账*/
		Book(6),
		/**虚拟收件**/
		VirtualAccept(7),
		/**补充收件资料**/
		Supply(8);//
		public int value;
		MateralStatus(int value){
			this.value = value;
		}
	}
	
	/*
	 * 转出条件类型
	 * **/
	public static enum PassConditionType{
		/*
		 * 按照条目验证
		 * **/
		ConditionItem("1"),
		ConditionGroup("2");
		public String value;
		PassConditionType(String value){
			this.value = value;
		}
	}
	
	/**
	 *活动办理方式 
	 * */
	public static enum ActDefRealtive{
		/*
		 * 按照条目验证
		 * **/
		None("0");
		public String value;
		ActDefRealtive(String value){
			this.value = value;
		}
	}
	/**
	 *转出方式
	 * */
	public static enum PassOverType{
		Staff(1),
		Role(2),
		Dept(3);
		public Integer value;
		PassOverType(Integer value){
			this.value = value;
		}
	}	

    /**
     * 文件上传的状态
     * */
	public  static enum Upload_Status{
		/**正常*/
		Normal(1),
		/**未分组*/
		NotGroup(2);
		public Integer value;
		Upload_Status(Integer value){
			this.value = value;
		}
	}
	/**
	 * 异常记录
	 * @author yiziyu
	 *
	 */
	public static enum Abnormal_Type{
		/*删除项目*/
		DeleteProject(1),
		/*注销项目*/
		LogOutProject(2),
		/*修改紧急状态*/
		ModifyUrgent(3),
		/**撤回项目**/
		WithDraw(11);
		public Integer value;
		Abnormal_Type(Integer value){
			this.value = value;
		}
	}
	
	/**派件模式*/
	public static enum Send_Type{
		/*无*/
		None(0),
		/*平均*/
		Average(1),
		/**办件箱量少*/
		LessFirst(2),
		/**办件量少*/
		LessWorkFirst(3),
		/**高效*/
		Efficient(4);		
		public Integer value;
		Send_Type(Integer value){
			this.value = value;
		}
	}
	/**提问状态*/
	public static enum Question_Status{
		/*无效*/
		None(0),
		/*等待解答*/
		Wait(1),
		/*解决*/
		solved(2);		
		public Integer value;
		Question_Status(Integer value){
			this.value = value;
		}
	}
	
	public static enum Proinst_Weight{
		/*正常*/
		Normal(0),
		/*紧急*/
		Urgent(10),
		/*即将超期*/
		SoonOutTime(20),
		/*驳回*/
		PassBack(50),
		/*超期*/
		OutTime(30),		
		/*特急*/
		ExtraUrgent(40),
		/*被督办*/
		Supervised(60);		
		public Integer value;
		Proinst_Weight(Integer value){
			String str=ConfigHelper.getNameByValue("proinstweight");
			str=str.replace(" ", "");
			String[] arr=str.split(";");
			int index=(int)value/10-1;
			if(arr.length-1<index||index<0){
				this.value=value;
			}else{
				String tt=arr[index];
				Integer weight=Integer.parseInt(arr[index].substring(arr[index].indexOf(":")+1));
				this.value = weight;
			}
		}
	}
	
	/**锁定类型**/
	public static enum LockedType{
		/*正常挂起*/
		HangUp(1),
		/*进行公告*/
		Announcement(2),
		/*资料异常*/
		AbnormalData(3),
		/*质检中*/
		QualityTest(4);
		public Integer value;
		LockedType(Integer value){
			this.value = value;
		}
	}
	
}