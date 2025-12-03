package lovely;


import android.os.Handler;
import android.os.Message;


/**
 * Date：2025/7/28
 * Describe:
 */
public class LoveHandler extends Handler {
    @Override
    public void handleMessage(Message message) {
        Ppz.d(message.what);
    }
}
