/**
 *
 */
package com.suixingpay.datas.manager.web.tl;

import com.suixingpay.datas.manager.web.token.Token;
import com.suixingpay.datas.manager.web.token.TokenUtil;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class WebToeknContext {

    private static ThreadLocal<String> tokenHodler = new ThreadLocal<String>();

    public static void initToken(String tokenId) {
        tokenHodler.set(tokenId);
    }

    public static ThreadLocal<String> getTokenHodler() {
        return tokenHodler;
    }

    public static void setTokenHodler(ThreadLocal<String> tokenHodler) {
        WebToeknContext.tokenHodler = tokenHodler;
    }

    public static <T extends Token> T getToken(Class<T> classT) throws Exception {
        return TokenUtil.unsign(tokenHodler.get(), classT);
    }
}
