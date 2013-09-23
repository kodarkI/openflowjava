/* Copyright (C)2013 Pantheon Technologies, s.r.o. All rights reserved. */
package org.opendaylight.openflowjava.protocol.impl.deserialization.factories;

import io.netty.buffer.ByteBuf;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.openflowjava.protocol.impl.util.BufferHelper;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.RoleRequestOutput;

/**
 * @author timotej.kubas
 * @author michal.polkorab
 */
public class RoleReplyMessageFactoryTest {

   /**
    * Testing of {@link RoleReplyMessageFactory} for correct translation into POJO
    */
    @Test
    public void test(){
        ByteBuf bb = BufferHelper.buildBuffer("00 00 00 02 00 00 00 00 00 01 02 03 04 05 06 07");
        RoleRequestOutput builtByFactory = BufferHelper.decodeV13(RoleReplyMessageFactory.getInstance(), bb);

        BufferHelper.checkHeaderV13(builtByFactory);
       
        Assert.assertEquals("Wrong role", 0x02, builtByFactory.getRole().getIntValue());
        Assert.assertEquals("Wrong generationId", 0x01020304050607L, builtByFactory.getGenerationId().longValue());
    }
}
