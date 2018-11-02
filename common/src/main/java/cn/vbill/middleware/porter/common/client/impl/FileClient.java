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

package cn.vbill.middleware.porter.common.client.impl;

import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.client.StatisticClient;
import cn.vbill.middleware.porter.common.config.source.FileOperationConfig;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:37
 */
public class FileClient extends AbstractClient<FileOperationConfig> implements ClusterClient<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileClient.class);

    private String workspace;
    public FileClient(FileOperationConfig config) {
        super(config);
    }

    @Override
    protected void doStart() throws IOException {
        FileOperationConfig config = getConfig();
        workspace = config.getHome();
        new File(workspace).mkdirs();
    }

    @Override
    protected void doShutdown() {
        //do nothing
    }

    @Override
    protected boolean isAlready() {
        return true;
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return Files.list(Paths.get(getRealPath(path))).map(p -> p.toString().replace(workspace + "/", "")).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.warn("getChildren {} fail.", path, e);
        }
        return new ArrayList<>(0);
    }

    @Override
    public Pair<String, Boolean> getData(String path) {
        String content = null;
        try {
            content = Files.lines(Paths.get(getRealPath(path))).reduce((p, n) -> p + n).orElse("");
        } catch (IOException e) {
            LOGGER.warn("getData {} fail.", path, e);
        }
        return new ImmutablePair<>(content, null == content);
    }

    @Override
    public String create(String path, boolean isTemp, String data) throws IOException {
        setData(path, data, -1);
        return path;
    }

    public void createDir(String path) throws IOException {
        Files.createDirectories(Paths.get(getRealPath(path)));
    }

    @Override
    public Boolean setData(String path, String data, int version) throws IOException {
        Path pathObj = Paths.get(getRealPath(path));
        delete(path);
        return null != Files.write(pathObj, data.getBytes());
    }

    @Override
    public Boolean exists(String path, boolean watch) {
        return Files.exists(Paths.get(getRealPath(path)));
    }

    @Override
    public void delete(String path) {
        try {
            Files.deleteIfExists(Paths.get(getRealPath(path)));
        } catch (Throwable e) {
            LOGGER.warn("delete {} fail.", path, e);
        }
    }

    @Override
    public boolean alive() {
        return true;
    }

    @Override
    public void setStatisticClient(StatisticClient client) {
        //do nothing
    }

    private String getRealPath(String path) {
        return workspace + path;
    }

    /**
     * createWhenNotExists
     * @param path
     * @param isTemp
     * @param watch
     * @param data
     * @return
     */
    public Boolean  createWhenNotExists(String path, boolean isTemp, boolean watch,  String data) {
        Boolean stat = false;
        try {
            stat = exists(path, watch);
            if (!stat) {
                create(path, isTemp, data);
            }
        } catch (Exception e) {
            LOGGER.warn("create {} fail", path, e);
        }
        return stat;
    }
}
