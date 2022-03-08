package com.kiylx.downloader.core.download_control;

import com.kiylx.download_module.Context;
import com.kiylx.download_module.Downloads;
import com.kiylx.download_module.view.ViewsCenter;
import com.kiylx.downloader.core.store.FileKitImpl;
import com.kiylx.downloader.core.store.RepoImpl;
import com.kiylx.downloader.core.store.SystemCallImpl;
import com.kiylx.librarykit.tools.live_data_bus.core.BusCore;

/**
 * 配置所有下载相关的工具类，它跟随application生命周期，所有databus也在此处被引用
 */
public class InitDownloadConfig {
    public Context.ContextConfigs contextConfigs;
    public Downloads downloads;
    public Context context;
    public ViewsCenter viewsCenter;
    public DownloadInfoSources sources;
    public final BusCore dataBus=BusCore.getInstance();

    private InitDownloadConfig() {
        initContext();
    }

    private void initContext() {
        contextConfigs = new Context.ContextConfigs();
        contextConfigs.setThreadNum(8);
        contextConfigs.setFileKitClazz(FileKitImpl.class);
        contextConfigs.setRepoClazz(RepoImpl.class);
        contextConfigs.setSysCallClazz(SystemCallImpl.class);

        downloads = Downloads.Companion.downloadsInstance(contextConfigs);

        context = downloads.getMContext();
        context.setLimit(2);

        viewsCenter = new ViewsCenter();
        sources=new DownloadInfoSources();
        viewsCenter.addListener(sources);
        viewsCenter.listen();
    }

    public void clear() {
        viewsCenter.unListen();
    }

    enum Singleton {
        SINGLETON;

        private InitDownloadConfig instance;

        private InitDownloadConfig getInstance() {
            if (instance == null)
                instance = new InitDownloadConfig();
            return instance;
        }
    }

    public static InitDownloadConfig getInstance() {
        return Singleton.SINGLETON.getInstance();
    }
}
