package com.example.demo.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class DownloadHandler2 extends ChannelInboundHandlerAdapter {

    private File file;
    private final int maxContentLength = 1024 * 1024;

    public DownloadHandler2(File file) {
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
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM);
        FileInputStream inputStream = new FileInputStream(dest);
        response.headers().
                add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED).
                add(HttpHeaderNames.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(dest.getName(), "UTF-8"));
        ctx.write(response);
        ByteBuf buffer = ctx.alloc().buffer();
        int need = Math.min(maxContentLength, inputStream.available());
        buffer.writeBytes(inputStream, need);
        ctx.channel().writeAndFlush(buffer).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                int need = Math.min(maxContentLength, inputStream.available());
                if (need != 0) {
                    ByteBuf buffer = ctx.alloc().buffer();
                    buffer.writeBytes(inputStream, need);
                    Thread.sleep(1000);
                    ctx.channel().writeAndFlush(buffer).addListener(this);
                } else {
                    inputStream.close();
                    ctx.channel().writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                }
            }
        });
    }
}
