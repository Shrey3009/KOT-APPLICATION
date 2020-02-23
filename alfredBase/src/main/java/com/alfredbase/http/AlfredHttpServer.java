package com.alfredbase.http;

import com.alfredbase.javabean.NanoHTTPD;
import com.alfredbase.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class AlfredHttpServer extends NanoHTTPD implements AlfredHttpHandler {

    public AlfredHttpServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> body = new HashMap<String, String>();
        Method method = session.getMethod();
        String uri = session.getUri();
        String apiName = uri.substring(1);
        Map<String, String> params = session.getParms();
        LogUtil.d(method + " '" + uri + "' ", params.toString());
        try {
            session.parseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
            return getInternalErrorResponse("");
        } catch (ResponseException e) {
            return getInternalErrorResponse("");
        }
        if (Method.POST.equals(method)) {
            if (apiName.startsWith("desktop")) {

                return this.doDesktopPost(apiName, method, params, body.get("postData"));
            } else if (apiName.startsWith("subPos")) {
                return this.doSubPosPost(apiName, method, params, body.get("postData"));
            } else if (apiName.startsWith("kpmg")) {
                return this.doKPMGPost(apiName, method, params, body.get("postData"));
            } else {
                return this.doPost(apiName, method, params, body.get("postData"));
            }
        } else if (Method.GET.equals(method)) {
            return this.doGet(apiName, method, params, body.get("getData"));
        }
        return new NanoHTTPD.Response("");
    }

    protected Response getNotFoundResponse() {
        return createResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT,
                "Error 404, file not found.");
    }

    public Response getJsonResponse(String data) {
        LogUtil.i("HttpResult", data);
        return createResponse(Response.Status.OK, NanoHTTPD.MIME_JSON,
                data);
    }

    protected Response getForbiddenResponse(String s) {
        return createResponse(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: "
                + s);
    }

    public Response getInternalErrorResponse(String s) {
        return createResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT,
                "INTERNAL ERRROR: " + s);
    }

    protected Response getHtmlrResponse(String type, InputStream inputStream) {
        return createResponse(Response.Status.OK, type, inputStream);
    }

    protected Response getHtmlrResponse(String patch) {
        return createResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, patch);
    }

    // Announce that the file server accepts partial content requests
    private Response createResponse(Response.Status status, String mimeType, InputStream message) {
        Response res = new Response(status, mimeType, message);
        res.addHeader("Accept-Ranges", "bytes");
        return res;
    }

    // Announce that the file server accepts partial content requests
    private Response createResponse(Response.Status status, String mimeType, String message) {
        Response res = new Response(status, mimeType, message);
        res.addHeader("Accept-Ranges", "bytes");
        return res;
    }

    @Override
    public Response doPost(String uri, Method mothod,
                           Map<String, String> params, String body) {

        return getForbiddenResponse("Not Support yet");
    }


    @Override
    public Response doDesktopPost(String uri, Method mothod, Map<String, String> params, String body) {
        return getForbiddenResponse("Not Support yet");
    }

    @Override
    public Response doSubPosPost(String uri, Method mothod, Map<String, String> params, String body) {
        return getForbiddenResponse("Not Support yet");
    }

    @Override
    public Response doKPMGPost(String uri, Method mothod, Map<String, String> params, String body) {
        return getForbiddenResponse("Not Support yet");
    }

    @Override
    public Response doGet(String uri, Method mothod,
                          Map<String, String> params, String body) {

        return getForbiddenResponse("Not Support yet");
    }

    @Override
    public Response doStaticFile(String uri, Method mothod,
                                 Map<String, String> params, String body) {
        // TODO Auto-generated method stub
        return getForbiddenResponse("Not Support yet");
    }

}
