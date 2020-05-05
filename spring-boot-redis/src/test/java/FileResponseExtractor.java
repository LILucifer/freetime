import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.client.ResponseExtractor;

import java.io.*;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/5/2 20:52
 */
public class FileResponseExtractor<T> implements ResponseExtractor<T> {
    @Nullable
    @Override
    public T extractData(ClientHttpResponse response) throws IOException {
        return null;
    }
}
