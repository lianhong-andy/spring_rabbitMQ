import com.andy.util.LogUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lianhong
 * @description 日志测试
 * @date 2019/5/12 0012下午 3:13
 */
public class TestLog {
    private static final Logger logger = LoggerFactory.getLogger(TestLog.class);
    @Test
    public void test1() {
        LogUtils.logInfo("INFO ~");
        LogUtils.logDebug("DEBUG ~");
        LogUtils.logError("ERROR~");
        LogUtils.logWarn("WARN ~");

    }

    @Test
    public void test2(){
        logger.info("日志输出拉~~~");
    }
}
