package com.mango.utils.validate;

import com.bzn.util.response.ResponseBzn;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 公用校验方法
 *
 * @author dzz
 * @version 1.0.0
 * @since 2020年04月26 下午3:36
 */
@Slf4j
@Component
public class CommonValidateTools {

    /**
     * 校验是否为空
     *
     * @param data 待校验参数
     * @param msg  提示消息
     * @return 校验结果
     */
    public ResponseBzn<String> validateEmpty(String data, String msg) {
        return Strings.isNullOrEmpty(data) ? ResponseBzn.fail(msg) : ResponseBzn.ok();
    }

    /**
     * 校验是否为null
     *
     * @param data 待校验参数
     * @param msg  提示消息
     * @return 校验结果
     */
    public ResponseBzn<String> validateNull(Object data, String msg) {
        return Objects.isNull(data) ? ResponseBzn.fail(msg) : ResponseBzn.ok();
    }

    /**
     * 校验手机号
     *
     * @param mobile 手机号
     * @return 校验结果
     */
    public ResponseBzn<String> isMobile(String mobile) {
        if (org.apache.commons.lang3.StringUtils.isBlank(mobile)) {
            return ResponseBzn.fail("手机号码不能为空");
        }
        return mobile.trim().matches("^1\\d{10}$") ? ResponseBzn.ok() : ResponseBzn.fail("手机号码格式错误");
    }

}
