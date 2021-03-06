/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowjava.protocol.impl.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.opendaylight.openflowjava.protocol.api.connection.ThreadConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Class implementing server over UDP for handling incoming connections.
 *
 * @author michal.polkorab
 */
public final class UdpHandler implements ServerFacade {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UdpHandler.class);
    private int port;
    private EventLoopGroup group;
    private final InetAddress startupAddress;
    private final SettableFuture<Boolean> isOnlineFuture;
    private UdpChannelInitializer channelInitializer;
    private ThreadConfiguration threadConfig;

    /**
     * Constructor of UdpHandler that listens on selected port.
     *
     * @param port listening port of UdpHandler server
     */
    public UdpHandler(final int port) {
        this(null, port);
    }

    /**
     * Constructor of UdpHandler that listens on selected address and port.
     * @param address listening address of UdpHandler server
     * @param port listening port of UdpHandler server
     */
    public UdpHandler(final InetAddress address, final int port) {
        this.port = port;
        this.startupAddress = address;
        isOnlineFuture = SettableFuture.create();
    }

    @Override
    public void run() {
        if (threadConfig != null) {
            group = new NioEventLoopGroup(threadConfig.getWorkerThreadCount());
        } else {
            group = new NioEventLoopGroup();
        }
        final ChannelFuture f;
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioDatagramChannel.class)
             .option(ChannelOption.SO_BROADCAST, false)
             .handler(channelInitializer);

            if (startupAddress != null) {
                f = b.bind(startupAddress.getHostAddress(), port).sync();
            } else {
                f = b.bind(port).sync();
            }
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while binding port {}", port, e);
            return;
        }

        try {
            InetSocketAddress isa = (InetSocketAddress) f.channel().localAddress();
            String address = isa.getHostString();

            // Update port, as it may have been specified as 0
            this.port = isa.getPort();

            LOGGER.debug("Address from udpHandler: {}", address);
            isOnlineFuture.set(true);
            LOGGER.info("Switch listener started and ready to accept incoming udp connections on port: {}", port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting for port {} shutdown", port, e);
        } finally {
            shutdown();
        }
    }

    @Override
    public ListenableFuture<Boolean> shutdown() {
        final SettableFuture<Boolean> result = SettableFuture.create();
        group.shutdownGracefully().addListener(new GenericFutureListener<io.netty.util.concurrent.Future<Object>>() {

            @Override
            public void operationComplete(
                    final io.netty.util.concurrent.Future<Object> downResult) throws Exception {
                result.set(downResult.isSuccess());
                if (downResult.cause() != null) {
                    result.setException(downResult.cause());
                }
            }

        });
        return result;
    }

    @Override
    public ListenableFuture<Boolean> getIsOnlineFuture() {
        return isOnlineFuture;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param channelInitializer
     */
    public void setChannelInitializer(UdpChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void setThreadConfig(ThreadConfiguration threadConfig) {
        this.threadConfig = threadConfig;
    }
}