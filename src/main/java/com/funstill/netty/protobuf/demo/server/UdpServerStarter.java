package com.funstill.netty.protobuf.demo.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServerStarter {
	static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
	public static void main(String[] args) {
		new UdpServerStarter().run();
	}

	public void run() {
		Bootstrap b = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		b.group(group).channel(NioDatagramChannel.class).handler(new UdpServerHandler());
		try {
			b.bind(PORT).sync().channel().closeFuture().await();
		} catch (InterruptedException e) {
			System.out.println("服务启动失败");
			e.printStackTrace();
		}
	}
}
