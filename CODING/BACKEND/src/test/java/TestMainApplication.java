import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static com.project.JewelryMS.service.DashboardService.convertLocalDateToDate;

public class TestMainApplication {
    public static void main(String[] args){
            LocalDate localDate = LocalDate.of(2024, 6, 25);
            LocalDateTime localDateTime = localDate.atTime(LocalTime.MAX);
            System.out.println(localDateTime);
        }

        public static Date convertLocalDateToDate(LocalDate localDate) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }


}
