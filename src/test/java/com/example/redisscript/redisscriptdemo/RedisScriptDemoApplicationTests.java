package com.example.redisscript.redisscriptdemo;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.file.FileUtils;
import com.alipay.api.response.AlipayUserAgreementQueryResponse;
import com.alipay.api.response.AlipayUserAgreementUnsignResponse;
import com.example.redisscript.redisscriptdemo.dto.UnsignOperateTypeEnum;
import com.example.redisscript.redisscriptdemo.entity.OrderInfo;
import com.example.redisscript.redisscriptdemo.service.AliPayChannel;
import com.example.redisscript.redisscriptdemo.service.OrderInfoService;
import com.example.redisscript.redisscriptdemo.service.TryLockService;
import com.example.redisscript.redisscriptdemo.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@Slf4j
class RedisScriptDemoApplicationTests {

    @Resource
    @Lazy
    private TryLockService tryLockService;
    @Resource
    private AliPayChannel aliPayChannel;
    @Resource
    private OrderInfoService orderInfoService;

    @Test
    void contextLoads() {
        String key = UUID.randomUUID().toString();
        String value = key.replace("-", "");
        boolean lock = RedisLock.tryLock(key, value, Duration.ofMinutes(2));
        log.info("key:{},value:{},lock:{}", key, value, lock);

        String value3 = value + "1";
        boolean unLockValue = RedisLock.unLock(key, value3);
        log.info("key:{},value:{},unLock:{}", key, value3, unLockValue);

        boolean unLock = RedisLock.unLock(key, value);
        log.info("key:{},value:{},unLock:{}", key, value, unLock);

        String key2 = UUID.randomUUID().toString();
        String value2 = key2.replace("-", "");
        boolean lock2 = RedisLock.tryLock(key2, value2, 2, TimeUnit.MINUTES);
        log.info("key:{},value:{},lock:{}", key2, value2, lock2);
        boolean unLock2 = RedisLock.unLock(key2, value2);
        log.info("key:{},value:{},unLock:{}", key2, value2, unLock2);

    }

    @Test
    public void testRedisson() throws InterruptedException {
        String key = UUID.randomUUID().toString();
        for (int i = 0; i < 6; i++) {
            new Thread(() -> tryLockService.tryLockByKey(key)).start();
        }
        Thread.sleep(1000 * 50);
    }

    @Test
    public void testCyclePayUserAgreementUnsign(){
        String noListS = "20225819887222982447,20225911895552510447,20225914896445400447,20225915896978131447,20225915896978143447,20225915896919199447,20225915896913127447,20225915897083057447,20225915897097705447,20225916896957007447";
        String userId = "2088412875099472";
        String[] noList = noListS.split(",");
        for (String no:noList){
            AlipayUserAgreementUnsignResponse response = aliPayChannel.cyclePayUserAgreementUnsign(userId, null, no, UnsignOperateTypeEnum.confirm);
        }
    }

    @Test
    public void testQueryOrderInfo(){
        OrderInfo orderInfo = orderInfoService.checkIsTestOrderInfoByChannelAgreementNo("20225624864387778661");
        if (Objects.nonNull(orderInfo)) {
            log.info(orderInfo.toString());
        }
    }


    @Test
    public void testCyclePayUserAgreementQuery() {
        AlipayUserAgreementQueryResponse response = aliPayChannel.cyclePayUserAgreementQuery("", "20225815878927017553");
        log.info(response.getBody());
    }


    @Test
    public void testCyclePayUserAgreementQueryFormFile() throws IOException {
        Path path = FileSystems.getDefault().getPath("F:\\Program Files\\Notepad++", "CHANNEL_AGREEMENT_NO.txt");
        JSONObject rj = new JSONObject();
        String noS = FileUtils.readFileToString(path.toFile(), Charset.defaultCharset());
        List<String> noList = Arrays.asList(noS.split(","));
        List<String> errorNoList = new ArrayList<>();
        for (String no : noList) {
            AlipayUserAgreementQueryResponse response = aliPayChannel.cyclePayUserAgreementQuery("", no);
            if (!response.isSuccess()) {
                rj.put("error_no_".concat(no), JSONObject.parseObject(response.getBody()).getJSONObject("alipay_user_agreement_query_response"));
                errorNoList.add(no);
            }
        }
        if(rj.size() != 0){
            Path resultPath = FileSystems.getDefault().getPath("F:\\Program Files\\Notepad++","ERROR_CHANNEL_AGREEMENT_NO_LIST.txt");
            File file =resultPath.toFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(StringUtils.joinWith(",",errorNoList.toArray()).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        }
    }
}
