/*
 * Copyright ©2018 vbill.cn.
 * <p>
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
 * </p>
 */

package cn.vbill.middleware.porter.manager.web.config;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基类
 */
public class CommonController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 判断对象是否为空,如果为空将抛出 {@link NotFoundException}
     *
     * @param obj
     * @param msg
     * @throws NotFoundException
     */
    protected void assertFound(Object obj, String msg) throws NotFoundException {
        if (obj == null) {
            throw new NotFoundException(msg);
        }

    }
}
