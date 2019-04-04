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

package cn.vbill.middleware.porter.common.cluster.client;

import cn.vbill.middleware.porter.common.statistics.StatisticClient;
import java.util.List;

/**
 * 树形结构实现集群客户端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 14:08
 */
public interface ClusterClient extends StatisticClient {

    /**
     * getChildren
     *
     * @date 2018/8/10 下午2:56
     * @param: [nodePath]
     * @return: java.util.List<java.lang.String>
     */
    List<String> getChildren(String nodePath);

    /**
     * getData
     *
     * @date 2018/8/10 下午2:56
     * @param: [nodePath]
     * @return: org.apache.commons.lang3.tuple.Pair<java.lang.String,S>
     */
    TreeNode getData(String nodePath);

    /**
     * create throws exception when errors.
     * @date 2018/8/10 下午2:56
     * @param: [nodePath, isTemp, data]
     * @return: TreeNode
     */
    TreeNode create(String nodePath, boolean isTemp, String data) throws Exception;

    /**
     * create tree node, if already exists, do nothing. No  exception throws when errors.
     * @param nodePath
     * @param data
     * @param isTemp
     * @param watch
     * @return
     */
    default LockVersion create(String nodePath, String data, boolean isTemp, boolean watch) {
        LockVersion stat = null;
        try {
            stat = exists(nodePath, watch);
            if (null == stat) {
                TreeNode newVersion = create(nodePath, isTemp, data);
                stat = null != newVersion ? newVersion.getVersion() : null;
            }
        } catch (Exception e) {
        }
        return stat;
    }

    /**
     * create root node
     * @param nodePath
     * @param isTemp
     * @throws Exception
     */
    default void createRoot(String nodePath, boolean isTemp) throws Exception {
        create(nodePath, isTemp, null);
    }


    /**
     * changeData and return new version. If no tree node exists, create new. No  exception throws when errors.
     * @date 2018/8/10 下午2:56
     * @param: [nodePath, data, version]
     */
    default void changeData(String path, boolean isTemp, boolean watch,  String data) {
        try {
            LockVersion stat = exists(path, watch);
            if (null == stat) {
                create(path, isTemp, data);
            } else {
                setData(path, data, stat);
            }
        } catch (Throwable e) {
        }
    }

    /**
     * setData and return new version.
     * @date 2018/8/10 下午2:56
     * @param: [nodePath, data, version]
     * @return: LockVersion
     */
    LockVersion setData(String nodePath, String data, LockVersion version) throws Exception;

    /**
     * exists return version.if not exists, return null.
     * @date 2018/8/10 下午2:57
     * @param: [nodePath, watch]
     * @return: Integer
     */
    LockVersion exists(String nodePath, boolean watch) throws Exception;



    /**
     * isExists
     * @param path
     * @param watch
     * @return
     */
    default boolean isExists(String path, boolean watch) {
        try {
            LockVersion stat = exists(path, watch);
            return null != stat;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * delete
     * @author FuZizheng
     * @date 2018/8/10 下午2:57
     * @param: [nodePath]
     * @return: void
     */
    void delete(String nodePath);


    /**
     * 代理统计指标上传客户端
     * @param client
     * @throws Exception
     */
    void setStatisticClient(StatisticClient client) throws Exception;

    /**
     * 活跃状态检测
     * @date 2018/8/10 下午2:57
     * @param: []
     * @return: boolean
     */
    boolean alive();

    /**
     * Client断开重连
     *
     * @date 2018/8/10 下午2:57
     * @param: []
     * @return: void
     */
    default void clientSpinning() {

    }

    class  TreeNode {
        private String path;
        private String data;
        private LockVersion version;

        public TreeNode(String path, String data, LockVersion version) {
            this.path = path;
            this.data = data;
            this.version = version;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public LockVersion getVersion() {
            return version;
        }

        public void setVersion(LockVersion version) {
            this.version = version;
        }
    }

    class LockVersion {
        private Integer version;

        public static LockVersion newVersion(int version) {
            return new LockVersion(version);
        }

        public LockVersion(Integer version) {
            this.version = version;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }
    }
}
