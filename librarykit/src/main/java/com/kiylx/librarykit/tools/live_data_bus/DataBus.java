package com.kiylx.librarykit.tools.live_data_bus;
import com.kiylx.librarykit.tools.live_data_bus.core.BusCore;
import com.kiylx.librarykit.tools.live_data_bus.core.Channel;

/**
 * 创建者 kiylx
 * 创建时间 2020/12/31 20:30
 * packageName：com.crystal.aplayer.module_base.tools.live_data_bus
 * 描述：
 */
public class DataBus {
    public static <T> Channel<T> with(String channelName) {
        return BusCore.getInstance().get(channelName);
    }
}
