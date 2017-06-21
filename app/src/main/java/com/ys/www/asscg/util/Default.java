package com.ys.www.asscg.util;

import android.util.DisplayMetrics;

public class Default {
	/**
	 * style = 0:TabBar样式 = 1：侧滑样式
	 */
	public static final int style = 0;

	public static boolean showLog = false;// 正式上线 false

	/** 富友支付 */
	public static final boolean use_fy = true;

//	public static final String ip = "http://106.14.83.114";
	public static final String ip = "http://120.25.58.247";

	public static final String yu = "http://www.czsuchang.com/";
//签到
	public static String qiandao = "NO";
	//签到接口
	public static final String qd = "/Member/Mcenter/doSign";
	//是否签到接口
	public static final String isqd = "/Member/Mcenter/isSign";
	// 获取手机分辨率
	public static DisplayMetrics dm;

	// 判断设置手势密码状态 false :关闭 true：打开
	public static boolean passwordSwitchChanged = false;

	// 企业直投和定投宝显示Flag FALSE 默认显示钱 TRUE 默认显示份数
	public static final boolean showUnitFlag = false;

	public static String user;
	public static boolean hdf_show_error_info = true;
	public static long userId = 0;
	public static String username = "";
	public static String user_photo_path = "/Style/header/customavatars/000/00/00/";
	public static int phoneverif = 1; // 1.手动验证,2.验证码验证
	public static String curVersion = "1.0";
	public static int curVersioncode = 1;
	public static boolean click_home_key_flag = false;
	public static boolean isActive = true;
	public static boolean isgestures = false;
	// -----------------------------------------------------------
	public static final String login = "/member/Mobilecommon/actlogin";
	public static final String register = "/member/Mobilecommon/regaction";
	public static final String peoInfoSafe = "/Member/MCenter/accountinfo"; // 账户信息
	public static final String peoInfoUpdate = "/Member/Mcenter/userinfo";
	public static final String peoInfoBorrowing = "/Member/Mcenter/jiekuan";
	public static final String peoInfoInvestmrnt = "/Member/Mcenter/touzi";
	// public static final String peoInfoBonus = "/Member/Mcenter/jiangjin";
	public static final String peoInfoMoney = "/Member/Mcenter/zjxx";
	public static final String peopleinfoPay = "/Member/pay/unspay"; // 用户充值
	public static final String peopleinfoEmail = "/Member/Mcenter/verifiEmail"; // 验证email
	
	public static final String jxjlist = "/Member/Mcenter/showTicket";
	public static final String ishasjxj = "/Member/Mcenter/ckNewTicket";
	
	public static final String redpkglist = "/Member/Mcenter/showRedPacket";

	public static final String peoInfoWithdrawal = "/Member/Mcenter/tixian"; // 更新数据
	public static final String peoInfoWithdrawal_2 = "/Member/Mcenter/validate"; // 我要提现—提交前确认
	public static final String peoInfoWithdrawal_3 = "/Member/Mcenter/actwithdraw";// 提取现金
	public static final String peoInfosmrz = "/Member/Mcenter/myverify_personalid"; // 实名认证
	public static final String peoInfosmrz_sx = "/Member/Mcenter/idcard_verify"; // 实名认证
	public static final String peoInfotouxiang = "/Member/Mcenter/upload_photo";
	public static final String peoInfoPhone = "/Member/Mcenter/commitphone"; // 获取手机验证码
	public static final String registerPhone = "/member/Mobilecommon/commitphone"; // 注册获取手机验证码
	public static final String bannerPic = "/Home/Main/slideshow"; // 获取banner图片
	public static final String bannerPicDetail = "/main/bnedit"; // 获取banner图片详情
																	// //
																	// 提交用于验证的手机号
	public static final String peoInfoPhone2 = "/Member/Mcenter/verifyphone"; // 验证手机号
	public static final String peoInfobindbankcard = "/Member/MCenter/bind_debitcard"; // 绑定银行卡
	public static final String peoInfobindbankcard_sx = "/member/Mcenter/bank_verify"; // 绑定银行卡
	public static final String peoInfoxsbankcard = "/Member/MCenter/obtain_bound_debit"; // 显示绑定银行信息
	public static final String peoInfoxsbankcard_sx = "/member/Mcenter/bank_verify"; // 显示绑定银行信息
	public static final String peoInfoxsjiaoyipsw = "/Member/MCenter/change_pay_passwd"; // 交易密码
	public static final String zq_request = "/Main/ajax_debt"; // 债权转让
	public static final String zq_buy_request = "/Main/buy_debt"; // 债权转让

	/** 融宝支付接口 */
	public static final String pay_rongbao_type = "/Pay/reapal_app"; // 债权转让

	/******** 投资管理 ********/

	public static final String invest_index = "/member/Mcenter/invest_index";// 投资管理总表
	public static final String canTransfer = "/member/Mcenter/canTransfer"; // 可转让债权
	public static final String onBonds = "/member/Mcenter/onBonds"; // 可装让债权
	public static final String successDeb = "/member/Mcenter/successDebt"; // 已转让让债权
	public static final String buydetb = "/member/Mcenter/buydetb"; // 已经购买债权
	public static final String sellhtml = "/member/Mcenter/sellhtml"; // 可转让转让债券详情页
	public static final String sell_debt = "/member/Mcenter/sell_debt"; // 可转让转让债券详情页
	public static final String cancel_debt = "/member/Mcenter/cancel_debt"; // 可转让转让债券详情页
	public static final String debt_download = "/member/Mcenter/debt_download"; // 可转让转让债券详情页
	public static final String agreement = "/member/Mcenter/debt_download"; // 债券转让列表之已购买债券协议页
	public static final String cancellist = "/member/Mcenter/cancellist"; // 债权已撤销
	public static final String ondetb = "/member/Mcenter/ondetb"; // 债权回收中

	// 散标
	public static final String borrow_list = "/member/Mcenter/borrow_list";
	// 企业
	public static final String tborrow_list = "/member/Mcenter/tborrow_list";
	// 散标之显示数据(还款详情)
	public static final String borrow_detail = "/member/Mcenter/borrow_detail";
	// 企业直投和散标列表之已购买债券协议页（分类 1是企业 0是散标）
	public static final String borrow_agreement = "/member/Mcenter/borrow_agreement";

	/******** 投资管理 ********/
	public static final String savelong = "/member/mcenter/savelong";
	public static final String autolong = "/member/mcenter/autolong";

	/** VIP申请 获取客服 **/
	public static final String vip_kf_inf = "/member/mcenter/vip_list";
	/** VIP申请 **/
	public static final String hf_vip = "/member/mcenter/apply";
	//首页地址图片请求
	public static final String indexurlpic = "/Home/Main/activityLogo";
	//最新2个标
	public static final String tzList = "/Home/Main/newInvest";
	
	public static final String sygg = "/Main/getinfo";
	
	public static final String tzAllList = "/Main/index_class";
	
	public static final String choosejxjList = "/Home/Main/getTicket";
	
	public static final String chooseredpkgList = "/Home/Main/getRedPacket";
	
	public static final String tznewList = "/Home/Main/newhand";
	
	public static final String zcList = "/Home/Main/raiseIndex";
	//众筹详情
	public static final String zcListxq = "/Home/Main/raiseShowInfor";
	//众筹投标
	public static final String zcListtb = "/Home/Main/raiseInvest";
	//是否投标
	public static final String zcjlissupport = "/Home/Main/raiseVote";
	//首页活动
	public static final String syList_sx = "/Home/Main/event_list";
	// 红包接口
	public static final String mycanbmList = "/member/mcenter/coupon";
	public static final String myusedbmList = "/member/mcenter/usedcoupon";
	public static final String mycannousebmList = "/member/mcenter/lostcoupon";

	public static final String uppic = "/Member/Mcenter/myverify_personalid";
	// 验证码借口
	public static final String getyzm = "/main/getpassword";
	public static final String getyzmyz = "/main/validatephone";
	public static final String yzmyz = "/Member/Mcenter/validate_code_temp";
	public static final String yzmyzsx = "/main/validatephone";
	public static final String addnewpwd = "/Member/Mcenter/find_phone_pinpass";
	//忘记登录密码
	public static final String addloginnewpwdsx = "/Home/main/updatepass";
	//忘记支付密码
	public static final String addzhifunewpwdsx = "/member/Mcenter/find_pin";
	public static final String tzList2 = "/Main/index";
	public static final String tzListItem = "/Main/detail";
	public static final String xxpl = "/Main/getInvestinfor";
	// public static final String tzListItem = "/Main/detail";
	public static final String tzListItem2 = "/Main/ajax_invest";
	// public static final String tzListItem2 = "/Main/ajax_invest";
	public static final String tzListItem3 = "/Main/investcheck";
	public static final String tzListItem4 = "/Main/investmoney";
	public static final String tztListItem = "/Main/detail";
	// public static final String tztListItem = "/Main/tdetail";
	public static final String tztListItem2 = "/Main/ajax_invest";
	// public static final String tztListItem2 = "/Main/tajax_invest";
	public static final String tztListItem3 = "/Main/tinvestcheck";
	public static final String tztListItem4 = "/Main/tinvestmoney";
	// 新闻
	public static final String news = "/Main/getNews";
	public static final String newsListItem = "/Main/event_show";
	public static final String exit = "/Member/Mobilecommon/mactlogout";
	public static final String changepass = "/Member/Mcenter/changepwd"; // 登录密码
	public static final String tradinglog = "/Member/Mcenter/tradinglog"; // 交易记录
	public static final String showPtbjl = "/Main/investlog";
	public static final String showLzbjl = "/Main/tinvestlog";
	public static final String chargeMoney = "/member/MCenter/chargeMoney";
	public static final String withdrawMoney = "/member/MCenter/withdrawMoney";
	// 检测更新
	public static final String version = "/Main/version";
	public static final String getBankInfo = "/member/MCenter/bankinfo";
	public static final String getCity = "/member/MCenter/getcity";
	
	//获取公司法人银行卡接口
	public static final String offinecharge = "/Home/Main/offinecharge"; 
	//上传公司法人银行卡接口
	public static final String offinepay = "/Home/Main/offinepay"; 
	// 活动
	public static final String notice = "/Main/getArticle";
	public static final String noticeListItem = "/Main/gg_show";

	// public static List<String> LEND_MONEY_STATUE = new ArrayList<String>();

	// 意见反馈
	public static final String FEEDBACK = "/Main/feedback";
	public static final String FORGOT_PWD_0 = "/member/common/phtous";// 获取手机验证码
	public static final String FORGOT_PWD_1 = "/main/getpassword";// 获取手机验证码
	// public static final String FORGOT_PWD_2 = "/main/validatephone";
	public static final String FORGOT_PWD_3 = "/main/repreatphone";// 修改密码
	public static final String GET_REGISTER_CONTEXT = "/api/ruleserver";// 注册协议

	/** 富友支付接口 */
	public static final String peopleinfoPay_fy = "/Pay/fuiouapp";

	// 系统信息
	public static String OS_VERSION = "";
	public static String PHONE_MODEL = "";

	// 借款列表
	public static final String LENDMONEY = "/member/MCenter/credit_list";

	// 借款申请
	public static final String LENDMONEY_REQUEST = "/member/MCenter/myrequest_credit";
	//我要借款
	public static final String borrow_sx = "/member/MCenter/member_borrow";

	public static double L_log;

	// -----------------------------------------------------------

	public static final int ANIMATION_LEFT_TO_RIGHT = 1;
	public static final int ANIMATION_RIGHT_TO_LEFT = 2;
	public static final int ANIMATION_TO_LEFT = 3;
	public static final int ANIMATION_TO_RIGHT = 4;

	// 本地存储数据-----------------------------------------------------------
	public static final String userPreferences = "lmqapp";
	public static final String userName = "name";
	public static final String userPassword = "password";
	public static final String userRemember = "remember";
	public static final String userPageStyle = "pagestyle";
	// -----------------------------------------------------------

	public static final int pageStyleLogin = 0;
	public static final int pageStyleInfo = 1;
	public static int layout_type;

	// -----------------------------------------------------------
	public static final int MESSAGE_TOAST = 0;
	public static final int MESSAGE_DIALOG = 1;
	public static final int MESSAGE_BITMAP = 2;
	// -----------------------------------------------------------

	// -----------------------------------------------------------
	public static final int RESULT_REGISTER_TO_LOGIN = 100;
	public static final int RESULT_LOGIN_TO_PEOPLE = 101;
	// -----------------------------------------------------------

	// -----------------------------------------------------------
	public static final int LOGIN_TYPE_2 = 101;
	public static final int LOGIN_TYPE_3 = 102;
	public static final int LOGIN_TYPE_4 = 103;
	// -----------------------------------------------------------

	public static final String PIC_PATH = "/lmqdata/";

	public static boolean isAlive = false;
	public static boolean showNewsList = false;
	public static boolean showNoticeList = false;
	public static long showNewsId;
	public static long showNoticeId;
	// 标记显示新闻还是公告 false 显示新闻 true显示notice
	public static boolean IS_SHOW_NESW_OR_NOTICE = false;

}
