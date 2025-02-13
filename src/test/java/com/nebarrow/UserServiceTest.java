package com.nebarrow;

import com.nebarrow.weathertracker.config.SpringConfiguration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfiguration.class)
@WebAppConfiguration
public class UserServiceTest {

    @Test
    public void tryTest() {
        System.out.println(1);
    }
}
