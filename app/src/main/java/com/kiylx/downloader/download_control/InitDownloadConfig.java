package com.kiylx.downloader.download_control;

import com.kiylx.download_module.Context;
import com.kiylx.download_module.Downloads;
import com.kiylx.download_module.lib_core.model.DownloadInfo;
import com.kiylx.download_module.view.ViewsCenter;
import com.kiylx.download_module.viewTest.TextConsel;

public class InitDownloadConfig {
    public Context.ContextConfigs contextConfigs;
    public Downloads downloads;
    public Context context;
    public ViewsCenter viewsCenter;
    public DownloadInfoSources sources;

    private InitDownloadConfig() {
        initContext();
    }

    private void initContext() {
        contextConfigs = new Context.ContextConfigs();
        contextConfigs.setThreadNum(8);

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
