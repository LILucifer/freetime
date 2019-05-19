package com.weixiao.smart.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-07 16:38.
 */
@Component
public class MysqlData {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public String getUserList() {
        String sql = "SELECT * FROM admin";
        Connection connection = null;
        try {
             connection = jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List list = jdbcTemplate.queryForList(sql);
        return "";
    }

}
