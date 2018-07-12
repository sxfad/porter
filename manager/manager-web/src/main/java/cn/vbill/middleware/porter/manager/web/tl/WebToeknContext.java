/**
 *
 */
package cn.vbill.middleware.porter.manager.web.tl;

import cn.vbill.middleware.porter.manager.web.token.Token;
import cn.vbill.middleware.porter.manager.web.token.TokenUtil;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class WebToeknContext {

    private static ThreadLocal<String> TOKENHODLER = new ThreadLocal<String>();

    public static void initToken(String tokenId) {
        TOKENHODLER.set(tokenId);
    }

    public static ThreadLocal<String> getTokenHodler() {
        return TOKENHODLER;
    }

    public static void setTokenHodler(ThreadLocal<String> tokenHodler) {
        WebToeknContext.TOKENHODLER = tokenHodler;
    }

    public static <T extends Token> T getToken(Class<T> classT) throws Exception {
        return TokenUtil.unsign(TOKENHODLER.get(), classT);
    }
}
