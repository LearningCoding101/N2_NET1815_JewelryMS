import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.project.JewelryMS.service.DashboardService.convertLocalDateToDate;

public class TestMainApplication {
    public static void main(String[] args){
            LocalDate localDate = LocalDate.of(2024, 6, 25);
            Date date = convertLocalDateToDate(localDate);
            System.out.println(date);
        }

        public static Date convertLocalDateToDate(LocalDate localDate) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }


}
