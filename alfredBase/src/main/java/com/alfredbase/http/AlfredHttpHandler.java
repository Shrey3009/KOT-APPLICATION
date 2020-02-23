package com.alfredbase.http;

import com.alfredbase.javabean.NanoHTTPD.Method;
import com.alfredbase.javabean.NanoHTTPD.Response;

import java.io.IOException;
import java.util.Map;

public interface AlfredHttpHandler {
   Response doPost(String uri, Method mothod, Map<String, String> params, String body);
   Response doDesktopPost(String uri, Method mothod, Map<String, String> params, String body);
   Response doSubPosPost(String uri, Method mothod, Map<String, String> params, String body);
   Response doKPMGPost(String uri, Method mothod, Map<String, String> params, String body);
   Response doGet(String uri, Method mothod, Map<String, String> params, String body) throws IOException;
   Response doStaticFile(String uri, Method mothod, Map<String, String> params, String body);


}
