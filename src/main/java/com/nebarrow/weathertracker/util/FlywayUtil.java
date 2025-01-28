package com.nebarrow.weathertracker.util;

import lombok.experimental.UtilityClass;
import org.flywaydb.core.Flyway;

@UtilityClass
public class FlywayUtil {

    public static void applyMigrations() {
        var flyway = Flyway.configure()
                .dataSource(
                        System.getenv("DB_URL"),
                        System.getenv("DB_USER"),
                        System.getenv("DB_PASSWORD"))
                .locations("classpath:db/migrations")
                .load();
        flyway.migrate();
    }
}
