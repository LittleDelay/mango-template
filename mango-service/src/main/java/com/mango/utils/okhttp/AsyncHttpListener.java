package com.mango.utils.okhttp;

/**
 * 异步HTTP请求监听器
 *
 */
public interface AsyncHttpListener {

    /**
     * 状态枚举
     */
    enum State {

        /**
         * 成功、失败、网络错误
         */
        SUCCESS, FAILURE, NETWORK_ERROR

    }

    /**
     * 成功回调
     *
     * @param data 返回数据
     */
    void onSuccess(String data);

    /**
     * 失败回调
     *
     * @param state   状态
     * @param message 错误消息
     */
    void onFail(State state, String message);

    /**
     * 重试回调
     *
     * @param data  业务数据
     * @param retry 重试次数
     */
    void onRetry(String data, int retry);

}