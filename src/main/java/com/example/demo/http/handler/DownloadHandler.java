package com.example.demo.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.URLDecoder;

public class DownloadHandler extends ChannelInboundHandlerAdapter {

    private File file;

    public DownloadHandler(File file) {
        this.file = file;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fullHttpRequest = (FullHttpRequest)msg;
        String uri = fullHttpRequest.uri();
        uri = URLDecoder.decode(uri, "UTF-8");
        File dest = file;
        for (String each : uri.split("/")) {
            File[] files = dest.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().equals(each);
                }
            });
            if (files.length == 1)
                dest = files[0];
        }
        FileInputStream inputStream = new FileInputStream(dest);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(inputStream, inputStream.available());
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM);
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH, inputStream.available());
        ctx.write(response);
        HttpContent chunk = new DefaultHttpContent(buffer);
        ctx.channel().write(chunk);
        ctx.channel().writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }
}
