package com.funstill.netty.protobuf.demo.client;

import java.net.InetSocketAddress;

import com.funstill.netty.protobuf.demo.model.ProtoMsg;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Sends one message when a connection is open and echoes back any received data
 * to the server. Simply put, the echo client initiates the ping-pong traffic
 * between the echo client and server by sending the first message to the
 * server.
 */
public final class UdpClientStarter {

	//服务器ip
	static final String HOST = System.getProperty("host", "127.0.0.1");
	//服务器端口
	static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

	public static void main(String[] args) throws Exception {
		new UdpClientStarter().connect();
	}

	public void connect() {
		 EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class).handler(new UdpClientHandler());
			Channel ch = b.bind(0).sync().channel();

			//发送一条消息
			ProtoMsg.Message.Builder builder = ProtoMsg.Message.newBuilder();
			builder.setContent("HelloWorld");
			DatagramPacket msg=new DatagramPacket(Unpooled.wrappedBuffer(builder.build().toByteArray()),
            		 new InetSocketAddress(HOST, PORT));
			ch.writeAndFlush(msg).sync();
			ch.closeFuture().await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
