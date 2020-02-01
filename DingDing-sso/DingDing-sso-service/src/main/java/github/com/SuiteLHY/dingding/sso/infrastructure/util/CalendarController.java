package github.com.suitelhy.dingding.sso.infrastructure.util;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 农历工具类
 * @description 目前仅精确到：年 -> 月 -> 日
 * @author Suite
 * @source http://www.it610.com/article/271380.htm
 * 
 */
class LunarCalendarUtil {
	/** 
	 * 基准日期
	 * @expectedValue：1900年1月31日 
	 */
	private static Calendar baseDate;
	
	private int year;
	
    private int month;
    
    private int day;
    
    private boolean leap;
    
    /*static SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy年MM月dd日");*/
    
    /** 天干(天文学 - 术语) */
    private final static String[] CELESTIAL_STEM = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
    
    private final static String[] CHINESE_NUMBER = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
			"十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "二十一",
			"二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十", "三十一"
    };

    /** (中国古语) */
    private final static String CHINESE_TEN[] = { "初", "十", "廿", "卅" };
    
    final static long[] LUNAR_INFO = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
    		0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
    		0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
    		0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
    		0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
    		0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
    		0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
    		0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
    		0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
    		0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
    		0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
    		0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
    		0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
    		0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
    		0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
    		0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
    		0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
    		0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
    		0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
    		0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
    		0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
    		0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 
    };
    
    /** 地支(天文学 - 术语) */
    private final static String[] TERRESTRIAL_BRANCH = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    
    /** 十二生肖 */
    private final static String[] ZODIAC_ANIMALS = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    
    //===== init method =====//
    /**
     * 初始化基准日期,赋值为1900年1月31日对应的内容
     */
    private static void initBaseDate() {
    	if (null == baseDate) {
    		baseDate = Calendar.getInstance();
    	}
    	//--- 基准日期：1900年1月31日
	    // 年份
	    baseDate.set(Calendar.YEAR, 1900);
	    // 月份
	    baseDate.set(Calendar.MONTH, 0);
	    // 当月的日期
	    baseDate.set(Calendar.DAY_OF_MONTH, 31);
	    // 时
	    baseDate.set(Calendar.HOUR_OF_DAY, 0);
	    // 分
	    baseDate.set(Calendar.MINUTE, 0);
	    // 秒
	    baseDate.set(Calendar.SECOND, 0);
	    // 毫秒
	    baseDate.set(Calendar.MILLISECOND, 0);
    }
    
    //===== Constructor =====//
    /**
     * <code>LunarCalendarUtil</code>无参构造方法
     */
    public LunarCalendarUtil() {
    	if (null == baseDate) {
    		initBaseDate();
    	}
    }
    
    /**
     * <code>LunarCalendarUtil</code>有参构造方法
     * @description 
     * 	传出y年m月d日对应的农历. 　　
     * 	yearCyl3:农历年与1864的相差数 ? 　　
     * 	monCyl4:从1900年1月31日以来,闰月数 　　
     * 	dayCyl5:与1900年1月31日相差的天数,再加40 ? 
     * @param cal 　　
     * @return 　　
     */
    public LunarCalendarUtil(Calendar cal) {
    	if (null == baseDate) {
    		initBaseDate();
    	}
        @SuppressWarnings("unused")
        int yearCyl, monCyl, dayCyl;
        int leapMonth = 0;
        //=== 求出和1900年1月31日相差的天数
        int offset = (int) ((cal.getTime().getTime() - baseDate.getTimeInMillis()) / 86400000L);
        dayCyl = offset + 40;
        monCyl = 14;
        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
            monCyl -= 12;
        }
        // 农历年份
        year = iYear;
        yearCyl = iYear - 1864;
        leapMonth = leapMonth(iYear);// 闰哪个月,1-12
        leap = false;
        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(year);
            } else {
                daysOfMonth = monthDays(year, iMonth);
            }
            offset -= daysOfMonth;
            // 解除闰月
            if (leap && iMonth == (leapMonth + 1))
                leap = false;
            if (!leap)
                monCyl++;
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        month = iMonth;
        day = offset + 1;
    }
    
    //=====//
    
    /**
     * 传回农历 y年的总天数
     * @param y
     * @return
     */
    final private static Integer yearDays(@NotNull Integer y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((LUNAR_INFO[y - 1900] & i) != 0) {
                sum += 1;
            }
        }
        return (sum + leapDays(y));
    }

    /**
     * 传回农历 y年闰月的天数
     * @param y
     * @return
     */
    final private static Integer leapDays(@NotNull Integer y) {
        if (leapMonth(y) != 0) {
            if ((LUNAR_INFO[y - 1900] & 0x10000) != 0) {
                return 30;
            }
            return 29;
        }
        return 0;
    }

    /**
     * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
     * @param y
     * @return
     */
    final private static Integer leapMonth(@NotNull Integer y) {
        return (int) (LUNAR_INFO[y - 1900] & 0xf);
    }

    /**
     * 传回农历 y年m月的总天数
     * @param y
     * @param m
     * @return
     */
    final private static Integer monthDays(@NotNull Integer y
			, @NotNull Integer m) {
        return ((LUNAR_INFO[y - 1900] & (0x10000 >> m)) == 0) ? 29 : 30;
    }

    /**
     * 传入月日的offset 传回干支, 0=甲子
     * @param num - <b>(年份对应的)农历纪年数</b>（容错模式处理）
     * @return - <b>(农历纪年数对应的)干支纪年数</b>
     */
    private final static String cycle(@NotNull Integer num) {
        /*final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
        final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };*/
        return CELESTIAL_STEM[num % CELESTIAL_STEM.length]
				+ TERRESTRIAL_BRANCH[num % TERRESTRIAL_BRANCH.length];
    }

    //===== Get And Set Function =====//
    /**
     * 获取农历中的生肖
     * @return
     */
    final public String getZodiacAnimals() {
        /*final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };*/
        return getZodiacAnimals(this.year);
    }
    
    /**
     * 计算指定年份对应的农历中的生肖
     * @param year - <b>年份</B>
     * @return
     */
    public final static String getZodiacAnimals(@NotNull Integer year) {
        return ZODIAC_ANIMALS[(year - 4) % ZODIAC_ANIMALS.length];
    }
    
    /**
     * 获取当前<code>LunarCalendar</code>对应的农历日
     * @return
     */
    public String getChineseDay() {
    	return getChineseDay(this.day);
    }
    
    /**
     * 获取农历的(当月)日数
     * @description 容错模式
     * @param day
     * @return
     */
    public static String getChineseDay(@NotNull Integer day) {
        if (day > 30) return "";
        if (day == 10) return "初十";
        return CHINESE_TEN[(day / 10) % CHINESE_TEN.length]
				+ CHINESE_NUMBER[(day % 10 == 0) ? 9 : (day % 10 - 1)];
    }
    
    public String getChineseMonth() {
    	return getChineseMonth(this.month);
    }
    
    public static String getChineseMonth(@NotNull Integer month) {
    	return (CHINESE_NUMBER[(month - 1) % CHINESE_NUMBER.length])
				+ "月";
    }
    
    /*public String getChineseYear() {
    	return getChineseYear(this.year);
    }
    
    // 该方法暂未完善,屏蔽
    public static String getChineseYear(int year) {
    	return CHINESE_NUMBER[(year - 1) % CHINESE_NUMBER.length] + "年";
    }*/
    
    /**
     * 传入 offset 传回干支, 0=甲子
     * @return
     */
    final public String getCycle() {
    	return cycle(year - 1900 + 36);
    }
    
    //===== Service Implementation =====//
    public String toString() {
        return year + "年"
				+ (leap ? "闰" : "")
				+ CHINESE_NUMBER[(month - 1) % CHINESE_NUMBER.length]
				+"月"
				+ getChineseDay(day);
    }
    
    //=====//

    /* public static void main(String[] args) throws ParseException {
        Calendar today = Calendar.getInstance();
        today.setTime();
        LunarCalendarUtil lunar = new LunarCalendarUtil(today);
        System.out.println("北京时间："+ chineseDateFormat.format(today.getTime()) +"　农历"+ lunar);
    }*/
	
}

/**
 * Calendar控制器
 * @author LuoPei
 *
 */
public class CalendarController {
	//===== Data members =====//
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	protected Calendar calendar;
	
	/**
	 * 采用 ThreadLocal 避免 SimpleDateFormat 非线程安全的问题
	 */
	protected static final ThreadLocal<Map<String, DateFormat>> safeSdf = /*new ThreadLocal<Map<String, DateFormat>>() {
	    @Override
	    protected Map<String, DateFormat> initialValue() {
	    	Map<String, DateFormat> dateFormatMap = new HashMap<>();
	    	dateFormatMap.put(DEFAULT_DATE_FORMAT, new SimpleDateFormat(DEFAULT_DATE_FORMAT));
	        return dateFormatMap;
	    }
	}*/ThreadLocal.withInitial(() -> {
			Map<String, DateFormat> dateFormatMap = new HashMap<>();
			dateFormatMap.put(DEFAULT_DATE_FORMAT, new SimpleDateFormat(DEFAULT_DATE_FORMAT));
			return dateFormatMap;
	});
	
	/** 年份 */
	protected int year;
	
	/** 
	 * 月份
	 * @description base on 1
	 * @example <b>1月</b>对应<code>1</code>
	 */
	protected int month;
	
	/** 
	 * 天数(当月的天数)
	 * @description base on 1
	 * @example <b>当月第1天</b>对应<code>1</code>
	 */
	protected int day;
	
	/** 
	 * 时[24时制]
	 * @example 
	 * 	<b>0点</b>对应<code>0</code></br>
	 * 	<b>23点</b>对应<code>23</code>
	 */
	protected int hour;
	
	/** 分 */
	protected int minute;
	
	/** 秒 */
	protected int second;
	
	/** 毫秒 */
	protected int millisecond;
	
	//===== Internal methods =====//
	protected static DateFormat safeSdf() {
		return safeSdf.get().get(DEFAULT_DATE_FORMAT);
	}
	
	/** (临时策略;暂无更优策略取代) */
	protected static DateFormat safeSdf(String pattern) {
		if (null == pattern) {
			return safeSdf.get().get(DEFAULT_DATE_FORMAT);
		}
		if (safeSdf.get().size() >= 512) {// (临时策略)
			/*throw new Exception("当前线程中的时间模板实例已达到上限,新增实例操作被拒绝!");*/
			return new SimpleDateFormat(pattern);
		}
		if (!safeSdf.get().containsKey(pattern) || null == safeSdf.get().get(pattern)) {
			safeSdf.get().put(pattern, new SimpleDateFormat(pattern));
		}
		return safeSdf.get().get(pattern);
	}
	
	protected DateFormat getSafeSdf() {
		return safeSdf();
	}
	
	protected void updateCalendar() {
		// 年份
		year = this.calendar.get(Calendar.YEAR);
		// 月份
		month = this.calendar.get(Calendar.MONTH) + 1;
		// (当月的)日期
		day = this.calendar.get(Calendar.DAY_OF_MONTH);
		// 时[24时制]
		hour = this.calendar.get(Calendar.HOUR_OF_DAY);
		// 分
		minute = this.calendar.get(Calendar.MINUTE);
		// 秒
		second = this.calendar.get(Calendar.SECOND);
		// 毫秒
		millisecond = this.calendar.get(Calendar.MILLISECOND);
	}
	
	protected void updateCalendar(Calendar newDate) {
		this.calendar = (Calendar) newDate.clone();
		updateCalendar();
	}
	
	//===== Constructor =====//
	public CalendarController() {
		this.calendar = Calendar.getInstance();
		updateCalendar();
	}
	
	public CalendarController(Calendar calendar) {
		updateCalendar(calendar);
	}
	
	public CalendarController(Date date) {
		this.calendar = Calendar.getInstance();
		this.calendar.setTime(date);
		updateCalendar();
	}
	
	//===== getter and setter =====//
	/**
	 * 获取当前 <code>CalendarController</code> 的 <code>Calendar</code>对象拷贝
	 * @return
	 */
	public Calendar getCalendar() {
		return (Calendar) calendar.clone();
	}
	
	public void setCalendar(Calendar calendar) {
		if (null != calendar) {
			updateCalendar(calendar);
		}
	}
	
	public String getChineseDay() {
		return LunarCalendarUtil.getChineseDay(this.day);
	}
	
	public static String getChineseDay(int day) {
		return LunarCalendarUtil.getChineseDay(day);
	}
	
	public String getChineseMonth() {
		return LunarCalendarUtil.getChineseMonth(this.month);
	}
	
	public static String getChineseMonth(int month) {
		return LunarCalendarUtil.getChineseMonth(month);
	}

	/*public String getChineseYear() {
		return LunarCalendarUtil.getChineseYear(this.year);
	}

	// 相关方法暂未完善,屏蔽
	public static String getChineseYear(int year) {
		return LunarCalendarUtil.getChineseYear(year);
	}*/

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMillisecond() {
		return millisecond;
	}

	public void setMillisecond(int millisecond) {
		this.millisecond = millisecond;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public Date getTime() {
		return calendar.getTime();
	}

	public void setTime(Date time) {
		this.calendar.setTime(time);
	}

	public long getTimeInMillis() {
		return calendar.getTimeInMillis();
	}

	public void setTimeInMillis(long time) {
		this.calendar.setTimeInMillis(time);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * 获取对应的农历中的生肖
	 * @return 生肖名称
	 */
	public final String getZodiacAnimals() {
		return LunarCalendarUtil.getZodiacAnimals(this.year);
	}

	/**
	 * 计算指定年份对应的农历中的生肖
	 * @param year - 年份
	 * @return 生肖名称
	 */
	public static final String getZodiacAnimals(@NotNull Integer year) {
		return LunarCalendarUtil.getZodiacAnimals(year);
	}
	
	//===== Service Implementation =====//
	/**
	 * 将当前 <code>CalendarController</code> 的时间转换为模板
	 * @return
	 */
	public String format() {
		return safeSdf().format(this.calendar.getTime());
	}
	
	/**
	 * 将时间转换为模板
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return safeSdf().format(date);
	}
	
	/**
	 * 将时间转换为模板
	 * @since 临时策略;暂无更优策略
	 * @param pattern - <b>用于解析的时间模板</b>
	 * @param date - <b>被解析的时间类型对象</b>
	 * @return the formatted time string.
	 */
	public static String format(String pattern, Date date) {
		return safeSdf(pattern).format(date);
	}
	
	/**
	 * 根据标准模板解析并转换时间
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(@NotNull String data) throws ParseException {
    	try {
    		return safeSdf().parse(data);
    	} catch (ParseException e) {
    		e.printStackTrace();
    		throw e;
    	}
    }

	/**
	 * 根据标准模板, 判断能否解析
	 * @param data
	 * @return
	 */
	public static boolean isParse(@NotNull String data) {
		try {
			safeSdf().parse(data);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据指定模板解析并转换时间
	 * @param pattern - <b>指定模板</b>
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String pattern, String data) throws ParseException {
    	try {
    		return safeSdf(pattern).parse(data);
    	} catch (ParseException e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
	
	/**
	 * <code>Calendar</code>#<code>toString()</code>方法
	 * @return
	 */
	public String toCalendarString() {
		return calendar.toString();
	}
	
	/**
	 * 获取CalendarController的时间数据
	 * @return <code>yyyy-MM-dd HH:mm:ss</code>格式数据
	 */
	public String toString() {
		return year
				+ "-" + ((month < 10) ? "0" + month : month)
				+ "-" + ((day < 10) ? "0" + day : day) 
				+ " " + ((hour < 10) ? "0" + hour : hour) 
				+ ":" + ((minute < 10) ? "0" + minute : minute) 
				+ ":" + ((second < 10) ? "0" + second : second);
	}
	
	//=====//
	/*
	// CalendarController的线程安全性验证
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = "2019-02-25 10:34:00";
	    // (2)创建多个线程，并启动
	    for (int i = 0; i < 10; i++) {
	        Thread thread = new Thread(new Runnable() {
	        	@Override
	            public void run() {
	            	// (3)使用单例日期实例解析文本(线程不安全)
	            	System.err.println("The singleton pattern of SimpleDateFormat:");
	                try {
	                	Date date = sdf.parse(dateStr);
	                	System.out.println("SimpleDateFormat#parse -> " + date.toString());
	    				System.out.println("SimpleDateFormat#format -> " + sdf.format(date));
	                } catch (ParseException e) {
	                    e.printStackTrace();
	                }
	                // 使用CalendarController(线程安全)
    				System.err.println("CalendarController:");
                    try {
                    	Date date = CalendarController.parse(dateStr);
						System.out.println("CalendarController#parse -> " + date);
						System.out.println("CalendarController#format -> " + CalendarController.format(date));
						System.out.println("CalendarController#toString -> " + new CalendarController(date).toString());
						System.out.println("CalendarController#toCalendarString -> " + new CalendarController(date).toCalendarString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
	            }
	        });
	        thread.start();// (4)启动线程
	    }
	}*/
	
	/*// 中国农历验证
	public static void main(String[] args) {
		CalendarController cc = new CalendarController();
		System.out.println("CalendarController#getZodiacAnimals@int -> " + CalendarController.getZodiacAnimals(cc.getYear()));
		System.out.println("CalendarController#getMonth -> " + cc.getMonth());
		System.out.println(CalendarController.getChineseMonth(cc.getMonth()));
		System.out.println("CalendarController#getDay -> " + cc.getDay());
		System.out.println(CalendarController.getChineseDay(cc.getDay()));
	}*/
	
}