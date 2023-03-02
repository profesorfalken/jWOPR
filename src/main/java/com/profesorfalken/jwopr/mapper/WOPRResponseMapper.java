package com.profesorfalken.jwopr.mapper;

import com.profesorfalken.jwopr.response.WOPRResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

/**
 * Component that perform the mapping needed to build a {{@link WOPRResponse}} object from different sources
 *
 * @author Javier Garcia Alonso
 */
public interface WOPRResponseMapper {
    WOPRResponse map(HttpsURLConnection connection) throws IOException;

    WOPRResponse map(IOException ioException);
}
