package lovely;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


/**
 * Date：2025/7/28
 * Describe:
 */
public class PaperWCC extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);
        if (i10 == 100) {
            Ppz.d(i10);
        }
    }
}
