package com.example.demo.http.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class ListFileHandler extends ChannelInboundHandlerAdapter {

    private              File         dir;
    private              ObjectMapper mapper  = new ObjectMapper();
    private static final Charset      CHARSET = Charset.forName("utf-8");

    public ListFileHandler(File dir)
    {
        this.dir = dir;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        String          uri             = fullHttpRequest.uri();
        System.out.println(uri + "-" + ctx.channel().remoteAddress().toString());
        File dest = dir;
        for (final String each : uri.split("/"))
        {
            File[] files = dest.listFiles(new FileFilter()
            {
                @Override
                public boolean accept(File pathname)
                {
                    return pathname.getName().equals(each);
                }
            });
            if (files.length == 1)
            {
                dest = files[0];
            }
        }
        System.out.println("最终搜索：" + dest);
        if (dest.isDirectory())
        {
            String[] list   = dest.list();
            String   s      = mapper.writeValueAsString(list);
            ByteBuf  buffer = ctx.alloc().buffer();
            buffer.writeBytes(s.getBytes(CHARSET));
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
            response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";" + HttpHeaderValues.CHARSET + "=utf8");
            response.headers().add(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());
            ctx.channel().writeAndFlush(response);
        }
        else
        {
            FileInputStream inputStream = new FileInputStream(dest);
            ByteBuf         buffer      = ctx.alloc().buffer();
            buffer.writeBytes(inputStream, inputStream.available());
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());
            response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM);
            ctx.write(response);
            HttpContent chunk = new DefaultHttpContent(buffer);
            ctx.write(chunk);
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        }
    }
}
