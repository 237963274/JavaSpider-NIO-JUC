package com.kedian.time;

import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.time
 * @Description:
 * @date 2019/7/25
 */
public class TestTimeAPI {

    //1.LocalDate 年月日、LocalTime 时分秒、LocalDateTime 日期
    @Test
    public void test1() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);

        LocalDateTime ldt = LocalDateTime.of(2019, 07, 25, 14, 10, 10);
        System.out.println(ldt);

        //日期运算：plus 加
        LocalDateTime ldt2 = ldt.plusYears(1);
        System.out.println(ldt2);

        //日期运算：minus 减
        LocalDateTime ldt3 = ldt.minusMonths(2);
        System.out.println(ldt3);

        System.out.println(ldt3.getYear());
        System.out.println(ldt3.getMonthValue());
        System.out.println(ldt3.getDayOfMonth());
        System.out.println(ldt3.getDayOfWeek());
        System.out.println(ldt3.getDayOfYear());
        System.out.println(ldt3.getHour());
        System.out.println(ldt3.getMinute());
        System.out.println(ldt3.getSecond());
    }

    //2. Instant : 时间戳。 （使用 Unix 元年  1970年1月1日 00:00:00 所经历的毫秒值）
    @Test
    public void test2() {
        //默认使用 UTC 时区,与中国的时区差8小时
        Instant ins = Instant.now();
        System.out.println(ins);

        OffsetDateTime odt = ins.atOffset(ZoneOffset.ofHours(8));
        System.out.println(odt);

        System.out.println(ins.toEpochMilli());
        System.out.println(ins.getNano());

        Instant ins2 = Instant.ofEpochSecond(5);
        System.out.println(ins2);
    }

    /**
     * 3.计算日期间隔
     * Duration : 用于计算两个“时间”间隔
     * Period : 用于计算两个“日期”间隔
     */
    @Test
    public void test3() throws InterruptedException {
        Instant ins1 = Instant.now();
        Thread.sleep(1000);
        Instant ins2 = Instant.now();
        Duration duration = Duration.between(ins1, ins2);
        System.out.println(duration);

        LocalDate ld1 = LocalDate.now();
        LocalDate ld2 = LocalDate.of(2020, 12, 1);
        Period period = Period.between(ld2, ld1);
        System.out.println(period);
    }

    /**
     * 4.DateTimeFormatter : 解析和格式化日期或时间
     */
    @Test
    public void test4() {
       /* DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        //日期转字符串
        String dateStr = df.format(now);
        System.out.println(dateStr);*/

        //字符串转日期
        String str = "2019-05-22 12:23:36";
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDate parse = LocalDate.parse(str, df2);
        System.out.println(parse);

        // 定义一个任意格式的日期时间字符串
        String str1 = "2014==04==12 01时06分09秒";
        // 根据需要解析的日期、时间字符串定义解析所用的格式器
        DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy==MM==dd HH时mm分ss秒");
        // 执行解析
        LocalDateTime dt1 = LocalDateTime.parse(str1, fomatter1);
        System.out.println(dt1); // 输出 2014-04-12T01:06:09
    }

    /**
     * 5.TemporalAdjuster : 时间校正器
     */
    @Test
    public void test5() {
        LocalDateTime ldt1 = LocalDateTime.now();
        //将号数改为10号
        LocalDateTime ldt2 = ldt1.withDayOfMonth(10);
        System.out.println(ldt1);
        System.out.println(ldt2);
        //偏移到下个周日
        LocalDateTime ldt3 = ldt1.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        System.out.println(ldt3);

        //自定义:查看下一个工作日号数
        LocalDateTime nextWorkDay = ldt1.with((t) -> {
            LocalDateTime ldt4 = (LocalDateTime) t;
            DayOfWeek dow = ldt4.getDayOfWeek();
            if (dow.equals(DayOfWeek.FRIDAY)) {
                return ldt4.plusDays(3);
            } else if (dow.equals(DayOfWeek.SATURDAY)) {
                return ldt4.plusDays(2);
            } else {
                return ldt4.plusDays(1);
            }
        });
        System.out.println(nextWorkDay);
    }

    /**
     * 6.ZonedDate、ZonedTime、ZonedDateTime ： 带时区的时间或日期
     */
    @Test
    public void test6(){
        //查看时区
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        zoneIds.forEach(System.out::println);
        //根据时区创建日期
        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println(ldt);

        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println(zdt);
    }
}
