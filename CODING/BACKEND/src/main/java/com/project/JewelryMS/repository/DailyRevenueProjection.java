package com.project.JewelryMS.repository;

import java.util.Date;

public interface DailyRevenueProjection {
    Date getPurchaseDate();
    Float getTotalRevenue();
}
