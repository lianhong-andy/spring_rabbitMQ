import com.andy.dao.AdminDao;
import com.andy.domain.TourAdmin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@WebAppConfiguration
public class TestR {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    public void testT(){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> map = new HashMap<>();
        map.put("page",1);
        map.put("size",10);
        sql.append("select * from tour_admin limit :page, :size");
        List<TourAdmin> list = namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(TourAdmin.class));
        System.out.println("list = " + list);


//        Integer count = jdbcTemplate.queryForObject("select count(*) from tour_admin", new BeanPropertyRowMapper<>(Integer.class));
//        System.out.println("count = " + count);
    }

    @Test
    public void test2(){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT g.`pid`,g.goods_id  FROM tour_goods_info g  WHERE g.is_on_sale = ?");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql.toString(), 1);
        System.out.println("maps = " + maps);

    }

    @Test
    public void test3(){
        List<String> taoCanIdByGoodsId = new ArrayList<>();
        taoCanIdByGoodsId.add("659");
        taoCanIdByGoodsId.add("660");
        taoCanIdByGoodsId.add("11");
        String sql = "SELECT pid,master_title FROM tour_package_info WHERE pid in (:pids) ";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("pids",taoCanIdByGoodsId);
        List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(sql,paramMap);
        System.out.println("maps = " + maps);
        for (Map<String, Object> map : maps) {
            Object o = map.get("pid");
            System.out.println("o = " + o);
        }

    }

    @Autowired
    private AdminDao adminDao;

    @Resource(name = "amqpTemplate")
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendQueue() {
        // convertAndSend 将Java对象转换为消息发送至匹配key的交换机中Exchange
        String exchange_key = "goods.events";
        String routing_key = "event.tuor.200";
        List<TourAdmin> all = adminDao.findAll(1, 10);
        TourAdmin admin = all.get(0);
//        amqpTemplate.convertAndSend(admin);
        amqpTemplate.convertAndSend(exchange_key,routing_key,"kelly");
    }
}
