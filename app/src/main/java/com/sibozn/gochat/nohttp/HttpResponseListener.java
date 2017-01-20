/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sibozn.gochat.nohttp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.sibozn.gochat.R;
import com.sibozn.gochat.dialog.WaitDialog;
import com.sibozn.gochat.utils.Tools;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ParseError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.net.ProtocolException;

/**
 * Created in Nov 4, 2016 8 01
 *
 * @author
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private Context mContext;
    /**
     * Dialog.
     */
    private WaitDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<T> callback;

    /**
     * @param context      context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Context context, Request<?> request, HttpListener<T> httpCallback,
                                boolean canCancel, boolean isLoading) {
        this.mContext = context;
        this.mRequest = request;
        if (context != null && isLoading) {
            mWaitDialog = new WaitDialog(context);
            mWaitDialog.setMessage(context.getText(R.string.wait_dialog_title));
            mWaitDialog.setCancelable(canCancel);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        if (mWaitDialog != null && mContext != null && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();
        if (responseCode > 400 && mContext != null) {
            if (responseCode == 405) {// 405表示服务器不支持这种请求方法，比如GET、POST、TRACE中的TRACE就很少有服务器支持。
                // mActivity.showMessageDialog(R.string.request_succeed, R.string.request_method_not_allow);
                Toast.makeText(mContext, "responseCode == 405", Toast.LENGTH_SHORT).show();
            } else {// 但是其它400+的响应码服务器一般会有流输出。
                // mActivity.showWebDialog(response);
                Toast.makeText(mContext, "400+的响应码", Toast.LENGTH_SHORT).show();
            }
        }
        if (callback != null) {
            callback.onSucceed(what, response);
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {// 网络不好
            // Snackbar.show(mActivity, R.string.error_please_check_network);
            Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof TimeoutError) {// 请求超时
            // Snackbar.show(mActivity, R.string.error_timeout);
            Toast.makeText(mContext, "请求超时，网络不好或者服务器不稳定。", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            //Snackbar.show(mActivity, R.string.error_not_found_server);
            Toast.makeText(mContext, "未发现指定服务器。", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof URLError) {// URL是错的
            Toast.makeText(mContext, "URL错误。", Toast.LENGTH_SHORT).show();
            //Snackbar.show(mActivity, R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            Toast.makeText(mContext, "没有发现缓存。", Toast.LENGTH_SHORT).show();
            //Snackbar.show(mActivity, R.string.error_not_found_cache);
        } else if (exception instanceof ProtocolException) {
            Toast.makeText(mContext, "系统不支持的请求方式。", Toast.LENGTH_SHORT).show();
            //Snackbar.show(mActivity, R.string.error_system_unsupport_method);
        } else if (exception instanceof ParseError) {
            Toast.makeText(mContext, "解析错误。", Toast.LENGTH_SHORT).show();
            //Snackbar.show(mActivity, R.string.error_parse_data_error);
        } else {
            Toast.makeText(mContext, "未知错误。", Toast.LENGTH_SHORT).show();
            //Snackbar.show(mActivity, R.string.error_unknow);
        }
        Logger.e("错误：" + exception.getMessage());
        if (callback != null)
            callback.onFailed(what, response);
    }

}
