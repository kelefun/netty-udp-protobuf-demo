package com.funstill.netty.protobuf.demo.server;

import com.funstill.netty.protobuf.demo.model.ProtoMsg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		ByteBuf bytebuf = msg.content();
		final byte[] array;
		// final int offset;
		final int length = bytebuf.readableBytes();
		if (bytebuf.hasArray()) {
			array = bytebuf.array();
			// offset = bytebuf.arrayOffset() + bytebuf.readerIndex();
		} else {
			array = new byte[length];
			bytebuf.getBytes(bytebuf.readerIndex(), array, 0, length);
			// offset = 0;
		}
		ProtoMsg.Message tem = ProtoMsg.Message.parseFrom(array);

		System.out.println("server收到消息:" + tem.getContent());
		// 回复一条信息给客户端(测试回复不用protobuf了)
		DatagramPacket reply = new DatagramPacket(Unpooled.copiedBuffer("Hello，我是Server", CharsetUtil.UTF_8),
				msg.sender());
		ctx.writeAndFlush(reply).sync();

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("server已激活");
		ctx.fireChannelActive();
	}
}
