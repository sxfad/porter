/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月07日 10:00
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换器插件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月07日 10:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月07日 10:00
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ConsumeConverterPlugin {
    CANAL_ROW("canalRow", "Canal行格式"),
    OGG_JSON("oggJson", "oggJson格式");

    @Getter private final String code;
    @Getter private final String name;

    public static final List<ConsumeConverterPlugin> PLUGINS = new ArrayList<ConsumeConverterPlugin>()  {
        {
            add(CANAL_ROW);
            add(OGG_JSON);
        }
    };

}
